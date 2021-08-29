package com.rpc.core.config.center;

import com.rpc.core.api.ConfigCenterListener;
import com.rpc.core.config.center.info.ConfigCenterInfo;
import com.rpc.core.config.center.info.ZookeeperConfigCenterInfo;
import com.rpc.core.utils.YamlAndPropertiesConvertUtil;
import java.io.FileInputStream;
import java.util.Map;
import java.util.Properties;
import java.util.concurrent.ConcurrentHashMap;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang.StringUtils;
import org.apache.curator.RetryPolicy;
import org.apache.curator.framework.CuratorFramework;
import org.apache.curator.framework.CuratorFrameworkFactory;
import org.apache.curator.framework.CuratorFrameworkFactory.Builder;
import org.apache.curator.framework.api.CuratorWatcher;
import org.apache.curator.retry.ExponentialBackoffRetry;
import org.apache.zookeeper.WatchedEvent;
import org.apache.zookeeper.Watcher.Event.KeeperState;

/**
 * @author wangyang
 * @ClassName ZookeeperConfigCenterInfo
 * @Description zookeeper 配置中心信息
 * @Date 2021/8/28 上午8:56
 * @Version 1.0
 */
@Slf4j
public class ZookeeperConfigCenter implements ConfigCenter {

  public static final String NAME = "zookeeper";
  private static Map<String, CuratorFramework> CACHE = new ConcurrentHashMap<>();

  @Override
  public Properties getProperties(ConfigCenterInfo info) {
    if (!(info instanceof ZookeeperConfigCenterInfo)) {
      throw new IllegalArgumentException();
    }
    ZookeeperConfigCenterInfo zookeeperConfigCenterInfo = (ZookeeperConfigCenterInfo) info;
    CuratorFramework curatorFramework = CACHE
        .computeIfAbsent(zookeeperConfigCenterInfo.centerUniqueKey(), k -> createClient(zookeeperConfigCenterInfo));
    try {
      byte[] bytes = curatorFramework.getData().forPath(zookeeperConfigCenterInfo.getDataId());
      return YamlAndPropertiesConvertUtil.getProperties(bytes);
    } catch (Exception e) {
      throw new RuntimeException(e);
    }
  }


  private CuratorFramework getClient(ConfigCenterInfo info) {
    return CACHE.get(info.centerUniqueKey());
  }


  @Override
  public Properties getPropertiesAsync(ConfigCenterInfo info, ConfigCenterListener listener) {
    if (!(info instanceof ZookeeperConfigCenterInfo)) {
      throw new IllegalArgumentException();
    }
    ZookeeperConfigCenterInfo zookeeperConfigCenterInfo = (ZookeeperConfigCenterInfo) info;
    CuratorFramework curatorFramework = CACHE
        .computeIfAbsent(zookeeperConfigCenterInfo.centerUniqueKey(), k -> createClient(zookeeperConfigCenterInfo));
    try {
      ZookeeperConfigCenterListener centerListener = new ZookeeperConfigCenterListener(listener, curatorFramework,
          zookeeperConfigCenterInfo.getUrl());
      byte[] bytes = curatorFramework.getData().usingWatcher(centerListener).forPath(((ZookeeperConfigCenterInfo) info).getDataId());
      return YamlAndPropertiesConvertUtil.getProperties(bytes);
    } catch (Exception e) {
      throw new RuntimeException(e);
    }
  }

  private CuratorFramework createClient(ZookeeperConfigCenterInfo info) {
    RetryPolicy retryPolicy = new ExponentialBackoffRetry(1000, 3);
    Builder builder = CuratorFrameworkFactory.builder().namespace(info.getNamespace())
        .connectString(info.getUrl())
        .retryPolicy(retryPolicy);

    if (!StringUtils.isBlank(info.getPassword()) && !StringUtils.isBlank(info.getUsername())) {
      builder.authorization("digest", (info.getUsername() + ":" + info.getPassword()).getBytes());
    }
    CuratorFramework curatorFramework = builder.build();
    curatorFramework.start();
    return curatorFramework;
  }


  private static class ZookeeperConfigCenterListener implements CuratorWatcher {

    private final ConfigCenterListener listener;
    private final String url;
    private final CuratorFramework curatorFramework;

    public ZookeeperConfigCenterListener(ConfigCenterListener listener, CuratorFramework curatorFramework, String url) {
      this.listener = listener;
      this.url = url;
      this.curatorFramework = curatorFramework;
    }

    @Override
    public void process(WatchedEvent event) throws Exception {
      KeeperState state = event.getState();
      if (state.equals(KeeperState.Disconnected)) {
        log.warn("zookeeper:{} config center disconnected", url);
      }
      String path = event.getPath();
      byte[] bytes = curatorFramework.getData().usingWatcher(this).forPath(path);
      listener.notify(YamlAndPropertiesConvertUtil.getProperties(bytes));
    }
  }

  public static void main(String[] args) throws Exception {

    ZookeeperConfigCenter center = new ZookeeperConfigCenter();
    ZookeeperConfigCenterInfo info = new ZookeeperConfigCenterInfo();
    info.setUrl("127.0.0.1:2181");
    info.setNamespace("rpc-demo");
    info.setDataId("/provider1");
    CuratorFramework client = center.createClient(info);
    FileInputStream inputStream = new FileInputStream(
        "/Users/wangyang/Desktop/workspace/workspace2/java-learn/rpc/rpcfx-demo-provider/src/main/resources/application.yml");
    int available = inputStream.available();
    byte[] bytes = new byte[available];
    inputStream.read(bytes);
    client.setData().forPath(info.getDataId(), bytes);
//
//    byte[] bytes1 = client.getData().forPath(info.getDataId());
//    String s = new String(bytes1);
//    System.out.println(s);

//    Properties map = yaml.loadAs(s, Properties.class);
//
//    Properties properties = center.getProperties(info);
//    System.out.println(properties);
//    System.out.println(YamlAndPropertiesConvertUtil.getValue("server.port",properties));
//    System.out.println(YamlAndPropertiesConvertUtil.getValue("rpc.props.consumer.loadBalance",properties));
  }
}
