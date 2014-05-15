import java.util.List;
import java.util.ArrayList;
import java.util.Set;
import java.util.Map;
import org.boon.json.JsonParser;
import org.boon.json.JsonParserAndMapper;
import org.boon.json.JsonParserFactory;

public class Cache {

  static private final Cache INSTANCE = new Cache();

  static public Cache get() {
    return INSTANCE;
  }

  private Cache() { }

  public List<String> event(String json) {
    List<String> details = new ArrayList<String>();

    JsonParserAndMapper mapper = new JsonParserFactory().create();
    List<Map> events = (List<Map>) mapper.parseList(Map.class, json);

    for (Map event : events) {
      String id = (String) event.get("object_id");
      details.add(Instagram.fetchGeographyDetails(id));
    }

    return details;
  }

}