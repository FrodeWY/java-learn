# redis环境搭建文档

### 环境

- ubuntu

## 主从复制

- 下载redis源码包并编译安装

  ```shell
  $ wget https://download.redis.io/releases/redis-6.2.5.tar.gz
  $ tar xzf redis-6.2.5.tar.gz
  $ cd redis-6.2.5
  $ make
  ```

- 配置两份redis配置文件

  - redis1.conf

    ```
    port 6379
    dbfilename dump1.rdb
    dir /home/wy/soft/redis/redis1-data
    ```

  - redis2.conf

    ```
    port 6380
    dbfilename dump2.rdb
    dir /home/wy/soft/redis/redis2-data
    ```

    

- 创建两个数据目录 redis1-data， redis2-data

- 启动redis

  > ./redis-server  redis1.conf
  >
  > ./redis-server  redis2.conf

- 进入redis2服务

  > ./redis-cli -h 127.0.0.1 -p 6380

- 配置主从关系

  > slaveof 127.0.0.1 6379

- 验证

  > info 
  >
  > 
  >
  > role:**slave**
  > master_host:127.0.0.1
  > master_port:6379
  > master_link_status:up

## sentinel 高可用 （3节点）

- 创建3份**sentinel.conf**

  - sentinel1.conf

    ```
    dir /tmp/sentinel-01
    port 26379
    pidfile /var/run/redis-sentinel-01.pid
    sentinel monitor mymaster 127.0.0.1 6379 2
    sentinel down-after-milliseconds mymaster 30000
    sentinel parallel-syncs mymaster 1
    sentinel failover-timeout mymaster 180000
    ```

    

  - sentinel2.conf

    ```
    dir /tmp/sentinel-02
    port 26380
    pidfile /var/run/redis-sentinel-02.pid
    sentinel monitor mymaster 127.0.0.1 6379 2
    sentinel down-after-milliseconds mymaster 30000
    sentinel parallel-syncs mymaster 1
    sentinel failover-timeout mymaster 180000
    ```

    

  - sentinel3.conf

    ```
    dir /tmp/sentinel-03
    port 26381
    pidfile /var/run/redis-sentinel-03.pid
    sentinel monitor mymaster 127.0.0.1 6379 2
    sentinel down-after-milliseconds mymaster 30000
    sentinel parallel-syncs mymaster 1
    sentinel failover-timeout mymaster 180000
    ```

    

- 分别启动sentinel服务

  >nohup redis-sentinel   sentinel1.conf>sentinel1.out &
  >
  >nohup redis-sentinel   sentinel2.conf>sentinel2.out &
  >
  >nohup redis-sentinel   sentinel3.conf>sentinel3.out &

- 验证

  - kill 主节点

  - sentinel日志输出，可以看到master节点已经切换到了6380

    ```
    10867:X 08 Sep 2021 23:40:14.270 # +sdown master mymaster 127.0.0.1 6379
    10867:X 08 Sep 2021 23:40:14.353 # +odown master mymaster 127.0.0.1 6379 #quorum 2/2
    10867:X 08 Sep 2021 23:40:14.354 # +new-epoch 1
    10867:X 08 Sep 2021 23:40:14.354 # +try-failover master mymaster 127.0.0.1 6379
    10867:X 08 Sep 2021 23:40:16.491 # +switch-master mymaster 127.0.0.1 6379 127.0.0.1 6380
    10867:X 08 Sep 2021 23:40:16.492 * +slave slave 127.0.0.1:6379 127.0.0.1 6379 @ mymaster 127.0.0.1 6380
    
    ```

    

### cluster搭建(3主3从)

- **mkdir** -p /usr/**local**/redis_cluster/redis_63{79,80,81,82,83,84}/{conf,pid,logs}

- vim /usr/local/redis_cluster/redis_6379/conf/redis.cnf

  ```json
  # 快速修改：:%s/6379/6380/g
  # 守护进行模式启动
  daemonize yes
  
  # 设置数据库数量，默认数据库为0
  databases 16
  
  # 绑定地址，需要修改
  bind 192.168.0.120
  
  # 绑定端口，需要修改
  port 6379
  
  # pid文件存储位置，文件名需要修改
  pidfile /usr/local/redis_cluster/redis_6379/pid/redis_6379.pid
  
  # log文件存储位置，文件名需要修改
  logfile /usr/local/redis_cluster/redis_6379/logs/redis_6379.log
  
  # RDB快照备份文件名，文件名需要修改
  dbfilename redis_6379.rdb
  
  # 本地数据库存储目录，需要修改
  dir /usr/local/redis_cluster/redis_6379
  
  # 集群相关配置
  # 是否以集群模式启动
  cluster-enabled yes
  
  # 集群节点回应最长时间，超过该时间被认为下线
  cluster-node-timeout 15000
  
  # 生成的集群节点配置文件名，文件名需要修改
  cluster-config-file nodes_6379.conf
  ```

- 分别启动

  > redis-server ./redis_6379/conf/redis.cnf

- 建立集群

  > ./redis-cli --cluster create 127.0.0.1:6379 127.0.0.1:6380 127.0.0.1:6381 127.0.0.1:6382 127.0.0.1:6383 127.0.0.1:6384 --cluster-replicas 1

