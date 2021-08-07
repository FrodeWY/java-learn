

# 第七周作业

## 分布式id生成器

> seventh.com.util.DistributionIdGenerator

- 分布式id格式====>  {时间戳}+{6位自增值}

- 支持单个和批量申请方式
- 6核处理器,10个线程的压测下,每秒能产生128w个分布式id
- 理论情况下,每毫秒最大支持申请10^5个分布式id,超多最大值则自旋获取下一毫秒的分布式id



## 不同方式的插入效率

> seventh.com.batchload.JDBCBatchLoader

### 插入100w订单数据

| 插入方式          | 耗时(ms) | 备注                                         |
| ----------------- | -------- | -------------------------------------------- |
| PreparedStatement | 96009    | 单线程前提下,每次提交10000条记录,分100次提交 |
| Load Data         | 14797    |                                              |
| Multi Value       | 23073    | 单线程前提下,每次提交10000条记录,分100次提交 |

### 插入1000w订单数据

| 插入方式          | 耗时(ms) | 备注                                          |
| ----------------- | -------- | --------------------------------------------- |
| PreparedStatement | 1049039  | 单线程前提下,每次提交10000条记录,分1000次提交 |
| Load Data         | 128819   |                                               |
| Multi Value       | 230463   | 单线程前提下,每次提交10000条记录,分1000次提交 |

### 结论

1.在相同硬件配置下插入性能

> Load Data>Multi Value>PreparedStatement

2.插入时间与插入数据量成线性成线性增长



## 读写分离V1

### 读写分离V1

####  实现原理

- read-write模块引用read-write-splitting-starter
- read-write-splitting-starter使用AbstractRoutingDataSource实现了DynamicDataSource
- 根据readwrite.datasource属性配置获取用户配置的数据源,从而对DynamicDataSource进行数据源初始化
- 使用切面拦截service目录下的方法,根据@ReadOnly进行读写分离
- DynamicDataSource中使用LoadBalance进行负载均衡



### 读写分离V2

#### 实现原理

- 使用shardingsphere-jdbc,实现读写分离逻辑和业务代码的解耦



### 读写分离V3

#### 实现原理

- 搭建shardingsphere-proxy,配置好相应的主库从库
- 项目配置直接将proxy作为mysql连接,将读写分离逻辑交给shardingsphere-proxy实现
