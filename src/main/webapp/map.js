let map;

/* Editable marker that displays when a user clicks in the map. */
let editMarker;

let markers = [[40.7122879, -74.0106218, "Starbucks"], [40.7105991, -74.0112465, "Irving Farm New York"]]

/** Creates a map that allows users to add markers. */
function createMap() {
  map = new google.maps.Map(
    document.getElementById('map'),
    {center: {lat: 40.7128, lng: -74.0060}, zoom: 12});

  // When the user clicks in the map, show a marker with a text box the user can
  // edit.
  map.addListener('click', (event) => {
    createMarkerForEdit(event.latLng.lat(), event.latLng.lng());
  });
  fetchMarkers();
}

/** Fetches markers from the backend and adds them to the map. */
function fetchMarkers() {
  for (i = 0; i < markers.length; i++) { 
    createMarkerForDisplay(markers[i]);
  }
}

/** 
 *  Creates a marker that shows a info window when clicked. 
 *  The link leads to the coffee shop page which contains
 *  individual coffee shop information
 */
function createMarkerForDisplay(shop) {
  const marker =
    new google.maps.Marker({position: {lat: shop[0], lng: shop[1]}, map: map, title: shop[2]});
  const infoWindow = new google.maps.InfoWindow({content: '<a href=coffeeshop.html>' + shop[2] + '</a>'});
  marker.addListener('click', () => {
    infoWindow.open(map, marker);
    map.setZoom(14);
    map.setCenter(LatLng(shop[0], shop[1]));
  });
}