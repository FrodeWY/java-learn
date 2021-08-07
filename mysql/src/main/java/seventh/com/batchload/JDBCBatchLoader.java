package seventh.com.batchload;

import java.awt.Component;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;
import seventh.com.pojo.Order;
import seventh.com.util.DistributionIdGenerator;

/**
 * @author wangyang
 * @ClassName JDBCBatchLoader
 * @Description TODO
 * @Date 2021/8/5 下午2:24
 * @Version 1.0
 */
public class JDBCBatchLoader {

  private final Random RANDOM = new Random(1000);
  private static final String URL = "jdbc:mysql://localhost:3316/order?characterEncoding=utf-8&useSSL=false";
  private static final String USER = "root";
  private static final String PASSWORD = "123456";

  public void insertOrderDataBatch(int batchSize, int times) throws SQLException {
    Connection connection = getConnection();
    String sqlTemplate = "insert into `order`(id, order_id, buyer_member_id, parent_order_id, order_amount, pay_amount, discount_amount, tax_amount,"
        + " score_deduction, score, growth_value, status, receive_address, receiver_name, receiver_phone, create_time, update_time, enabled, currency) "
        + "values (?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?)";
    long elapsedTime = 0;
    try (PreparedStatement preparedStatement = getPreparedStatement(connection, sqlTemplate)) {
      List<Order> orderList;
      for (int i = 0; i < times; i++) {
        orderList = mockOrderData(batchSize);
        elapsedTime += executeBatch(preparedStatement, orderList);
      }
    } catch (Exception e) {
      connection.rollback();
      throw e;
    } finally {
      connection.commit();
      connection.close();
    }
    System.out.println("总耗时:" + elapsedTime);
  }

  public void insertOrderDataBatch2(int batchSize, int times) throws SQLException {
    Connection connection = getConnection();
    String sqlTemplate = "insert into `order`(id, order_id, buyer_member_id, parent_order_id, order_amount, pay_amount, discount_amount, tax_amount,"
        + " score_deduction, score, growth_value, status, receive_address, receiver_name, receiver_phone, create_time, update_time, enabled, currency) "
        + "values ";
    long elapsedTime = 0;
    try (Statement statement = getStatement(connection)) {
      List<Order> orderList;
      String format = "(%d,'%s',%d,'%s','%s','%s','%s','%s','%s',%d,%d,%d,'%s','%s',%d,%d,%d,%d,'%s'),";
      for (int i = 0; i < times; i++) {
        StringBuilder valuesBuilder = new StringBuilder();
        orderList = mockOrderData(batchSize);
        for (Order order : orderList) {
          valuesBuilder = valuesBuilder.append(String
              .format(format, order.getId(), order.getOrderId(), order.getBuyerMemberId(), order.getParentOrderId(), order.getOrderAmount(),
                  order.getPayAmount(), order.getDiscountAmount()
                  , order.getTaxAmount(), order.getScoreDeduction(), order.getScore(), order.getGrowthValue(), order.getStatus(),
                  order.getReceiveAddress(), order.getReceiverName(), order.getReceiverPhone(),
                  order.getCreateTime(), order.getUpdateTime(), order.getEnabled(), order.getCurrency()));
        }
        String substring = valuesBuilder.substring(0, valuesBuilder.length() - 1);
//        System.out.println();
        long startTime = System.currentTimeMillis();
        String sql = sqlTemplate + substring;
        statement.execute(sql);
        elapsedTime += System.currentTimeMillis() - startTime;
      }
    } catch (Exception e) {
      connection.rollback();
      e.printStackTrace();
    } finally {
      connection.commit();
      connection.close();
    }
    System.out.println("总耗时:" + elapsedTime);
  }

  /**
   * 批量执行
   *
   * @return 执行时间
   */
  private Long executeBatch(PreparedStatement preparedStatement, List<Order> orderList) throws SQLException {
    long start = System.currentTimeMillis();
    for (Order order : orderList) {
      preparedStatement.setLong(1, order.getId());
      preparedStatement.setString(2, order.getOrderId());
      preparedStatement.setLong(3, order.getBuyerMemberId());
      preparedStatement.setString(4, order.getParentOrderId());
      preparedStatement.setString(5, order.getOrderAmount());
      preparedStatement.setString(6, order.getPayAmount());
      preparedStatement.setString(7, order.getDiscountAmount());
      preparedStatement.setString(8, order.getTaxAmount());
      preparedStatement.setString(9, order.getScoreDeduction());
      preparedStatement.setInt(10, order.getScore());
      preparedStatement.setInt(11, order.getGrowthValue());
      preparedStatement.setInt(12, order.getStatus());
      preparedStatement.setString(13, order.getReceiveAddress());
      preparedStatement.setString(14, order.getReceiverName());
      preparedStatement.setLong(15, order.getReceiverPhone());
      preparedStatement.setLong(16, order.getCreateTime());
      preparedStatement.setLong(17, order.getUpdateTime());
      preparedStatement.setInt(18, order.getEnabled());
      preparedStatement.setString(19, order.getCurrency());
      preparedStatement.addBatch();
    }
    preparedStatement.executeBatch();
    return System.currentTimeMillis() - start;
  }

  private Connection getConnection() throws SQLException {
    return DriverManager.getConnection(URL, USER, PASSWORD);
  }

  private PreparedStatement getPreparedStatement(Connection connection, String sqlTemplate) throws SQLException {
    connection.setAutoCommit(false);
    return connection.prepareStatement(sqlTemplate);
  }

  protected Statement getStatement(Connection connection) throws Exception {
    connection.setAutoCommit(false);
    return connection.createStatement();
  }

  public List<Order> mockOrderData(int total) {
    List<Order> orderList = new ArrayList<>(total);
    for (int i = 0; i < total; i++) {
      Order order = new Order();
      order.setId(DistributionIdGenerator.generateSingleId());
      order.setOrderId("order" + DistributionIdGenerator.generateSingleId());
      order.setBuyerMemberId(DistributionIdGenerator.generateSingleId());
      int random = this.RANDOM.nextInt();
      order.setOrderAmount(String.valueOf(100 + random));
      order.setPayAmount("100");
      order.setDiscountAmount("10");
      order.setTaxAmount("7");
      order.setScoreDeduction("50");
      order.setScore(100 + random);
      order.setStatus(0);
      order.setGrowthValue(random);
      order.setReceiveAddress("南京" + random);
      order.setReceiverName("莉莉" + random);
      order.setParentOrderId("parent" + DistributionIdGenerator.generateSingleId());
      order.setReceiverPhone(12121312L);
      long timeMillis = System.currentTimeMillis();
      order.setCreateTime(timeMillis);
      order.setUpdateTime(timeMillis);
      order.setEnabled(1);
      order.setCurrency("CNY");
      orderList.add(order);
    }
    return orderList;
  }

  public static void main(String[] args) throws SQLException {
    JDBCBatchLoader jdbcBatchLoader = new JDBCBatchLoader();
//    jdbcBatchLoader.insertOrderDataBatch(10000, 1000);
    jdbcBatchLoader.insertOrderDataBatch2(10000, 10);

  }
}
