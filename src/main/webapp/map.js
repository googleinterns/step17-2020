var map;
var shopInfo;
var userPos;
var geocoder;
var markers = [];
var coffeeShopInfo = [];

/** Creates a map that shows all coffee shops around the user. */
function createMap() {
  // Set default location at new york city
  var newyork = new google.maps.LatLng(40.7128, -74.0060);
  map = new google.maps.Map(
    document.getElementById('map'),
    {center: newyork, zoom: 13});
  
  // Global infowindow for coffee shop
  shopInfo = new google.maps.InfoWindow();

  getUserLocation();
}

function getUserLocation() {
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
        handleLocationError(true);
      }
    );
  } else {
    // Browser doesn't support Geolocation
    handleLocationError(false);
  }
}

/**
/* Request nearby coffee shop info
/* Unit of radius: Metres. Maximum allowed in Places API is 50000 metres
 */
function coffeeShopRequest(userPos) {
	var request = {
    location: userPos,
    radius: '300',
    query: 'coffee shop'
  };
  if (typeof map === "undefined") {
    service = new google.maps.places.PlacesService(document.createElement('div'));
  } else {
    service = new google.maps.places.PlacesService(map);
  }
  service.textSearch(request, callback);
}

function handleLocationError(browserHasGeolocation) {
  if (browserHasGeolocation) {
  	alert("The Geolocation service failed. Please enter a zip code.");
  } else {
  	alert("Your browser doesn't support geolocation. Please enter a zip code.");
  }
}

/* Call this wherever needed to actually handle the display */
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
        // Got result, center the map and put it out there
        if (typeof map !== "undefined") {
          map.setCenter(results[0].geometry.location);
        }
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
    // Display markers for Explore tab
    if (document.URL.includes("store.html")) {
      for (var i = 0; i < results.length; i++) {
        createMarker(results[i]);
        displayMarkers();
      }
    // Calculate distance for Search for a drink tab
    } else {
      coffeeShopInfo = [];
      for (var i = 0; i < results.length; i++) {
        var distance = haversine_distance(results[i].geometry.location.lat(),
          results[i].geometry.location.lng(), userPos.lat, userPos.lng);
        // Create coffeeshop array that contains the name, address
        // and distance from user
        var coffeeShop = {
          'name': results[i].name,
          'address': results[i].formatted_address,
          'store': results[i].place_id,
          'distance': distance,
          'lat': results[i].geometry.location.lat(),
          'lng': results[i].geometry.location.lng()
        };
        coffeeShopInfo.push(coffeeShop);
        coffeeShopInfo.sort( function(a, b) {
          return (a['distance'] - b['distance']);
        });
      }
    }
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
  markers.forEach(marker => marker.setMap(map));
}

function clearMarkers() {
  markers.forEach(marker => marker.setMap(null));
  markers = [];
}

/**
/* Calculates the straight line distance between two
/* sets of latitutes and longitutes
/* A breakdown of the haversine formula can be found at
/* http://www.movable-type.co.uk/scripts/latlong.html
 */
function haversine_distance(lat1,lon1,lat2,lon2) {
  var R = 6371; // Radius of the earth in km
  var dLat = degToRad(lat2-lat1);
  var dLon = degToRad(lon2-lon1); 
  var a = 
    Math.sin(dLat/2) * Math.sin(dLat/2) +
    Math.cos(degToRad(lat1)) * Math.cos(degToRad(lat2)) * 
    Math.sin(dLon/2) * Math.sin(dLon/2);
  var c = 2 * Math.atan2(Math.sqrt(a), Math.sqrt(1-a)); 
  var d = R * c; // Distance in km
  return d;
}

function degToRad(deg) {
  return deg * (Math.PI/180)
}

/**
/* Gets the location and name of the place
/* and displays the walking route on Google Map
 */
function displayRoute(userPos, coffeeShop) {
  var directionsDisplay = new google.maps.DirectionsRenderer();
  directionsDisplay.setMap(map);

  var request = {
    origin : userPos,
    destination : coffeeShop,
    travelMode : google.maps.TravelMode.WALKING
  };
  var directionsService = new google.maps.DirectionsService(); 
  directionsService.route(request, function(response, status) {
    if (status == google.maps.DirectionsStatus.OK) {
      directionsDisplay.setDirections(response);
    }
  });
}

function searchForDrink() {
  // Prompt user to enter a location
  if (typeof userPos === "undefined") {
    alert("Please enter a zip code before searching for a drink.")
  }
  var drink = document.getElementById("beverage").value;
  var filtersElement = document.getElementById("filters");
  var filter = filtersElement.options[filtersElement.selectedIndex].value;
  var params = new URLSearchParams();
  params.append('drink', drink);
  params.append('filter', filter)
  params.append('coffeeshop', JSON.stringify(coffeeShopInfo));
  document.getElementById("store-list").innerHTML = "";

  // Get results from search servlet
  fetch('/search', {method: 'POST', body: params}).then(function(stores) {
    return stores.json();
  }).then(function(stores) {
    const ratingListElement = document.getElementById('store-list');
    stores.forEach((store) => {
      ratingListElement.appendChild(createListElement(store));
    })
  }).catch(e => {
    console.log(e)
  });
}

function createListElement(store) {
  const listElement = document.createElement('li');
  listElement.className = 'store';
  const storeElement = document.createElement('span');

  // Display search by rating
  if (store.rating !== undefined) {
    storeElement.innerText = store.name + " at " + store.address + ", with a rating of " + store.rating;
  
  // Display search by distance
  } else {
    storeElement.innerText = store.name + " at " + store.address + ", " + store.distance + " km away.";
  }
  listElement.appendChild(storeElement);
  return listElement;
}
