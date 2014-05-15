import java.util.List;
import java.util.ArrayList;
import java.util.Set;
import java.util.Map;
import org.boon.json.ObjectMapper;
import org.boon.json.ObjectMapperFactory;

public class Cache {

  static private final Cache INSTANCE = new Cache();

  static public Cache get() {
    return INSTANCE;
  }

  private Cache() { }

  public List<String> event(String json) {
    List<String> details = new ArrayList<String>();

    ObjectMapper mapper =  ObjectMapperFactory.create();

    List<Map<String, String>> events = 
      (List<Map<String, String>>) mapper.readValue(json, List.class, Map.class);

    for (Map<String, String> event : events) {
      String id = event.get("object_id");
      details.add(Instagram.fetchGeographyDetails(id));
    }

    return details;
  }

}