## 常用的并发操作 API 和工具类

### 线程安全关键字

### synchronized

- 原子性
  - synchronized采用**互斥同步**的方式，让多个线程并发访问共享数据时，保证临界区在同一时刻只被一个线程访问。
- 可见性
  - 线程加锁时，将清空工作内存的共享变量的值，从而使用共享变量时需要从主内存中重新读取最新的值
  - 线程解锁前，必须将共享变量的最新值刷新到主内存中
- 使用范围
  - 修饰代码块：大括号括起来的代码，锁是Synchonized括号里配置的对象
  - 修饰普通方法：整个方法，锁是当前实例对象。
  - 修饰静态方法：整个静态方法，锁是当前类的Class对象。
- 加锁释放锁

### volatile

- 可见性
  - 底层实现主要是通过汇编lock前缀命令
  - Lock前缀指令会引起处理器缓存回写到内存
  - 一个处理器的缓存回写到内存会导致其他处理器的缓存无效。
- 有序性
  - jvm层面基于JMM内存屏障插入策略对volatile修饰的变量前后加内存屏障,来保证有序性
- 不能保证原子性
- 使用volatile需要满足两个条件
  - 对变量的写操作不依赖当前值
  - 该变量没有包含在具有其他变量的不变式中。





### Lock

#### ReadWriteLock 

- ReentrantLock是**可重入锁**，顾名思义，就是支持重进入的锁，它表示该锁**能够支持一个线程对资源的重复加锁**。

- 支持公平锁和非公平锁

  - **公平锁**是指多个线程**按照申请锁的顺序来获取锁**，线程直接进入队列中排队，队列中的第一个线程才能获得锁（不可插队，等待时间越长，请求锁时会被优先满足）。**公平锁的优点是等待锁的线程不会饥饿**。**缺点是整体吞吐量相对非公平锁要低**
  - **非公平锁**是多个线程**加锁时直接尝试获取锁**，获取不到才会到等待队列的队尾等待（可插队的）。但如果此时锁刚好可用，那么这个线程可以无需阻塞直接获取到锁，所以非公平锁有可能出现后申请锁的线程先获取锁的场景。非公平锁的优点是**可以减少唤起线程的开销，整体的吞吐量高**，因为线程有几率不阻塞直接获得锁，CPU不必唤醒所有线程。**缺点是处于等待队列中的线程可能会饥饿，或者等很久才会获得锁。**

- ReentrantLock与synchronized的区别

  - 可重入性：两者的锁都是可重入的，差别不大，有线程进入锁，计数器自增1，等下降为0时才可以释放锁
  - 锁的实现：synchronized是基于JVM实现的（用户很难见到，无法了解其实现），ReentrantLock是JDK实现的。
  - 性能区别：在最初的时候，二者的性能差别差很多，当synchronized引入了偏向锁、轻量级锁（自选锁）后，二者的性能差别不大，官方推荐synchronized（写法更容易、在优化时其实是借用了ReentrantLock的CAS技术，试图在用户态就把问题解决，避免进入内核态造成线程阻塞）
  - 功能区别
    - 便利性：synchronized更便利，它是由编译器保证加锁与释放。ReentrantLock是需要手动释放锁，所以为了避免忘记手工释放锁造成死锁，所以最好在finally中声明释放锁。 
    - 锁的细粒度和灵活度，ReentrantLock优于synchronized
    - ReentrantLock可以指定是公平锁还是非公平锁，sync只能是非公平锁。
    - ReentrantLock提供了一个Condition类，可以分组唤醒需要唤醒的线程。不像是synchronized要么随机唤醒一个线程，要么全部唤醒。
    - ReentrantLock可以被中断,synchronized不可以被中断

- 常用API

  | **重要方法**                                         | **说明**                                   |
  | ---------------------------------------------------- | ------------------------------------------ |
  | void lock()                                          | 获取锁; 类比 synchronized (lock)           |
  | void lockInterruptibly() throws InterruptedException | 获取锁; 允许打断;                          |
  | boolean tryLock(long time, TimeUnit unit)            | 尝试获取锁; 成功则返回 true; 超时则退出    |
  | boolean tryLock()                                    | boolean tryLock()                          |
  | void unlock()                                        | 解锁；要求当前线程已获得锁; 类比同步块结束 |
  | Condition newCondition()                             | 新增一个绑定到当前Lock的条件               |

  

