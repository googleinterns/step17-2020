var map;
var infoWindow;
var shopInfo;
var userPos;

/** Creates a map that shows all coffee shops around the user. */
function createMap() {
  // Set default location at new york city
  var newyork = new google.maps.LatLng(40.7128, -74.0060);
  // Infowindow for to handle error in get user location
  infoWindow = new google.maps.InfoWindow();
  // Global infowindow for coffee shop
  shopInfo = new google.maps.InfoWindow();

  map = new google.maps.Map(
    document.getElementById('map'),
    {center: newyork, zoom: 14});

  // Try HTML5 geolocation to get user location.
  if (navigator.geolocation) {
    navigator.geolocation.getCurrentPosition(
      function(position) {
        userPos = {
          lat: position.coords.latitude,
          lng: position.coords.longitude
        };
        map.setCenter(userPos);

        // Request nearby coffee shop info
        var request = {
          location: userPos,
          radius: '500',
          query: 'coffee shop'
        };
        service = new google.maps.places.PlacesService(map);
        service.textSearch(request, callback);
      },
      function() {
        handleLocationError(true, infoWindow, map.getCenter());
      }
    );
  } else {
    // Browser doesn't support Geolocation
    handleLocationError(false, infoWindow, map.getCenter());
  }
}

function handleLocationError(browserHasGeolocation, infoWindow, userPos) {
  infoWindow.setPosition(userPos);
  infoWindow.setContent(
    browserHasGeolocation
      ? "Error: The Geolocation service failed."
      : "Error: Your browser doesn't support geolocation."
  );
  infoWindow.open(map);
}

function callback(results, status) {
  if (status == google.maps.places.PlacesServiceStatus.OK) {
    for (var i = 0; i < results.length; i++) {
      // var place = results[i];
      createMarker(results[i]);
    }
  }
}

function createMarker(place) {
  var marker = new google.maps.Marker({
    map: map,
    position: place.geometry.location
  });
  marker.addListener('click', () => {
    shopInfo.close();
    shopInfo.setContent('<a href=coffeeshop.html>' + place.name + '</a>');
    localStorage.setItem("shopName", place.name);
    localStorage.setItem("address", place.formatted_address);
    shopInfo.open(map, marker);
  });
}

function getShopName() {
  document.getElementById('title').innerHTML = localStorage.getItem("shopName");
  document.getElementById('address').innerHTML = localStorage.getItem("address");
}