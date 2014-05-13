package java;

public class Env {

  static public String get(String envVarName) {
    return System.getenv(envVarName);
  }

}