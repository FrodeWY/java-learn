package com.rpc.core.cluster;

import com.rpc.core.api.Cluster;

/**
 * @author wangyang
 * @ClassName ClassFactory
 * @Description TODO
 * @Date 2021/8/18 下午8:28
 * @Version 1.0
 */
public class ClusterFactory {

  public static Cluster getCluster(String type) {
    Cluster cluster = null;
    if (FailfastCluster.NAME.equals(type)) {
      cluster = new FailfastCluster();
    } else {
      throw new IllegalArgumentException("not found class type is :" + type + " cluster");
    }
    return cluster;
  }
}
