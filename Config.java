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

  static public String get(String envVarName, String defaultValue) {
    return get(new String[]{envVarName}, defaultValue);
  }

  static public String get(String[] envVarNames, String defaultValue) {
    for (int i = 0; i < envVarNames.length; i++) {
      String value = System.getenv(envVarNames[i]);
      if value != null return value;
    }
    return defaultValue;
  }

  static public Integer getInteger(String envVarName) {
    return getInteger(envVarName, null);
  }

  static public Integer getInteger(String envVarName, Integer defaultValue) {
    return getInteger(new String[]{envVarName}, defaultValue);
  }

  static public Integer getInteger(String[] envVarNames, Integer defaultValue) {
    Integer value = Integer.parseInt(get(envVarNames));
    return value == null ? defaultValue : value;
  }

}