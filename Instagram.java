import org.apache.http.client.fluent.Request;
import org.apache.http.client.fluent.Content;
import org.apache.http.client.fluent.Response;
import java.net.URLEncoder;

public class Instagram {

  static private final String INSTAGRAM_API_HOST = "https://api.instagram.com";
  static private final String INSTAGRAM_API_ENDPOINT = "/v1/subscriptions/";
  static private final String CALLBACK_URL = "http://jbossvertx-ffranz.rhcloud.com/instagram/event"; 

  static public void requestSubscriptionToGeography(Double latitude, Double longitude, Integer radius) {
    String url = new StringBuilder().
      append(INSTAGRAM_API_HOST).
      append(INSTAGRAM_API_ENDPOINT).
      append('?').
      append("client_id=").append(Config.instagramClientId()).append('&').
      append("client_secret=").append(Config.instagramClientSecret()).append('&').
      append("object=geography").append('&').
      append("aspect=media").append('&').
      append("lat=").append(latitude.toString()).append('&').
      append("lng=").append(longitude.toString()).append('&').
      append("radius=").append(radius.toString()).append('&').
      append("callback_url=").append(encode(CALLBACK_URL)).toString();
    System.out.println("requestSubscribeToGeography returned: " + Request.Post(url).execute().returnContent().asString();
  }

  static public void verifySubscriptionToGeography(String challenge) {
    String url = new StringBuilder().
      append(INSTAGRAM_API_HOST).
      append(INSTAGRAM_API_ENDPOINT).
      append('?').
      append("hub.challenge=").append(challenge).toString();
    System.out.println("verifySubscriptionToGeography returned: " + Request.Post(url).execute().returnContent().asString();
  }

  static private String encode(String s) {
    try {
      return URLEncoder.encode(s, "UTF-8");
    } catch (Exception e) {
      return s;
    }
  } 

}