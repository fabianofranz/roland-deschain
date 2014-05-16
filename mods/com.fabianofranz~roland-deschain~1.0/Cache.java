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
      new LinkedHashMap<String, JsonObject>() {

        public V put(K key, V value) {
          V value = get(key);
          if (value == null) {
            return super.put(key, value);
          } else {
            return value;
          }
        }

        protected boolean removeEldestEntry(Map.Entry eldest) {
          return size() > CACHE_MAX_SIZE;
        }

      });

  private Cache() {}

  static public Cache instance() {
    return INSTANCE;
  }

  public Map<String, JsonObject> cache() {
    return cache;
  }

  public List<JsonObject> event(final String json) {
    return new ArrayList<JsonObject>() {{
      for (String id : Instagram.parseEventObjectIds(json)) {
        JsonObject details = Instagram.fetchGeographyDetails(id);
        cache.put(id, details);
        add(details);
      }
    }};
  }

}