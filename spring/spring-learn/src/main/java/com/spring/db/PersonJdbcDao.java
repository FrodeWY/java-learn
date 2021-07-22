package com.spring.db;

import com.spring.pojo.Person;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;

/**
 * @author wangyang
 * @ClassName JDBC
 * @Description TODO
 * @Date 2021/7/22 下午9:32
 * @Version 1.0
 */
public class PersonJdbcDao implements BaseDao<Person> {

  public static final String URL = "jdbc:mysql://localhost:3306/geek?characterEncoding=utf-8&useSSL=false";
  public static final String USER = "root";
  public static final String PASSWORD = "123456";

  public void deleteByPrimaryKey(Long id) {
    String sqlTemplate = "delete from person where id= %d";
    String executeSql = String.format(sqlTemplate, id);
    execute(executeSql);
  }


  public void create(Person person) {
    String sqlTemplate = "insert into person (name,age,address) value('%s',%d,'%s')";
    String executeSql = String.format(sqlTemplate, person.getName(), person.getAge(), person.getAddress());
    execute(executeSql);
  }

  public Person getByPrimaryKey(Long id) throws SQLException {
    String sqlTemplate = "select * from person where id=%d";
    String executeSql = String.format(sqlTemplate, id);
    List<Person> personList = executeQuery(executeSql);
    return personList.size() > 0 ? personList.get(0) : null;
  }

  public void update(Person person) {
    String sqlTemplate = "update person set name='%s',age=%d,address='%s' where id = %d";
    String executeSql = String.format(sqlTemplate, person.getName(), person.getAge(), person.getAddress(), person.getId());
    execute(executeSql);
  }

  private List<Person> executeQuery(String executeSql) {
    Statement statement = getStatement();
    try {
      ResultSet resultSet = statement.executeQuery(executeSql);
      return getPeopleListByResultSet(resultSet);
    } catch (Exception e) {
      throw new RuntimeException(e);
    }
  }

  protected List<Person> getPeopleListByResultSet(ResultSet resultSet) throws SQLException {
    List<Person> personList = new ArrayList<Person>();
    while (resultSet.next()) {
      long id = resultSet.getLong("id");
      String name = resultSet.getString("name");
      String address = resultSet.getString("address");
      int age = resultSet.getInt("age");
      Person person = new Person(id, name, address, age);
      personList.add(person);
    }
    return personList;
  }

  private void execute(String executeSql) {
    Statement statement = getStatement();
    try {
      statement.execute(executeSql);
    } catch (Exception e) {
      throw new RuntimeException(e);
    }
  }

  protected Connection getConnection() {
    try {
      Class.forName("com.mysql.jdbc.Driver");
      return DriverManager.getConnection(URL, USER, PASSWORD);
    } catch (Exception e) {
      throw new RuntimeException(e);
    }
  }

  protected Statement getStatement() {
    try {
      Connection connection = getConnection();
      return connection.createStatement();
    } catch (Exception e) {
      throw new RuntimeException(e);
    }
  }

  public static void main(String[] args) throws SQLException {
    PersonJdbcDao personJdbcDao = new PersonJdbcDao();
    Person person = personJdbcDao.getByPrimaryKey(1L);
    System.out.println("select person:" + person);

    person.setAge(person.getAge() + 1);
    person.setName("update name");
    personJdbcDao.update(person);

    Person newPerson = new Person("李强", "河南郑州", 32);
    personJdbcDao.create(newPerson);

    personJdbcDao.deleteByPrimaryKey(2L);

  }
}
