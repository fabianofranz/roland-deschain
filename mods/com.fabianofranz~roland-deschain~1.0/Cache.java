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

  public List<String> event(String json) {
    List<String> details = new ArrayList<String>();

    JSONParser parser = new JSONParser();

    JSONArray events = (JSONArray) parser.parse(json);
    for (int i = 0; i < events.size(); i++) {
      JSONObject event = (JSONObject) events.get(i);
      String id = (String) event.get("object_id");
      details.add(Instagram.fetchGeographyDetails(id));
    }

    return details;
  }

}