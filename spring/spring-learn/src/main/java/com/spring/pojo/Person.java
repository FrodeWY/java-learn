package com.spring.pojo;

/**
 * @author wangyang
 * @ClassName Person
 * @Description TODO
 * @Date 2021/7/22 下午9:39
 * @Version 1.0
 */
public class Person {

  private Long id;

  private String name;

  private String address;

  private Integer age;

  public Person() {

  }

  public Person(String name, String address, Integer age) {
    this(null, name, address, age);
  }

  public Person(Long id, String name, String address, Integer age) {
    this.id = id;
    this.name = name;
    this.address = address;
    this.age = age;
  }

  public Long getId() {
    return id;
  }

  public void setId(Long id) {
    this.id = id;
  }

  public String getName() {
    return name;
  }

  public void setName(String name) {
    this.name = name;
  }

  public String getAddress() {
    return address;
  }

  public void setAddress(String address) {
    this.address = address;
  }

  public Integer getAge() {
    return age;
  }

  public void setAge(Integer age) {
    this.age = age;
  }

  @Override
  public String toString() {
    return "Person{" +
        "id=" + id +
        ", name='" + name + '\'' +
        ", address='" + address + '\'' +
        ", age=" + age +
        '}';
  }
}
