import org.apache.http.client.fluent.Request;
import org.apache.http.client.fluent.Content;
import org.apache.http.client.fluent.Response;
import org.apache.http.client.fluent.Form;
import org.vertx.java.core.json.JsonArray;
import org.vertx.java.core.json.JsonObject;
import java.net.URLEncoder;
import java.util.Set;
import java.util.HashSet;
import java.util.List;
import java.util.ArrayList;
import java.io.IOException;

public class Instagram {

  static private final String INSTAGRAM_API_HOST = "https://api.instagram.com";
  static private final String INSTAGRAM_API_SUBSCRIPTIONS_ENDPOINT = "/v1/subscriptions/";
  static private final String INSTAGRAM_API_GEOGRAPHIES_ENDPOINT = "/v1/geographies/";
  static private final String CALLBACK_URL = "http://jbossvertx-ffranz.rhcloud.com/instagram/event"; 

  static public JsonArray parseEvent(final String json) {
    return new JsonArray(json);
  }

  static public List<String> parseEventObjectIds(final String json) {
    List<String> ids = new ArrayList<String>();
    JsonArray events = parseEvent(json);
    if (events != null) {
      for (int i = 0; i < events.size(); i++) {
        JsonObject event = (JsonObject) events.get(i);
        ids.add(event.getString("object_id"));
      }
    }
    return ids;
  }

  static public JsonObject fetchGeographyDetails(String geography) {
    String url = new StringBuilder().
      append(INSTAGRAM_API_HOST).
      append(INSTAGRAM_API_GEOGRAPHIES_ENDPOINT).
      append(geography).
      append("/media/recent").
      append('?').
      append("client_id=").append(Config.instagramClientId()).toString();
    try {
      String response = Request.Get(url).execute().returnContent().asString();
      return new JsonObject(response);
    } catch (IOException e) {
      e.printStackTrace();
      return null;
    }
  }

  static public void requestSubscriptionToGeography(Double latitude, Double longitude, Integer radius) {
    String url = new StringBuilder().
      append(INSTAGRAM_API_HOST).
      append(INSTAGRAM_API_SUBSCRIPTIONS_ENDPOINT).toString();

    try {
      System.out.println("Going to POST: " + url);
      Form form = Form.form().
          add("client_id", Config.instagramClientId()).
          add("client_secret", Config.instagramClientSecret()).
          add("object", "geography").
          add("aspect", "media").
          add("lat", latitude.toString()).
          add("lng", longitude.toString()).
          add("radius", radius.toString()).
          add("callback_url", encode(CALLBACK_URL));
      System.out.println("Form is " + form.build().toString());
      String response = Request.Post(url).
        bodyForm(form.build()).
        execute().returnContent().asString();
      System.out.println("requestSubscribeToGeography returned: " + response);
    } catch (Exception e) {
      e.printStackTrace();
    }
  }

  static public void unsubscribeEverything() {

  }

  static private String encode(String s) {
    try {
      return URLEncoder.encode(s, "UTF-8");
    } catch (Exception e) {
      return s;
    }
  } 

}