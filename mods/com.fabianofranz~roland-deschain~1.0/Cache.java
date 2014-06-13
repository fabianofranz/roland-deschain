import java.util.List;
import java.util.ArrayList;
import java.util.Set;
import java.util.Map;
import java.util.LinkedHashSet;
import java.util.Collections;
import org.vertx.java.core.json.JsonArray;
import org.vertx.java.core.json.JsonObject;
import org.apache.commons.collections4.queue.CircularFifoQueue;
import java.util.Queue;
import java.util.LinkedHashMap;

public class Cache {

  static private final Integer CACHE_MAX_SIZE = 300;

  static private final Cache INSTANCE = new Cache();

  private final Map<String, JsonObject> cache = 
    Collections.synchronizedMap(
      new LinkedHashMap<String, JsonObject>(CACHE_MAX_SIZE) {
        protected boolean removeEldestEntry(Map.Entry eldest) {
          boolean remove = size() > CACHE_MAX_SIZE;
          return remove;
        }
      });

  private Cache() { }

  static public Cache instance() {
    return INSTANCE;
  }

  public Integer size() {
    return cache.size();
  }

  public Boolean insert(String key, JsonObject value) {
    JsonObject v = cache.get(key);
    if (v == null) {
      cache.put(key, value);
      return true;
    } else {
      return false;
    }
  }

}