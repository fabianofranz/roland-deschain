public class Instagram {

  static private final String INSTAGRAM_API_HOST = "https://api.instagram.com" 
  static private final String INSTAGRAM_API_ENDPOINT = "/v1/subscriptions/" 

  public void subscribeToGeography(Long latitude, Long longitude, Integer radius) {
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
      append("callback_url=").append("http://jbossvertx-ffranz.rhcloud.com/instagram").toString();
  }

}