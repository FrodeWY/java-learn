# 四种单例模式对比

- 懒汉模式
   - 优点:
      - 达到了按需初始化的目的
   - 缺点:
      - 线程不安全,需要开发者自己保证线程安全
- 饿汉模式
  - 优点:
    - 没有线程安全问题,由jvm保证线程安全
  - 缺点: 
    - 不能做到按需加载
    - 存在反射攻击或者反序列化攻击

- 静态代码块模式
  - 优点:
    - 没有线程安全问题,由jvm保证线程安全
  - 缺点: 
    - 不能做到按需加载
    - 存在反射攻击或者反序列化攻击

- 单例枚举类模式
  - 优点:
    - 没有线程安全问题,由jvm保证线程安全
    - 提供了序列化机制,能防止多次实例化,即使是面对反序列化攻击或反射攻击
    - 写法非常简便
  - 缺点: 不能做到按需加载

- 静态内部类模式
  - 优点:
    - 没有线程安全问题,由jvm保证线程安全
    - 加载外部类时不会加载内部类，这样可以实现懒加载
  - 缺点: 
    - 存在反射攻击或者反序列化攻击



## 简单AOP实现

模仿Spring aop的实现思路,使用BeanPostProcessor创建bean的代理对象

- com.spring.aop.process.MyAspectBeanPostProcessor

  > 使用MyAspectBeanPostProcessor 找到和切点相匹配Bean,通过MyAspectProxyCreator并找到可以使用的Advice,将Advice转换成MyMethodInterceptor ,将可以使用MyMethodInterceptor 织入到目标对象生成代理对象proxy

- com.spring.aop.process.MyAspectProxyCreator

  > 在所有bean中,扫描声明了@MyAspect方法,将其转换成Advice

## maven/spring 的 profile 机制

- maven

  在pom中配置多套环境,根据打包时指定的环境进行打包

  ``` 
  <profiles>
      <profile>
          <!-- 本地开发环境 -->
          <id>dev</id>
          <properties>
              <profiles.active>dev</profiles.active>
          </properties>
          <activation>
              <activeByDefault>true</activeByDefault>
          </activation>
      </profile>
      <profile>
          <!-- 测试环境 -->
          <id>test</id>
          <properties>
              <profiles.active>test</profiles.active>
          </properties>
      </profile>
      <profile>
          <!-- 生产环境 -->
          <id>pro</id>
          <properties>
              <profiles.active>pro</profiles.active>
          </properties>
      </profile>
  </profiles>
  ```

  ```
  ## 开发环境打包
  mvn clean package -P dev
  
  ## 测试环境打包
  mvn clean package -P test
  
  ## 生产环境打包
  mvn clean package -P pro
  ```

  

- spring

  - @Profile 可以在类和方法上声明,
    - 根据环境变量**spring.profiles.active**选择生效的配置
    - 使用**spring.profiles.default**指定默认profile
    - 也可以使用**@ActiveProfiles**指定profile
    - 如果使用spring boot 可以通过**SpringApplicationBuilder#profiles**指定profile



##  Hibernate 与 MyBatis 的各方面异同点

- Mybatis 优点:

  - 原生SQL(XML 语法)，直观，可以很方便的查看到真正执行的sql

- Hibernate 优点:

  - 简单场景不用写 SQL(HQL、Cretiria、SQL)
  -  Hibernate是全自动ORM框架，hibernate完全可以通过对象关系模型实现对数据库的操作，拥有完整的JavaBean对象与数据库的映射结构来自动生成sql。
  - 移植性强, hibernate通过它强大的映射结构和hql语言，大大降低了对象与数据库（oracle、mysql等）的耦合性

- Mybatis 缺点:

  - 繁琐，可以用 MyBatis-generator、MyBatis-Plus 之类的插件
  - Mybatis是半自动ORM框架。mybatis仅有基本的字段映射，对象数据以及对象实际关系仍然需要通过手写配置来实现和管理。
  - sql移植性差, mybatis由于需要手写sql，因此与数据库的耦合性直接取决于程序员写sql的方法，如果sql不具通用性而用了很多某数据库特性的sql语句的话，移植性也会随之降低很多，成本很高。

- Hibernate 缺点:

  - 对DBA 不友好,不能直观地看到真正执行的sql,不利于sql审核

  
  