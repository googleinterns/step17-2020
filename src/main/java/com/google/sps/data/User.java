package com.google.sps.data;

public class User {
  private long id;
  private String email;
  private String username;

  public User(long id, String email, String username) {
    this.id = id;
    this.email = email;
    this.username = username;
  }

  public long getId() {
    return id;
  }

  public String getEmail() {
    return email;
  }

  public String getUsername() {
    return username;
  }
}
