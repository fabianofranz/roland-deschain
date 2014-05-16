import java.util.List;
import java.util.ArrayList;
import java.util.Set;
import java.util.Map;
import org.json.simple.parser.JSONParser;
import org.json.simple.JSONArray;
import org.json.simple.JSONObject;

public class Cache {

  static private final Cache INSTANCE = new Cache();

  static public Cache get() {
    return INSTANCE;
  }

  private Cache() { }

  public List<JSONObject> event(String json) {
    List<JSONObject> details = new ArrayList<JSONObject>();
    for (String id : Instagram.parseEventObjectIds(json)) {
      details.add(Instagram.fetchGeographyDetails(id));
    }
    return details;
  }

}