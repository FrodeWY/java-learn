# 不同 GC 和堆内存的总结



## 不同GC收集器

### 1. 串行(Serial+Serial old)

- 由于只有一个GC worker线程执行垃圾收集,不能并行处理,所以会触发STW
- 也因为只有一个线程进行垃圾收集,所以单次GC停顿时间较长,吞吐量低
- 不能利用多核cpu的优势,适合作为单核cpu的垃圾收集器

### 2. 并行(Parallel Scavenge+Paralle old)

- 多个GC worker 线程并行执行垃圾收集,小于8核时默认并行线程数等于cpu核数
- GC执行期间,所有cpu内核都在并行垃圾收集,所以也会触发STW
- 相对于串行收集器,并行收集器充分利用了CPU多核资源,所以总暂停时间更短,吞吐量高
- 并行垃圾收集器关注的是吞吐量,所以更适合后台计算多,交互少的应用
- 适合作为多核cpu的垃圾收集器

### 3.ParNew+CMS

- Young GC 使用的是ParNew收集器
- CMS使用多个Worker线程并发的执行垃圾收集
- 初始标记(Initial Mark)阶段,这个阶段伴随着 **STW** 暂停,但只标记直接被GC Roots引用的老年代对象,以及年轻代引用的老年代对象所以耗时很短
- 并发标记(concurrent-mark),并发预清理(concurrent-preclean)阶段都是和用户线程并发执行的,不会导致STW
- 在cms收集过程中可能会伴随一次或多次Young GC
- 最终标记(Final Remark)阶段,这个阶段的目标是完成所有对象的最终标记,所以需要一次**STW**,停止应用线程并发执行带来的引用变化
- 并发清除(concurrent-sweep)此阶段和应用线程并发执行,删掉不可达的对象，并回收它们的空间。
- 并发重置(concurrent-reset),重置 CMS 算法相关的内部数据
- CMS整个回收阶段会伴随两次短暂的STW,大多数工作都是和用户线程并发执行的,所以停顿时间相对并行收集器更短,降低了业务系统的停顿时间
- CMS更适合交互多的场景

### 4. G1

- G1也是使用多个Worker线程并发的执行垃圾收集

- G1每次停顿的时长几乎都小于-XX:MaxGCPauseMillis设置的预期停顿时间

- G1将内存划分成了很多**Region**,从日志上能看出G1会动态的调整各个分区的Region数量,对于Young 区调整Region数量,对于old区调整单次回收的region个数,从而满足预期暂停时间

- 因为会根据MaxGCPauseMillis调整每次回收的old region个数,所以从日志上也能看出当MaxGCPauseMillis设置的越小,单次mixed GC回收的Old 区内存越少,mixed GC发生的频率越高

- 一次并发标记后会可能会发生多次mixed GC

- young区垃圾收集(**(G1 Evacuation Pause) (young)**)分为以下几个阶段

  - Ext Root Scanning 根对象扫描
  - Update RS 更新RSet
  - Scan RS 扫描RSet
  - Code Root Scanning 遍历对象图,标记存活对象
  - Object Copy 将存活对象复制到Survivor Region
  - Termination 结束收集

  young GC会触发STW

- 并发标记阶段

  - initial-mark 初始标记,从日志上能看出初始标记(initial-mark)复用了young GC 的STW,因为他们可以复用Root Scan的操作

    >GC pause (G1 Humongous Allocation) (young) (initial-mark)

  - GC concurrent-root-region-scan-start Root区域扫描

  - concurrent-mark 并发标记

  - remark 再次标记 ,这个阶段需要一次STW

  - cleanup 清除,这个阶段也会触发STW

- 混合回收阶段(**GC pause (G1 Evacuation Pause) (mixed)**)

  - 不仅回收了年轻代,还回收了一部分老年代

