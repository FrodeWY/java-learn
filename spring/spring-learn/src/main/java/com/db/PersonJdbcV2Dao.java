package com.db;

import com.pojo.Person;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.function.Consumer;
import java.util.function.Function;

/**
 * @author wangyang
 * @ClassName PersonJdbcV2Dao
 * @Description 使用PreparedStatement和事务
 * @Date 2021/7/22 下午11:06
 * @Version 1.0
 */
public class PersonJdbcV2Dao extends PersonJdbcDao {


    @Override
    public Person getByPrimaryKey(Long id) throws Exception {
        Function<Connection, Person> function = (connection) -> {
            String sqlTemplate = "select * from person where id=?";
            try (PreparedStatement preparedStatement = getPreparedStatement(connection, sqlTemplate)) {
                preparedStatement.setLong(1, 1L);
                ResultSet resultSet = preparedStatement.executeQuery();
                List<Person> personList = getPeopleListByResultSet(resultSet);
                return personList.size() > 0 ? personList.get(0) : null;
            } catch (SQLException throwables) {
                throw new RuntimeException(throwables);
            }
        };
        return executeByTransaction(function);
    }


    @Override
    public void deleteByPrimaryKey(Long id) throws Exception {
        Consumer<Connection> consumer = (connection) -> {
            String sqlTemplate = "delete from person where id= ?";
            try (PreparedStatement preparedStatement = getPreparedStatement(connection, sqlTemplate)) {
                preparedStatement.setLong(1, id);
                preparedStatement.execute();
            } catch (SQLException throwables) {
                throw new RuntimeException(throwables);
            }
        };
        executeByTransaction(consumer);
    }

    @Override
    public void update(Person person) throws Exception {
        Consumer<Connection> consumer = (connection) -> {
            String sqlTemplate = "update person set name=?,age=?,address=? where id = ?";
            try (PreparedStatement preparedStatement = getPreparedStatement(connection, sqlTemplate)) {
                preparedStatement.setString(1, person.getName());
                preparedStatement.setInt(2, person.getAge());
                preparedStatement.setString(3, person.getAddress());
                preparedStatement.setLong(4, person.getId());
                preparedStatement.execute();
            } catch (SQLException throwables) {
                throw new RuntimeException(throwables);
            }
        };
        executeByTransaction(consumer);
    }

    @Override
    public void create(Person person) throws Exception {
        Consumer<Connection> consumer = (connection) -> {
            String sqlTemplate = "insert into person (name,age,address) value(?,?,?)";
            try (PreparedStatement preparedStatement = getPreparedStatement(connection, sqlTemplate)) {
                preparedStatement.setString(1, person.getName());
                preparedStatement.setInt(2, person.getAge());
                preparedStatement.setString(3, person.getAddress());
                preparedStatement.execute();
            } catch (SQLException throwables) {
                throw new RuntimeException(throwables);
            }

        };
        executeByTransaction(consumer);
    }

    /**
     * 批量新增
     */
    public void createBatch(List<Person> personList) throws Exception {
        Consumer<Connection> consumer = (connection) -> {
            String sqlTemplate = "insert into person (name,age,address) values(?,?,?)";
            try (PreparedStatement preparedStatement = getPreparedStatement(connection, sqlTemplate)) {
                for (Person person : personList) {
                    preparedStatement.setString(1, person.getName());
                    preparedStatement.setInt(2, person.getAge());
                    preparedStatement.setString(3, person.getAddress());
                    preparedStatement.addBatch();
                }
                preparedStatement.executeBatch();
            } catch (SQLException throwables) {
                throw new RuntimeException(throwables);
            }
            throw new RuntimeException("rollback test");
        };
        executeByTransaction(consumer);
    }

    private PreparedStatement getPreparedStatement(Connection connection, String sqlTemplate) throws SQLException {
        connection.setAutoCommit(false);
        return connection.prepareStatement(sqlTemplate);
    }


    private <T> T executeByTransaction(Function<Connection, T> function) throws Exception {
        Connection connection = getConnection();
        try {
            return function.apply(connection);
        } catch (Exception e) {
            connection.rollback();
            throw e;
        } finally {
            connection.commit();
            connection.close();
        }
    }

    private void executeByTransaction(Consumer<Connection> consumer) throws Exception {
        Connection connection = getConnection();
        try {
            consumer.accept(connection);
        } catch (Exception e) {
            connection.rollback();
            throw e;
        } finally {
            connection.commit();
            connection.close();
        }
    }


    public static void main(String[] args) throws Exception {
        PersonJdbcV2Dao personJdbcDao = new PersonJdbcV2Dao();
        Person person = personJdbcDao.getByPrimaryKey(1L);
        System.out.println("select person:" + person);
//
//        person.setAge(person.getAge() + 1);
//        person.setName("update name2");
//        personJdbcDao.update(person);

//        Person newPerson = new Person("李蛋", "河南新阳", 43);
//        personJdbcDao.create(newPerson);
//
//        personJdbcDao.deleteByPrimaryKey(1L);


        List<Person> personList = new ArrayList<>();
        for (int i = 0; i < 10; i++) {
            Person p = new Person("batch insert" + i, "test", i);
            personList.add(p);
        }

        personJdbcDao.createBatch(personList);
    }
}
