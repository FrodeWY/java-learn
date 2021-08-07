package seventh.com.util;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.Future;
import java.util.concurrent.FutureTask;
import java.util.concurrent.atomic.AtomicLong;
import java.util.concurrent.atomic.AtomicReference;
import java.util.concurrent.locks.ReentrantLock;
import java.util.stream.Collectors;
import lombok.extern.slf4j.Slf4j;

/**
 * @author wangyang
 * @ClassName DistributionIdGenerator
 * @Description 分布式id生成器
 * @Date 2021/8/5 下午3:05
 * @Version 1.0
 */
@Slf4j
public class DistributionIdGenerator {

  /**
   * 自增计数器
   */
  private static final AtomicLong SEQUENCE_GENERATOR = new AtomicLong(1);

  /**
   * 同一个时间戳能够申请的最大上限
   */
  private static final Integer UPPER_LIMIT = 100000;
  /**
   * 记录上一次申请的时间
   */
  private static final AtomicReference<Long> LAST_TIME_REFERENCE = new AtomicReference<>(System.currentTimeMillis());
  /**
   * 步长
   */
  private static final Integer STEP = 10000;

  private static final String FORMAT = "%06d";

  private static final ReentrantLock LOCK = new ReentrantLock();

  /**
   * 生成一个分布式id
   */
  public static Long generateSingleId() {
    long currentTimeMillis = System.currentTimeMillis();
    Long lastTime = LAST_TIME_REFERENCE.get();
    if (currentTimeMillis == lastTime) {
      long sequence;
      try {
        LOCK.lock();
        if (currentTimeMillis == LAST_TIME_REFERENCE.get()) {
          sequence = SEQUENCE_GENERATOR.getAndIncrement();
        } else {
          return generateSingleId();
        }
      } finally {
        LOCK.unlock();
      }
      if (sequence > UPPER_LIMIT) {
        log.warn("Description The current concurrency exceeds the maximum limit");
        Thread.yield();
        return generateSingleId();
      }
      return Long.valueOf(currentTimeMillis + "" + String.format(FORMAT, sequence));
    } else if (currentTimeMillis > lastTime) {
      refreshSequence(currentTimeMillis);
    }
    return generateSingleId();
  }

  /**
   * 生成一段分布式id
   */
  public static List<Long> generateBatch() {
    long currentTimeMillis = System.currentTimeMillis();
    Long lastTime = LAST_TIME_REFERENCE.get();
    if (currentTimeMillis == lastTime) {
      long sequenceStart;
      try {
        LOCK.lock();
        if (currentTimeMillis == LAST_TIME_REFERENCE.get()) {
          sequenceStart = SEQUENCE_GENERATOR.getAndAdd(STEP);
        } else {
          return generateBatch();
        }
      } finally {
        LOCK.unlock();
      }
      if (sequenceStart >= UPPER_LIMIT - STEP) {
        log.warn("Description The current concurrency exceeds the maximum limit");
        Thread.yield();
        return generateBatch();
      }
      return getLongList(sequenceStart, currentTimeMillis);
    } else if (currentTimeMillis > lastTime) {
      refreshSequence(currentTimeMillis);
    }
    return generateBatch();
  }

  /**
   * 如果当前时间大于最后操作时间,则刷新序列数生成器
   *
   * @param currentTimeMillis 当前时间
   */
  private static void refreshSequence(long currentTimeMillis) {
    try {
      LOCK.lock();
      Long lastTime = LAST_TIME_REFERENCE.get();
      if (currentTimeMillis > lastTime) {
        if (LAST_TIME_REFERENCE.compareAndSet(lastTime, currentTimeMillis)) {
          SEQUENCE_GENERATOR.set(1);
        }
      }
    } finally {
      LOCK.unlock();
    }
  }

  /**
   * 根据申请到的起始序列号生成一段分布式id集合
   *
   * @param sequenceStart 起始序列号
   * @param currentTimeMillis 当前时间
   * @return 分布式id集合
   */
  private static List<Long> getLongList(Long sequenceStart, long currentTimeMillis) {
    ArrayList<Long> list = new ArrayList<>(STEP);
    for (long i = sequenceStart; i <= STEP; i++) {
      list.add(Long.valueOf(currentTimeMillis + "" + String.format(FORMAT, i)));
    }
    return list;
  }


  public static void main(String[] args) throws Exception {
    List<Long> idAllList = new ArrayList<>();
    List<Future<List<Long>>> taskList = new ArrayList<>(10);
    List<Thread> threadList = new ArrayList<>(10);
    for (int i = 0; i < 10; i++) {
      FutureTask<List<Long>> task;
      if (i < 5) {
        task = new FutureTask<>(() -> {
          List<Long> idList = new ArrayList<>();
          while (!Thread.interrupted()) {
            List<Long> distributionIdList = DistributionIdGenerator.generateBatch();
            idList.addAll(distributionIdList);
          }
          return idList;
        });
      } else {
        task = new FutureTask<>(() -> {
          List<Long> idList = new ArrayList<>();
          while (!Thread.interrupted()) {
            Long id = DistributionIdGenerator.generateSingleId();
            idList.add(id);
          }
          return idList;
        });
      }

      Thread t = new Thread(task);
      taskList.add(task);
      threadList.add(t);
      t.start();
    }
    Thread.sleep(1000);
    threadList.forEach(Thread::interrupt);
    for (Future<List<Long>> future : taskList) {
      List<Long> idList = future.get();
      idAllList.addAll(idList);
    }
    while (threadList.stream().anyMatch(Thread::isAlive)) {
    }
    System.out.println("id list size:" + idAllList.size());
    List<Long> distinct = idAllList.stream().distinct().collect(Collectors.toList());
    System.out.println("id distinct size:" + distinct.size());


  }
}
