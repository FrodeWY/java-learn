package com.rpc.core.utils;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.util.Collection;
import java.util.Collections;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Properties;
import org.apache.commons.lang.StringUtils;
import org.yaml.snakeyaml.Yaml;

/**
 * @author wangyang
 * @ClassName YamlAndPropertiesConvertUtil
 * @Description TODO
 * @Date 2021/8/29 下午3:15
 * @Version 1.0
 */
public class YamlAndPropertiesConvertUtil {

  private static Map<String, Map<String, Object>> ymlMap = new HashMap<>();

  public static boolean checkIsYAML(String data) {
    if (data.contains("=")) {
      return false;
    }
    return true;
  }

  public static Properties getProperties(byte[] bytes) throws IOException {
    String dataString = new String(bytes);
    if (StringUtils.isBlank(dataString)) {
      return new Properties();
    }
    Properties properties = new Properties();
    if (checkIsYAML(dataString)) {
      Yaml yaml = new Yaml();
      Map<String, Object> map = (Map<String, Object>) yaml.loadAs(dataString, Map.class);
      flattenedMap(properties, map);
      return properties;
    }
    properties.load(new ByteArrayInputStream(dataString.getBytes()));
    Map<String, Object> map = new HashMap<>();
    properties.putAll(map);
    flattenedMap(properties, map);
    return properties;
  }

  private static void flattenedMap(Properties properties, Map<String, Object> map) {
    Map<String, Object> flattenedMap = getFlattenedMap(map);
    for (Entry<String, Object> entry : flattenedMap.entrySet()) {
      properties.put(entry.getKey(), entry.getValue());
    }
  }

  public static String getValue(String key, Properties properties) {
    String separator = ".";
    String[] separatorKeys = null;
    if (key.contains(separator)) {
      separatorKeys = key.split("\\.");
    } else {
      return properties.get(key).toString();
    }
    Map finalValue = new HashMap<>();
    for (int i = 0; i < separatorKeys.length - 1; i++) {
      if (i == 0) {
        finalValue = (Map<String, Map<String, Object>>) properties.get(separatorKeys[i]);
        continue;
      }
      if (finalValue == null) {
        break;
      }
      finalValue = (Map) finalValue.get(separatorKeys[i]);
    }
    return finalValue == null ? null : finalValue.get(separatorKeys[separatorKeys.length - 1]).toString();
  }


  private static Map<String, Object> getFlattenedMap(Map<String, Object> source) {
    Map<String, Object> result = new LinkedHashMap<>();
    buildFlattenedMap(result, source, null);
    return result;
  }

  private static void buildFlattenedMap(Map<String, Object> result, Map<String, Object> source, String path) {
    source.forEach((key, value) -> {
      if (!StringUtils.isBlank(path)) {
        if (key.startsWith("[")) {
          key = path + key;
        } else {
          key = path + '.' + key;
        }
      }
      if (value instanceof String) {
        result.put(key, value);
      } else if (value instanceof Map) {
        // Need a compound key
        @SuppressWarnings("unchecked")
        Map<String, Object> map = (Map<String, Object>) value;
        buildFlattenedMap(result, map, key);
      } else if (value instanceof Collection) {
        // Need a compound key
        @SuppressWarnings("unchecked")
        Collection<Object> collection = (Collection<Object>) value;
        if (collection.isEmpty()) {
          result.put(key, "");
        } else {
          int count = 0;
          for (Object object : collection) {
            buildFlattenedMap(result, Collections.singletonMap(
                "[" + (count++) + "]", object), key);
          }
        }
      } else {
        result.put(key, (value != null ? value : ""));
      }
    });
  }

}
