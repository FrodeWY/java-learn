package fourth_week.com.concurrent;

import java.util.concurrent.ArrayBlockingQueue;
import java.util.concurrent.BrokenBarrierException;
import java.util.concurrent.Callable;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.CyclicBarrier;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.Future;
import java.util.concurrent.FutureTask;
import java.util.concurrent.Semaphore;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicBoolean;

/**
 * 本周作业：（必做）思考有多少种方式，在main函数启动一个新线程或线程池， 异步运行一个方法，拿到这个方法的返回值后，退出主线程？ 写出你的方法，越多越好，提交到github。
 *
 * 一个简单的代码参考：
 */
public class Homework03 {

  private static volatile Boolean flag = false;
  private static ThreadPoolExecutor threadPoolExecutor = new ThreadPoolExecutor(1, Runtime.getRuntime().availableProcessors(), 60, TimeUnit.SECONDS,
      new ArrayBlockingQueue<>(100));

  public static void main(String[] args) throws Exception {

    long start = System.currentTimeMillis();

    //1
//        Callable<Integer> callable = Homework03::sum;
//        FutureTask<Integer> futureTask = new FutureTask<>(callable);
//        new Thread(futureTask).start();
//        Integer result = futureTask.get();

    //2
//        Callable<Integer> callable = Homework03::sum;
//        FutureTask<Integer> futureTask = new FutureTask<>(callable);
//        threadPoolExecutor.execute(futureTask);
//        Integer result = futureTask.get();

    //3
//        Future<Integer> futureTask = threadPoolExecutor.submit(Homework03::sum);
//        Integer result = futureTask.get();
    //4
//    ResultHolder resultHolder = new ResultHolder();
//    Thread thread = new Thread(() -> resultHolder.setResult(sum()));
//    thread.start();
//    thread.join();
//    Integer result = resultHolder.getResult();

    //5
//    int a1 = Thread.activeCount();
//    ResultHolder resultHolder = new ResultHolder();
//    Thread thread = new Thread(() -> resultHolder.setResult(sum()));
//    thread.start();
//    while (Thread.activeCount() >a1){
//      Thread.yield();
//    }
//    Integer result = resultHolder.getResult();

    //6
//    CompletableFuture<Integer> completableFuture= CompletableFuture.supplyAsync(Homework03::sum);
//    Integer result =  completableFuture.get();

    //7
//    ResultHolder resultHolder = new ResultHolder();
//    CountDownLatch countDownLatch = new CountDownLatch(1);
//    new Thread(() -> {
//      resultHolder.setResult(sum());
//      countDownLatch.countDown();
//    }).start();
//    countDownLatch.await();
//    Integer result = resultHolder.getResult();

    //8
//    CyclicBarrier cyclicBarrier=new CyclicBarrier(2);
//    ResultHolder resultHolder = new ResultHolder();
//    new Thread(() -> {
//      resultHolder.setResult(sum());
//      try {
//        cyclicBarrier.await();
//      } catch (Exception e) {
//        e.printStackTrace();
//      }
//    }).start();
//    cyclicBarrier.await();
//    Integer result = resultHolder.getResult();

    //9
    ResultHolder resultHolder = new ResultHolder();
    new Thread(() -> {
      resultHolder.setResult(sum());
      flag = Boolean.TRUE;
    }).start();
    while (!flag) {
      Thread.yield();
    }
    Integer result = resultHolder.getResult();

    System.out.println("异步计算结果为：" + result);

    System.out.println("使用时间：" + (System.currentTimeMillis() - start) + " ms");
    threadPoolExecutor.shutdown();

  }

  private static int sum() {
    return fibo(36);
  }

  private static int fibo(int a) {
    if (a < 2) {
      return 1;
    }
    return fibo(a - 1) + fibo(a - 2);
  }

  private static class ResultHolder {

    private Integer result;


    public Integer getResult() {
      return result;
    }

    public void setResult(Integer result) {
      this.result = result;
    }
  }
}
