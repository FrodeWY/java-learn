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
    int a = 5;
    double b = 10.5D;
    short c = 3;
    byte d = 5;
    double sum = 0D;
    for (byte i = 0; i < d; i++) {
      if (i < 3) {
        sum = sum + mult(a, b);
      } else {
        sum = sum + add(a, c);
      }
    }
    System.out.println("sum:" + sum);
  }


  public static Double mult(double a, double b) {
    return a * b;
  }

  public static Integer add(int a, int b) {
    return a + b;
  }


}