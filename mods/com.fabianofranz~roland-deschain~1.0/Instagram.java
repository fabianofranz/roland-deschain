import org.apache.http.client.fluent.Request;
import org.apache.http.client.fluent.Content;
import org.apache.http.client.fluent.Response;
import org.apache.http.client.fluent.Form;
import java.net.URLEncoder;

public class Instagram {

  static private final String INSTAGRAM_API_HOST = "https://api.instagram.com";
  static private final String INSTAGRAM_API_ENDPOINT = "/v1/subscriptions/";
  static private final String CALLBACK_URL = "http://jbossvertx-ffranz.rhcloud.com/instagram/event"; 

  static public void requestSubscriptionToGeography(Double latitude, Double longitude, Integer radius) {
    String url = new StringBuilder().append(INSTAGRAM_API_HOST).append(INSTAGRAM_API_ENDPOINT).toString();

    try {
      System.out.println("Going to POST: " + url);
      String response = Request.Post(url).
        bodyForm(Form.form().
          add("client_id", Config.instagramClientId()).
          add("client_secret", Config.instagramClientSecret()).
          add("object", "geography").
          add("aspect", "media").
          add("lat", latitude.toString()).
          add("lng", longitude.toString()).
          add("radius", radius.toString()).
          add("callback_url", encode(CALLBACK_URL)).build()).
        execute().returnContent().asString();
      System.out.println("requestSubscribeToGeography returned: " + response);
    } catch (Exception e) {
      System.out.println(e.toString());
      e.printStackTrace();
    }
  }

  static public void verifySubscriptionToGeography(String challenge) {
    String url = new StringBuilder().
      append(INSTAGRAM_API_HOST).
      append(INSTAGRAM_API_ENDPOINT).
      append('?').
      append("hub.challenge=").append(challenge).toString();
    try {
      System.out.println("Going to GET: " + url);
      String response = Request.Get(url).execute().returnContent().asString();
      System.out.println("verifySubscriptionToGeography returned: " + response);
    } catch (Exception e) {
      System.out.println(e.toString());
      e.printStackTrace();
    }
  }

  static private String encode(String s) {
    try {
      return URLEncoder.encode(s, "UTF-8");
    } catch (Exception e) {
      return s;
    }
  } 

}