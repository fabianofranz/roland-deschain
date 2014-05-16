import java.util.List;
import java.util.ArrayList;
import java.util.Set;
import java.util.Map;
import org.vertx.java.core.json.JsonArray;
import org.vertx.java.core.json.JsonObject;

public class Cache {

  static private final Cache INSTANCE = new Cache();

  static public Cache get() {
    return INSTANCE;
  }

  private Cache() { }

  public List<JsonObject> event(final String json) {
    return new ArrayList<JsonObject>() {{
      for (String id : Instagram.parseEventObjectIds(json)) {
        add(Instagram.fetchGeographyDetails(id));
      }
    }};
  }

}