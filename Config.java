public class Config {

  static public String get(String envVarName) {
    return System.getenv(envVarName);
  }

  static public String get(String envVarName, String defaultValue) {
    String value = get(envVarName);
    return value == null ? defaultValue : value;
  }

  static public Integer getInteger(String envVarName) {
    return Integer.parseInt(get(envVarName));
  }

  static public Integer getInteger(String envVarName, Integer defaultValue) {
    Integer value = getInteger(envVarName);
    return value == null ? defaultValue : value;
  }

  static public String serverIp() {
    return get("OPENSHIFT_VERTX_IP", "127.0.0.1");
  }

  static public Integer serverPort() {
    return getInteger("OPENSHIFT_VERTX_PORT", "8080");
  }

}