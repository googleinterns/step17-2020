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

/** Creates a chart and adds it to the page */
function drawChart() {
  const data = new google.visualization.DataTable();
  data.addColumn('string', 'Rating');
  data.addColumn('number', 'Votes');

  for(let i = 1; i < 6; i++) {
    data.addRow([i.toString(), ratingMap[i]]);
  }

  const options = {
    'title': 'Store Ratings',
    'width':500,
    'height':500
  };

  const chart = new google.visualization.BarChart(
    document.getElementById('chart-container'));
  chart.draw(data, options);
}


/** Adds store information to the page */
function loadStoreInfo() {
  document.getElementById('title').innerHTML = localStorage.getItem("shopName");
  document.getElementById('address').innerHTML = localStorage.getItem("address");
  loadRatings();
}

function loadRatings() {
  var url = new URL('/comment', "https://" + window.location.hostname);
  var params = {store: localStorage.getItem("store")};
  url.search = new URLSearchParams(params).toString();
  fetch(url).then(response => response.json()).then((drinks) => {
    const ratingListElement = document.getElementById('comment-list');
    ratingListElement.innerHTML = "";
    initializeChart();

    drinks.forEach((drink) => {
      ratingListElement.appendChild(createListElement(drink));
      addDrinkToChart(drink);
    })

    drawChart();
  });
}

/** Initialize all rows on the chart to 0 */
function initializeChart() {
  for(let i = 1; i < 6; i++) {
    ratingMap[i] = 0;
  }
}

/** Add to the appropriate row on the chart with given dirnk*/
function addDrinkToChart(drink) {
  ratingMap[drink.rating]++;
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
        location.reload();
      }).catch(error => {
        console.error('There has been a problem with get-email:', error);
      });
    }
  }).catch(error => {
    console.error('There has been a problem with get login info:', error);
  });
}

/** Send new comment to backend to store in database. */
function storeComment(email) {
  var drink = document.getElementById("drink").value;
  var content = document.getElementById("content").value;
  var storeInfo= localStorage.getItem("shopName") + " " + localStorage.getItem("address");
  var params = new URLSearchParams();
  params.append('drink', drink);
  var rating = document.getElementById("rating");
  params.append('rating', rating.options[rating.selectedIndex].value)
  params.append('content', content);
  params.append('store', localStorage.getItem("store"));
  params.append('email', email);
  params.append('storeInfo', storeInfo);
  fetch('/comment', {method: 'POST', body: params}).catch(e => {
    console.log(e)
  });
  window.location.reload();
}

/** Add to the appropriate row on the chart when a new comment is received */
function updateChart() {
  var rating = document.getElementById("rating");
  ratingMap[rating.options[rating.selectedIndex].value]++;
}

