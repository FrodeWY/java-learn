package com.jdbc;

import com.pojo.Person;
import com.zaxxer.hikari.HikariConfig;
import com.zaxxer.hikari.HikariDataSource;

import java.sql.Connection;
import java.sql.SQLException;

/**
 * 使用Hikari连接池
 */
public class PersonJdbcV3Dao extends PersonJdbcV2Dao {

    public static final int MS_TO_SECOND = 1000;
    private static HikariDataSource HIKARI_DATASOURCE;

    static {
        HikariConfig hikariConfig = new HikariConfig();
        hikariConfig.setConnectionTimeout(10 * MS_TO_SECOND);
        hikariConfig.setIdleTimeout(60 * MS_TO_SECOND);
        hikariConfig.setMaxLifetime(65 * MS_TO_SECOND);
        hikariConfig.setMaximumPoolSize(50);
        hikariConfig.setMinimumIdle(20);
        hikariConfig.setPassword("123456");
        hikariConfig.setUsername("root");
        hikariConfig.setJdbcUrl(URL);
        hikariConfig.setAutoCommit(false);
        hikariConfig.setPoolName("my-connection-pool");
        HIKARI_DATASOURCE = new HikariDataSource(hikariConfig);
    }

    @Override
    protected Connection getConnection() throws  SQLException {
        return HIKARI_DATASOURCE.getConnection();
    }

    public static void main(String[] args) throws Exception {
        PersonJdbcV3Dao personJdbcDao = new PersonJdbcV3Dao();
        Person person = personJdbcDao.getByPrimaryKey(1L);
        System.out.println("select person:" + person);
//
        person.setAge(person.getAge() + 1);
        person.setName("update name2");
        personJdbcDao.update(person);

        Person newPerson = new Person("李蛋", "河南新阳", 43);
        personJdbcDao.create(newPerson);
//
//        personJdbcDao.deleteByPrimaryKey(1L);


//        List<Person> personList = new ArrayList<>();
//        for (int i = 0; i < 10; i++) {
//            Person p = new Person("batch insert" + i, "test", i);
//            personList.add(p);
//        }
//
//        personJdbcDao.createBatch(personList);
    }
}
