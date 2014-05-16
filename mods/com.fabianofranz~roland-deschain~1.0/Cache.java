import java.util.List;
import java.util.ArrayList;
import java.util.Set;
import java.util.Map;
import java.util.LinkedHashSet;
import java.util.Collections;
import org.vertx.java.core.json.JsonArray;
import org.vertx.java.core.json.JsonObject;

public class Cache {

  static private final Cache INSTANCE = new Cache();
  private final Set<JsonObject> cache = Collections.synchronizedSet(new LinkedHashSet<JsonObject>());

  static public Cache get() {
    return INSTANCE;
  }

  private Cache() { }

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