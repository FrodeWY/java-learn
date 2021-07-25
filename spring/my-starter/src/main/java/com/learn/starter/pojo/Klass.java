package com.learn.starter.pojo;

import org.springframework.stereotype.Component;

import java.util.List;

@Component//注解方式 装配Bean
public class Klass {
   private final List<Student> students;

   public Klass(List<Student> students) {
      this.students = students;
   }

   public List<Student> getStudents() {
      return students;
   }
}
