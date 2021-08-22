package com.rpc.core.registry;


import com.rpc.core.api.Listener;
import com.rpc.core.api.Registry;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang.StringUtils;
import org.apache.curator.RetryPolicy;
import org.apache.curator.framework.CuratorFramework;
import org.apache.curator.framework.CuratorFrameworkFactory;
import org.apache.curator.framework.api.CuratorWatcher;
import org.apache.curator.retry.ExponentialBackoffRetry;
import org.apache.zookeeper.CreateMode;
import org.apache.zookeeper.WatchedEvent;
import org.apache.zookeeper.Watcher;
import org.apache.zookeeper.Watcher.Event.KeeperState;

@Slf4j
public class ZookeeperRegistry implements Registry {

  public static final String NAME = "zookeeper";
  private CuratorFramework client;

  private static final Map<String, ConcurrentHashMap<Listener, ZKChildListener>> ZK_CHILD_LISTENER_MAP = new ConcurrentHashMap<>();
  private static final Map<String, ZKReRegisterListener> ZK_RE_REGISTER_LISTENER_MAP = new ConcurrentHashMap<>();

  public ZookeeperRegistry(String registryAddress) {
    RetryPolicy retryPolicy = new ExponentialBackoffRetry(500, 3);
    client = CuratorFrameworkFactory.builder().retryPolicy(retryPolicy)
        .connectString(registryAddress).build();
    client.start();
  }

  @Override
  public void subscribe(String subscribePath, Listener listener) {
    if (StringUtils.isBlank(subscribePath)) {
      return;
    }
    ConcurrentHashMap<Listener, ZKChildListener> listenerMap;
    if ((ZK_CHILD_LISTENER_MAP.get(subscribePath)) == null) {
      ZK_CHILD_LISTENER_MAP.putIfAbsent(subscribePath, new ConcurrentHashMap<>());
    }
    listenerMap = ZK_CHILD_LISTENER_MAP.get(subscribePath);
    ZKChildListener childListener = listenerMap.get(listener);
    if (childListener == null) {
      ZKChildListener zkChildListener = new ZKChildListener(client, listener);
      listenerMap.putIfAbsent(listener, zkChildListener);
      childListener = listenerMap.get(listener);
    }
    List<String> updateChildPaths = new ArrayList<>();
    try {
      List<String> childPaths = client.getChildren().usingWatcher(childListener)
          .forPath(subscribePath);
      if (childPaths != null && childPaths.size() > 0) {
        updateChildPaths.addAll(childPaths);
      }
    } catch (Exception e) {
      e.printStackTrace();
    }
    listener.notify(updateChildPaths);
  }


  @Override
  public void unSubscribe(String unSubscribePath, Listener listener) {
    ConcurrentHashMap<Listener, ZKChildListener> childListenerMap = ZK_CHILD_LISTENER_MAP.get(unSubscribePath);
    if (childListenerMap != null) {
      ZKChildListener zkChildListener = childListenerMap.get(listener);
      if (zkChildListener != null) {
        zkChildListener.stop();
      }
    }
  }

  @Override
  public void register(String registerPath) {
    try {
      client.create().creatingParentsIfNeeded().withMode(CreateMode.EPHEMERAL).forPath(registerPath);
      //注册watch,当注册节点因为网络抖动导致删除时,重新注册znode
      ZKReRegisterListener zkReRegisterListener = ZK_RE_REGISTER_LISTENER_MAP
          .computeIfAbsent(registerPath, k -> new ZKReRegisterListener(client, registerPath));
      client.watchers().add().usingWatcher(zkReRegisterListener).forPath(registerPath);
    } catch (Exception e) {
      log.warn(e.getMessage());
    }
  }

  @Override
  public void unRegister(String unRegisterPath) {
    try {
      client.delete().guaranteed().deletingChildrenIfNeeded().forPath(unRegisterPath);
    } catch (Exception e) {
      e.printStackTrace();
    }
  }

  @Override
  public void destroy() {
    for (ConcurrentHashMap<Listener, ZKChildListener> childListenerMap : ZK_CHILD_LISTENER_MAP.values()) {
      for (ZKChildListener zkChildListener : childListenerMap.values()) {
        zkChildListener.stop();
      }
    }
    client.close();

  }

  @Slf4j
  public static class ZKChildListener implements CuratorWatcher {

    private final CuratorFramework client;
    private Listener listener;

    public ZKChildListener(CuratorFramework client, Listener listener) {
      this.client = client;
      this.listener = listener;
    }

    public void stop() {
      listener = null;
    }

    @Override
    public void process(WatchedEvent event) throws Exception {
      log.warn("ZKChildListener WatchedEvent eventType:{},eventState:{}", event.getType(), event.getState());
      if (listener != null) {
        if (event.getState() == KeeperState.Disconnected) {//注册中心断连,使用缓存
          return;
        }
        String path = event.getPath() == null ? "" : event.getPath();
        listener.notify(
            StringUtils.isNotBlank(path) ? client.getChildren().usingWatcher(this).forPath(path)
                : Collections
                    .emptyList());
      }
    }
  }

  @Slf4j
  public static class ZKReRegisterListener implements CuratorWatcher {

    private final CuratorFramework client;
    private String registerPath;

    public ZKReRegisterListener(CuratorFramework client, String registerPath) {
      this.client = client;
      this.registerPath = registerPath;
    }

    @Override
    public void process(WatchedEvent watchedEvent) throws Exception {
      log.warn("ZKReRegisterListener WatchedEvent eventType:{},eventState:{}", watchedEvent.getType(), watchedEvent.getState());
      if (watchedEvent.getType() == Watcher.Event.EventType.NodeDeleted) {
        client.create().creatingParentsIfNeeded().withMode(CreateMode.EPHEMERAL).forPath(registerPath);
        client.watchers().add().usingWatcher(this).forPath(registerPath);
      }
    }
  }
}
