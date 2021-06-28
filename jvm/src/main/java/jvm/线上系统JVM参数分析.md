# 线上系统JVM参数分析

## 1. JVM参数

```
 -Xms6G -Xmx6G -Xss256k -XX:NewRatio=2 -XX:+HeapDumpOnOutOfMemoryError
 -XX:HeapDumpPath=./logs/hd.hprof -XX:+UseG1GC -XX:+PrintGCDateStamps -XX:+PrintGCDetails
 -Xloggc:./logs/gc.log
```



## 2. JVM行命令执行情况

由于线上环境没法截图,所以记录执行jvm行命令后的情况

- jstat -t -gc pid 1000 100
  - Eden区平缓增长
  - 业务高峰期时young GC后 Survivor To区利用率100%,有一部分对象放入了老年代
  - 老年代使用率在3.6%-6.4%之间
- jstack -l pid
  - 没有发现死锁



## 3 分析

- Young GC后幸存对象因为survivor区内存空间不足,通过分配担保直接分配到老年代
- old 区内存利用率很低,说明进程中并没有很多强引用,大多是局部变量,也说明old区可能并不需要如此大的内存空间
- 可以适当减少堆内存大小,目前的业务量下,6g最大堆内存有点过大,增加了单次GC 的STW时间,可以根据实际情况适量减少
- G1 GC会根据运行情况调整各个分区的大小,不应该设置-XX:NewRatio,且没有配置-XX:-UseAdaptiveSizePolicy 则 -XX:NewRatio的配置也不会真正生效,,所以参数-XX:NewRatio=2没有必要,可以去除







