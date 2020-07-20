// Copyright 2020 Google LLC
//
// Licensed under the Apache License, Version 2.0 (the "License");
// you may not use this file except in compliance with the License.
// You may obtain a copy of the License at
//
//     https://www.apache.org/licenses/LICENSE-2.0
//
// Unless required by applicable law or agreed to in writing, software
// distributed under the License is distributed on an "AS IS" BASIS,
// WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
// See the License for the specific language governing permissions and
// limitations under the License.

google.charts.load('current', {'packages':['corechart']});
google.charts.setOnLoadCallback(drawChart);
var ratingMap = new Map();

// Creates a chart and adds it to the page
function drawChart() {
  //   fetch('/store-rating').then(response => response.json())
  // .then((votes) => {
    const data = new google.visualization.DataTable();
    data.addColumn('string', 'Rating');
    data.addColumn('number', 'Votes');
    // Object.keys(votes).forEach((rating) => {
      // data.addRow([rating, votes[rating]]);
    // });

    
    console.log(ratingMap);
    for(let i = 1; i < 6; i++) {
      console.log(ratingMap[i]);
      data.addRow([i.toString(), ratingMap[i]]);
    }
 
    const options = {
      'title': 'Store Ratings',
      'width':600,
      'height':500
    };
 
    const chart = new google.visualization.BarChart(
        document.getElementById('chart-container'));
    chart.draw(data, options);
    // });
}


// Adds comments to the page
function loadStoreInfo() 
{
  document.getElementById('title').innerHTML = localStorage.getItem("shopName");
  document.getElementById('address').innerHTML = localStorage.getItem("address");
  loadRatings();
}

// TODO: WRITE FUNCTION TO FETCH DRINKS AND SHOW THE NAME AND
function loadRatings() {
  var url = new URL('/comment', "https://" + window.location.hostname);
  var params = {store: localStorage.getItem("store")};
  console.log(localStorage.getItem("store"));
  url.search = new URLSearchParams(params).toString();
  fetch(url).then(response => response.json()).then((drinks) => {
    console.log(drinks)
    const ratingListElement = document.getElementById('comment-list');
    ratingListElement.innerHTML = "";
    initializeChart();

    drinks.forEach((drink) => 
    {
      ratingListElement.appendChild(createListElement(drink));
      addDrinkToChart(drink);
    })

    drawChart();
  });
}

function initializeChart() {
  for(let i = 1; i < 6; i++) {
    console.log(typeof(i));
    ratingMap[i] = 0;
  }
}

function addDrinkToChart(drink) {
  console.log(typeof(drink.rating));
  console.log(drink.rating)
  ratingMap[drink.rating]++;
}

/** Creates an <li> element containing text. */
function createListElement(drink) {
  console.log(drink);
  const ratingElement = document.createElement('li');
  ratingElement.className = 'drink';

  const drinkElement = document.createElement('span');
  drinkElement.innerText = drink.drink + ", " + drink.rating + "/5, " + drink.content;

  ratingElement.appendChild(drinkElement);
  return ratingElement;
}

function processComment() {
  fetch('/get-login-info').then(response => response.json()).then((isLoggedIn) => {
    if (!isLoggedIn) {
      window.alert("You're not logged in. Please log in to leave comment.");
      return;
    } else {
      fetch('/get-email').then(response => response.json()).then((email) => {
        storeComment(email);
        updateChart();
        drawChart();
      }).catch(error => {
        console.error('There has been a problem with get-email:', error);
      });
    }
  }).catch(error => {
    console.error('There has been a problem with get login info:', error);
  });
}

function storeComment(email) {
  var drink = document.getElementById("drink").value;
  var content = document.getElementById("content").value;
  var params = new URLSearchParams();
  params.append('drink', drink);
  var rating = document.getElementById("rating");
  params.append('rating', rating.options[rating.selectedIndex].value)
  params.append('content', content);
  params.append('store', localStorage.getItem("store"));
  params.append('email', email);
  fetch('/comment', {method: 'POST', body: params}).catch(e => {
    console.log(e)
  });
  const drinkObj = new Object();
  drinkObj.drink = drink.toLowerCase();
  drinkObj.rating = rating.options[rating.selectedIndex].value;
  drinkObj.content = content;
  const ratingListElement = document.getElementById('comment-list');
  ratingListElement.appendChild(createListElement(drinkObj));
  clearInput();
}

function updateChart() {
  var rating = document.getElementById("rating");
  ratingMap[rating.options[rating.selectedIndex].value]++;
}

function clearInput() {
  document.getElementById("drink").value = "";
  document.getElementById("content").value = "";
}