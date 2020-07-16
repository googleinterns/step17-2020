public class Store {
  private String id;
  private long lat;
  private long lng;

  public Store(String id, long lat, long lng) {
    this.id = id;
    this.lat = lat;
    this.lng = lng;
  }

  public String getId() {
    return id;
  }

  public long getLat() {
    return lat;
  }

  public long getLng() {
    return lng;
  }
}
