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

public class Cache {

  static private final Integer CACHE_MAX_SIZE = 300;
  static private final Cache INSTANCE = new Cache();
  private final Queue<JsonObject> cache = new CircularFifoQueue<JsonObject>(CACHE_MAX_SIZE);

  static public Cache get() {
    return INSTANCE;
  }

  private Cache() {}

  public List<JsonObject> event(final String json) {
    return new ArrayList<JsonObject>() {{
      for (String id : Instagram.parseEventObjectIds(json)) {
        JsonObject details = Instagram.fetchGeographyDetails(id);
        cache.add(details);
        add(details);
      }
    }};
  }

}