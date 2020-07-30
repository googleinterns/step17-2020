package com.google.sps.data;

public class Store { // todo: update average rating every time a store gets a new comment
  private String id;
  private double lat;
  private double lng;
  private int numComments;
  private double avgRating;

  public Store(String id, double lat, double lng, int numComments, double avgRating) {
    this.id = id;
    this.lat = lat;
    this.lng = lng;
    this.numComments = numComments;
    this.avgRating = avgRating;
  }

  public String getId() {
    return id;
  }

  public double getLat() {
    return lat;
  }

  public double getLng() {
    return lng;
  }

  public int getNumberOfComments() {
    return numComments;
  }

  public double getRating() {
    return avgRating;
  }
}
