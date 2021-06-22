package jvm;

/**
 * @author wangyang
 * @ClassName Hello
 * @Description TODO
 * @Date 2021/6/22 下午10:47
 * @Version 1.0
 */
public class Hello {

  public static void main(String[] args) {
    double price=10.50d;
    int amount=5;
    byte a=5;
    short b= 10;

    if(a<b){
      int sum = a + b;
      System.out.println("a+b ="+sum);
    }
    for (int i = 0; i <a ; i++) {
      System.out.println("need to pay: " +price*amount);
    }
  }
}
