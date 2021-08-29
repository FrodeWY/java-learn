package com.rpc.core.utils;

import static com.rpc.core.common.RegistryConstants.AND;
import static com.rpc.core.common.RegistryConstants.EQUAL;
import static com.rpc.core.common.RegistryConstants.QUESTION_MARK;

import com.rpc.core.common.RegistryConstants;
import java.io.UnsupportedEncodingException;
import java.net.URLDecoder;
import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;
import java.util.HashMap;
import java.util.Map;
import java.util.Map.Entry;

/**
 * @author wangyang
 * @ClassName ServicePathUtil
 * @Description 服务提供者路径解析工具
 * @Date 2021/8/26 下午10:22
 * @Version 1.0
 */
public class ServicePathUtil {

  /**
   * 将map转成String
   */
  public static String mapToString(Map<String, String> map) {
    if (map == null || map.size() == 0) {
      return "";
    }
    StringBuilder builder = new StringBuilder();
    for (Entry<String, String> entry : map.entrySet()) {
      builder.append(entry.getKey()).append(RegistryConstants.EQUAL).append(entry.getValue()).append(RegistryConstants.AND);
    }
    return builder.substring(0, builder.length() - 2);
  }

  /**
   * 对字符串使用URLEncoder 转义
   */
  public static String pathParamsEncode(String str) {
    try {
      return URLEncoder.encode(str, StandardCharsets.UTF_8.name());
    } catch (UnsupportedEncodingException e) {
      throw new RuntimeException(e);
    }
  }

  /**
   * 对转义后的字符串使用URLDecoder得到原字符串
   */
  public static String pathParamsDecode(String encodeStr) {
    try {
      return URLDecoder.decode(encodeStr, StandardCharsets.UTF_8.name());
    } catch (UnsupportedEncodingException e) {
      throw new RuntimeException(e);
    }
  }

  public static Map<String, String> getParamMap(String servicePath) {
    int index = servicePath.indexOf(QUESTION_MARK);
    if (index == -1) {
      return new HashMap<>();
    }
    String substring = servicePath.substring(index + 1);
    String paramsDecode = pathParamsDecode(substring);
    String[] split = paramsDecode.split(AND);
    Map<String, String> map = new HashMap<>(split.length);
    for (String s : split) {
      String[] entryArr = s.split(EQUAL);
      map.put(entryArr[0], entryArr[1]);
    }
    return map;
  }

  public static void main(String[] args) throws UnsupportedEncodingException {
    Map<String, String> map = new HashMap<>();
    map.put("test", "test");
    String paramString = ServicePathUtil.mapToString(map);
    String encode = URLEncoder.encode(paramString, StandardCharsets.UTF_8.name());
    System.out.println(encode);
    System.out.println(URLDecoder.decode(encode, StandardCharsets.UTF_8.name()));
  }
}
