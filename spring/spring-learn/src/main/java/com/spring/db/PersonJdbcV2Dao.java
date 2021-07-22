package com.spring.db;

import com.spring.pojo.Person;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;

/**
 * @author wangyang
 * @ClassName PersonJdbcV2Dao
 * @Description TODO
 * @Date 2021/7/22 下午11:06
 * @Version 1.0
 */
public class PersonJdbcV2Dao extends PersonJdbcDao {


  @Override
  public Person getByPrimaryKey(Long id) throws SQLException {
    Connection connection = getConnection();
    try {
      connection.setAutoCommit(false);
      String sqlTemplate = "select * from person where id=?";
      PreparedStatement preparedStatement = connection.prepareStatement(sqlTemplate);
      preparedStatement.setLong(1, 1L);
      ResultSet resultSet = preparedStatement.executeQuery();
      List<Person> personList = getPeopleListByResultSet(resultSet);
      return personList.size() > 0 ? personList.get(0) : null;
    } catch (SQLException e) {
      connection.rollback();
      throw e;
    } finally {
      connection.close();
    }
  }


}