#### ReentrantReadWriteLock

- ReentrantReadWriteLock管理一组锁，一个读锁，一个写锁。

- ReentrantReadWriteLock 读锁与读锁之间不互斥,读锁与写锁互斥,写锁与写锁互斥。在实际应用中，大部分情况下对共享数据（如缓存）的访问都是读操作远多于写操作，这时ReentrantReadWriteLock能够提供比排他锁更好的并发性和吞吐量。**适用于读多写少的并发情况**。

- 常用API

  | **重要方法**                                 | **说明**                            |
  | -------------------------------------------- | ----------------------------------- |
  | ReentrantReadWriteLock.ReadLock readLock()   | 返回读锁,读锁api与ReentrantLock类似 |
  | ReentrantReadWriteLock.WriteLock writeLock() | 返回写锁,写锁api与ReentrantLock类似 |



### Atomic原子类

- 核心实现原理
  - 使用 volatile 保证value可见性
  - 使用 CAS 指令，作为乐观锁实现，通过自旋重试保证写入。
- 常用实现
  - AtomicBoolean 
  - AtomicLong
  - AtomicInteger
  - AtomicStampedReference 
  - LongAdder
- 与synchronized的对比
  - 在竞争不是特别激烈的场景下重试几次就能够很快修改成功,synchronized加锁和释放锁需要用户态和内核态切换,所以CAS性能优于synchronized
  - 在竞争特别激烈时,Atomic原子类可能一直自旋,消耗大量cpu资源
  - Atomic原子类只能保证一个变量的原子操作



### AQS组件

#### Semaphore:

- 用于保证同一时间并发访问线程的数目。

- 使用场景：仅能提供有限访问的资源。比如数据库连接。


- 常用API

  | **重要方法**                             | **说明**                         |
  | ---------------------------------------- | -------------------------------- |
  | void acquire()                           | 获取一个信号量,可以被中断        |
  | void acquireUninterruptibly()            | 获取一个信号量,不可以被中断      |
  | void release()                           | 释放一个信号                     |
  | void acquire(int permits)                | 获取permits个信号,可以被中断     |
  | void acquireUninterruptibly(int permits) | 获取permits个信号量,不可以被中断 |
  | void release(int permits)                | 释放permits个信号                |

  

#### CountDownLatch 

- 阻塞主线程，N 个子线程满足条件时主线程继续

- 适用场景: Master 线程等待 Worker 线程把任务执行完

- 常用API

  | **重要方法**                               | **说明**                                                 |
  | ------------------------------------------ | -------------------------------------------------------- |
  | void await()                               | 等待计数器归零                                           |
  | boolean await(long timeout, TimeUnit unit) | 限时等待                                                 |
  | void countDown()                           | 计数器减1                                                |
  | long getCount()                            | 获取当前 CountDownLatch 维护的值，也就是AQS的state的值。 |



#### CyclicBarrier

- 适用场景:任务执行到一定阶段, 等待其他任务对齐，阻塞 N 个线程时所有线程被唤醒继续。

- 常用API

  | 重要方法                           | 说明                                                         |
  | ---------------------------------- | ------------------------------------------------------------ |
  | await()                            | 在所有线程都已经在此 barrier上并调用 await 方法之前，将一直等待。 |
  | await(long timeout, TimeUnit unit) | 所有线程都已经在此屏障上调用 await 方法之前将一直等待，或者超出了指定的等待时间。 |
  | reset()                            | 将屏障重置为其初始状态。                                     |
  | isBroken()                         | 查询此屏障是否处于损坏状态。                                 |

  

#### CyclicBarrier和CountDownLatch的区别

| **CountDownLatch**                     | **CyclicBarrier**                          |
| -------------------------------------- | ------------------------------------------ |
| 在主线程里 await 阻塞并做聚合          | 直接在各个子线程里 await 阻塞，回调聚合    |
| N 个线程执行了countdown，主线程继续    | N个线程执行了 await 时，N 个线程继续       |
| 主线程里拿到同步点                     | 回调被最后到达同步点的线程执行             |
| 基于 AQS 实现，state 为 count，递减到0 | 基于可重入锁 condition.await/signalAll实现 |
| 不可以复用                             | 计数为0时重置为 N，可以复用                |



