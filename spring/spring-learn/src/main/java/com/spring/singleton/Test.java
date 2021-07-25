package com.spring.singleton;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;

/**
 * @author wangyang
 * @ClassName Test
 * @Description TODO
 * @Date 2021/7/25 上午11:29
 * @Version 1.0
 */
public class Test {

  public static void main(String[] args) throws Exception {
    ImmediatelySingleton immediatelySingleton = ImmediatelySingleton.getInstance();
    ImmediatelySingleton immediatelySingleton2 = ImmediatelySingleton.getInstance();
    System.out.println(immediatelySingleton == immediatelySingleton2);

    EnumSingleton enumSingleton = EnumSingleton.getInstance();

//    Class<EnumSingleton> enumSingletonClass = EnumSingleton.class;
//    Constructor<EnumSingleton> constructor = enumSingletonClass.getDeclaredConstructor();
//    constructor.setAccessible(true);
//    EnumSingleton enumSingleton2 = constructor.newInstance();

    ObjectOutputStream oos = new ObjectOutputStream(new FileOutputStream("singleton"));
    oos.writeObject(enumSingleton);
    oos.close();

    //将文件反序列化到对象
    File file = new File("singleton");
    ObjectInputStream ois = new ObjectInputStream(new FileInputStream(file));
    EnumSingleton enumSingleton2 = (EnumSingleton) ois.readObject();
    System.out.println(enumSingleton == enumSingleton2);
    EnumSingleton enumSingleton3 = EnumSingleton.getInstance();

    System.out.println(enumSingleton == enumSingleton3);
    StaticSingleton staticSingleton = StaticSingleton.getInstance();

    StaticInnerClassSingleton staticInnerClassSingleton = StaticInnerClassSingleton.getInstance();
    StaticInnerClassSingleton staticInnerClassSingleton2 = StaticInnerClassSingleton.getInstance();
    System.out.println(staticInnerClassSingleton == staticInnerClassSingleton2);
    Constructor<StaticInnerClassSingleton> declaredConstructor = StaticInnerClassSingleton.class.getDeclaredConstructor();
    declaredConstructor.setAccessible(true);
    declaredConstructor.newInstance();
  }
}
