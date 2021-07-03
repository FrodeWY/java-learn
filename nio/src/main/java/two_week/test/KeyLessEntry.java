package two_week.test;

import java.util.HashMap;
import java.util.Map;
import java.util.Objects;

/**
 * @author wangyang
 * @ClassName KeyLessEntry
 * @Description TODO
 * @Date 2021/6/28 下午10:14
 * @Version 1.0
 */
public class KeyLessEntry {

  static class Key {

    Integer id;

    public Key(Integer id) {
      this.id = id;
    }

//    @Override
//    public boolean equals(Object o) {
//      boolean response = false;
//      if (o instanceof Key) {
//        response = (((Key) o).id).equals(this.id);
//      }
//      return response;
//    }

    @Override
    public int hashCode() {
      return id.hashCode();
    }
  }

  public static void main(String[] args) {
    Key key1 = new Key(1000);
    Key key2 = new Key(1000);
    Map<Key, String> map = new HashMap<Key, String>();
    map.put(key1, "key1");
    map.put(key2, "key2");
    System.out.println(map.values());
  }
}
