package jvm;

import java.io.InputStream;
import java.lang.reflect.Method;

public class XlassClassLoader extends ClassLoader {

  public static void main(String[] args) {
    try {
      final String xlassName = "Hello";
      final String methodName = "hello";
      final Object o = new XlassClassLoader().findClass(xlassName).newInstance();
      final Method[] declaredMethods = o.getClass().getDeclaredMethods();
      for (Method declaredMethod : declaredMethods) {
        if (methodName.equals(declaredMethod.getName())) {
          declaredMethod.invoke(o);
        }
      }
    } catch (Exception e) {
      e.printStackTrace();
    }

  }

  @Override
  protected Class<?> findClass(String name) throws ClassNotFoundException {
    //实际类文件名
    String filePath = name.replace(".", "/");
    String classFileName = filePath + ".xlass";
    final InputStream inputStream = this.getClass().getClassLoader()
        .getResourceAsStream(classFileName);
    final byte[] bytes = getClassByte(inputStream);

    return defineClass(name, bytes, 0, bytes.length);
  }


  public static byte[] getClassByte(InputStream inputStream) throws ClassNotFoundException {
    if (inputStream == null) {
      throw new IllegalArgumentException("inputStream is null");
    }
    try {
      final int available = inputStream.available();
      byte[] bytes = new byte[available];
      if (inputStream.read(bytes) > 0) {
        return getRealBytes(bytes);
      }
      throw new IllegalArgumentException("class stream is empty");
    } catch (Exception e) {
      throw new ClassNotFoundException("class not found");
    }
  }

  public static byte[] getRealBytes(byte[] bytes) {
    if (bytes == null || bytes.length == 0) {
      throw new IllegalArgumentException("class stream is empty");
    }
    byte[] realBytes = new byte[bytes.length];
    for (int i = 0; i < realBytes.length; i++) {
      realBytes[i] = (byte) (255 - bytes[i]);
    }
    return realBytes;
  }
}