package com.rpc.core.common;

/**
 * 目录枚举
 */
public enum CatalogueEnum {
  /**
   * 提供者目录
   */
  PROVIDER("provider"),
  /**
   * 配置目录
   */
  CONFIGURATOR("configurator"),
  /**
   * 消费者目录
   */
  CONSUMER("consumer");

  private String name;

  public String getName() {
    return name;
  }

  CatalogueEnum(String name) {
    this.name = name;
  }
}
