package com.readwrite.config;

import com.readwrite.loadbalance.LoadBalance;
import com.readwrite.aspect.ReadWriteContext;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.concurrent.ConcurrentHashMap;
import javax.sql.DataSource;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.datasource.lookup.AbstractRoutingDataSource;

/**
 * @author wangyang
 * @ClassName MyDataSource
 * @Description TODO
 * @Date 2021/8/6 下午2:50
 * @Version 1.0
 */
@Slf4j
public class DynamicDataSource extends AbstractRoutingDataSource {

  public Map<String, List<Object>> dataSourceKeyCache = new ConcurrentHashMap<>();
  @Autowired
  private LoadBalance loadBalance;

  public DynamicDataSource(DataSource defaultDataSource, Map<Object, Object> targetDataSourceMap) {
    setDefaultTargetDataSource(defaultDataSource);
    setTargetDataSources(targetDataSourceMap);
    for (Entry<Object, Object> entry : targetDataSourceMap.entrySet()) {
      if (((String) entry.getKey()).startsWith(DataSourceConfig.MASTER)) {
        dataSourceKeyCache.put(DataSourceConfig.MASTER, Collections.singletonList(entry.getKey()));
      } else {
        List<Object> dataSources = dataSourceKeyCache.computeIfAbsent(DataSourceConfig.SLAVE, k -> new ArrayList<>());
        dataSources.add(entry.getKey());
      }
    }
  }

  protected Object determineCurrentLookupKey() {
    String masterOrSlave = ReadWriteContext.get();
    Object select = loadBalance.select(dataSourceKeyCache.get(masterOrSlave));
    log.info("current datasource is {}", select);
    return select;
  }


}
