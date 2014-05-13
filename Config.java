public class Config {

  static public String serverIp() {
    return get("OPENSHIFT_VERTX_IP", "127.0.0.1");
  }

  static public Integer serverPort() {
    return getInteger("OPENSHIFT_VERTX_PORT", 8080);
  }

  static public String get(String envVarName) {
    return get(envVarName, null);
  }

  static public String get(String[] envVarNames, String defaultValue) {
    for (int i = 0; i < envVarNames.length; i++) {
      String value = System.getenv(envVarNames[i]);
      if value != null return value;
    }
    return defaultValue;
  }

  static public String get(String envVarName, String defaultValue) {
    return get([envVarName], defaultValue);
  }

  static public Integer getInteger(String envVarName) {
    return Integer.parseInt(get(envVarName));
  }

  static public Integer getInteger(String envVarName, Integer defaultValue) {
    Integer value = getInteger(envVarName);
    return value == null ? defaultValue : value;
  }

}