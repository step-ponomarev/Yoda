package edu.ponomarev.step.dao;

import java.util.Properties;

public class DAO_Properties extends Properties {
  private String url;
  private String user;
  private String password;

  public DAO_Properties(String url, String user, String password) {
    this.url = url;
    this.user = user;
    this.password = password;

    this.put("user", user);
    this.put("password", password);
  }

  String getUrl() {
    return url;
  }

  String getUser() {
    return user;
  }

  String getPassword() {
    return password;
  }
}
