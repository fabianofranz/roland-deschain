package com.fabianofranz.deschain;

import java.util.Map;
import java.io.File;
import java.io.IOException;
import java.nio.file.Files;

public class Config {

  static public String serverIp() {
    return get(
      new String[]{
        "OPENSHIFT_VERTX_IP",
        "VERTX_IP"
      },
      "127.0.0.1");
  }

  static public Integer serverPort() {
    return getInteger(
      new String[]{
        "OPENSHIFT_VERTX_PORT",
        "VERTX_PORT"
      },
      8080);
  }

  static public String instagramClientId() {
    return get("INSTAGRAM_CLIENT_ID");
  }

  static public String instagramClientSecret() {
    return get("INSTAGRAM_CLIENT_SECRET");
  }

  static public String configDir() {
    return get("OPENSHIFT_DATA_DIR", System.getProperty("java.io.tmpdir"));
  }

  static public String configFile() {
    return configDir() + File.separator + "roland_deschain.json";
  }

  static public Boolean configured() {
    return new File(configFile()).isFile();
  }

  static public void configure() {
    try {
      Files.copy(new File("locations.json").toPath(), new File(configFile()).toPath());
    } catch (IOException e) {
      System.out.println(e.getMessage());
    }
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
      if (value != null) return value;
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
    return Integer.parseInt(get(envVarNames, defaultValue == null ? null : defaultValue.toString()));
  }

}
