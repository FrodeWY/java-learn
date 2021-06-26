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
  - 每次young GC后 Survivor To区利用率100%,有一部分对象放入了老年代
  - 老年代使用率只有3.6%
  - 老年代:年轻代=2:1
- jstack -l pid
  - 没有发现死锁



## 3 分析

- Young GC后幸存对象因为survivor区内存空间不足,通过分配担保直接分配到老年代
- old 区内存利用率很低,说明进程中并没有很多强引用,大多是局部变量,说明old区可能并不需要如此大的内存空间
- G1会根据运行情况自动调整每个分代的Region数量,但是由于设置**-XX:NewRatio=2** ,使得分代比例固定,使得young区空间不足,而old区空间过剩

## 结论

> -XX:NewRatio=2参数的设置影响力G1对分区的自动分配,导致了老年代空间过剩,年轻代每次GC都会有一部分幸存对象因为survivor to区空间不足,晋升到老年代,



