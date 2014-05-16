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
          System.out.println("Cache:cache: removeEldestEntry? " + remove);
          return remove;
        }
      });

  private Cache() {
    System.out.println("Cache: created instance!");
  }

  static public Cache instance() {
    return INSTANCE;
  }

  public Integer size() {
    return cache.size();
  }

  public Boolean insert(String key, JsonObject value) {
    JsonObject v = cache.get(key);
    if (v == null) {
      System.out.println("Cache:cache:put Will put " + key);
      cache.put(key, value);
      return true;
    } else {
      System.out.println("Cache:cache:put " + key + " already there");
      return false;
    }
  }

  public List<JsonObject> handleEvent(final String json) {
    return new ArrayList<JsonObject>() {{
      for (String geography : Instagram.parseEventObjectIds(json)) {
        JsonObject envelope = Instagram.fetchGeographyDetails(geography);
        JsonArray items = envelope.getArray("data");
        for (int i = 0; i < items.size(); i++) {
          JsonObject item = (JsonObject) items.get(i);
          String id = item.getString("object_id");
          if (insert(id, item)) {
            add(item);
          }
        }
      }
    }};
  }

}