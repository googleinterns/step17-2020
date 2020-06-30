function determineLogin() {
  fetch('/login').then(response => response.json()).then((isLoggedIn) => {
    console.log("Displaying login info")
    const element = document.getElementById('form');
    const textElement = document.createElement('span');
    const form = document.createElement('form');
    form.method='GET';
    form.action='/login';
    const button = document.createElement('button');
    if (!isLoggedIn) {
      textElement.innerText = "Please log in to view this page";
      button.innerHTML = "Login";
      console.log("not logged in");
    } else {
      textElement.innerText = "You're logged in!";
      button.innerHTML = "Logout";
      console.log("logged in");
    }
    form.appendChild(button);
    element.appendChild(textElement);
    element.appendChild(form);
  }).catch(error => {
    console.error('There has been a problem with your operation:', error);
  });
}

//how to fetch data from servlet
async function getData() {
  const response = await fetch('/data');
  const data = await response.text();
  document.getElementById('server-data-container').innerText = data;
}

// function getUserEmail() {fetch('/').then(response => response.json()).then((isLoggedIn) => {
//     console.log("Displaying login info")

// }

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
function createMarkerForDisplay(markers) {
  const marker =
    new google.maps.Marker({position: {lat: markers[0], lng: markers[1]}, map: map, title: markers[2]});
  const infoWindow = new google.maps.InfoWindow({content: '<a href=coffeeshop.html>'});
  marker.addListener('click', () => {
    infoWindow.open(map, marker);
    map.setZoom(14);
    map.setCenter(LatLng(markers[0], markers[1]));
  });
}

function createCoffeeShopPage(shopName){
  document.title = shopName;

  return containerDiv;
}
