var map;
var infoWindow;
var shopInfo;
var userPos;
var geocoder;
var markers = [];

/** Creates a map that shows all coffee shops around the user. */
function createMap() {
  // Set default location at new york city
  var newyork = new google.maps.LatLng(40.7128, -74.0060);
  map = new google.maps.Map(
    document.getElementById('map'),
    {center: newyork, zoom: 13});
  
  // Infowindow for to handle error in get user location
  infoWindow = new google.maps.InfoWindow();
  // Global infowindow for coffee shop
  shopInfo = new google.maps.InfoWindow();

  // Try HTML5 geolocation to get user location.
  if (navigator.geolocation) {
    navigator.geolocation.getCurrentPosition(
      function(position) {
        userPos = {
          lat: position.coords.latitude,
          lng: position.coords.longitude
        };
        map.setCenter(userPos);
        coffeeShopRequest(userPos);
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

// Request nearby coffee shop info
function coffeeShopRequest(userPos) {
	var request = {
    location: userPos,
    radius: '300',
    query: 'coffee shop'
  };
  service = new google.maps.places.PlacesService(map);
  service.textSearch(request, callback);
}

function handleLocationError(browserHasGeolocation, infoWindow, userPos) {
  infoWindow.setPosition(userPos);
  if (browserHasGeolocation) {
  	infoWindow.setContent("The Geolocation service failed. Please enter a zip code to view nearby coffee shops.");
  } else {
  	infoWindow.setContent("Your browser doesn't support geolocation. Please enter a zip code to view nearby coffee shops.");
  }
  infoWindow.open(map);
}

// Call this wherever needed to actually handle the display
function codeAddress() {
	// Clear markers from previous search results
	clearMarkers();
	geocoder = new google.maps.Geocoder();
	var zipCode = document.getElementById("zipcode").value;
    geocoder.geocode( {
      componentRestrictions: {
        country: 'US',
        postalCode: zipCode
      }
    }, function(results, status) {
      if (status == google.maps.GeocoderStatus.OK) {
        //Got result, center the map and put it out there
        map.setCenter(results[0].geometry.location);
        userPos = {
          lat: results[0].geometry.location.lat(),
          lng: results[0].geometry.location.lng()
        };
        coffeeShopRequest(userPos);
      } else {
        alert('Geocode was not successful for the following reason: ' + status);
      }
   });
}
  
 function callback(results, status) {
  if (status == google.maps.places.PlacesServiceStatus.OK) {
    for (var i = 0; i < results.length; i++) {
        createMarker(results[i]);
    }
    displayMarkers();
  }
}

/**
/* Takes a JSON object returned by Places API query
/* Gets the location and name of the place
/* Adds a marker at corresponding place on the map
 */
function createMarker(place) {
  var marker = new google.maps.Marker({
    map: map,
    position: place.geometry.location
  });
  marker.addListener('click', () => {
    shopInfo.close();
    shopInfo.setContent('<a href=coffeeshop.html>' + place.name + '</a>');
    // Save place name and address in local storage
    // to display in the coffeeshop page
    localStorage.setItem("shopName", place.name);
    localStorage.setItem("address", place.formatted_address);
    localStorage.setItem("store", place.place_id);
    shopInfo.open(map, marker);
  });
  markers.push(marker);
}

function displayMarkers() {
  for (var i = 0; i < markers.length; i++ ) {
    markers[i].setMap(map);
  }
}

function clearMarkers() {
  for (var i = 0; i < markers.length; i++ ) {
    markers[i].setMap(null);
  }
  markers = [];
}
