import java.util.List;
import java.util.ArrayList;

public class Cache {

  static private final Cache INSTANCE = new Cache();

  static public Cache get() {
    return INSTANCE;
  }

  private Cache() { }

  public List<String> event(String json) {
    List<String> details = new ArrayList<String>();
    Set<String> geographies = Instagram.wasNotifiedFor(json);
    for (String geography : geographies) {
      details.add(Instagram.fetchGeographyDetails(geography));
    }
    return details;
  }

}