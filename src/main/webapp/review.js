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

// Creates a chart and adds it to the page
function drawChart() {
    const data = new google.visualization.DataTable();
    data.addColumn('string', 'Rating');
    data.addColumn('number', 'Votes');
    data.addRows([
          ['Excellent', 33],
          ['Good', 19],
          ['Average', 10],
          ['Bad', 6],
          ['Terrible', 3]
        ]);

    const options = {
      'title': 'Store Ratings',
      'width':600,
      'height':500
    };

    const chart = new google.visualization.BarChart(
        document.getElementById('chart-container'));
    chart.draw(data, options);
}

// Adds comments to the page
function loadComments() 
{
  fetch('/make-comment').then(response => response.json()).then((tasks) => 
  {
    const taskListElement = document.getElementById('task-list');

    tasks.forEach((task) => 
    {
      taskListElement.appendChild(createTaskElement(task));
    })
    
  });
}

/** Creates an element that represents a task, including its delete button. */
function createTaskElement(task) 
{
    const taskElement = document.createElement('li');
    taskElement.className = 'task';

    const titleElement = document.createElement('span');
    titleElement.innerText = task.comment;

    const deleteButtonElement = document.createElement('button');
    deleteButtonElement.innerText = 'Delete';
    
    deleteButtonElement.addEventListener('click', () => 
    {
        deleteTask(task);

        // Remove the task from the DOM.
        taskElement.remove();
    });

    taskElement.appendChild(titleElement);
    taskElement.appendChild(deleteButtonElement);
    return taskElement;
}


/** Tells the server to delete the task. */
function deleteTask(task) {
  const params = new URLSearchParams();
  params.append('id', task.id);
  fetch('/delete-task', {method: 'POST', body: params});
}

/** Get comments and ratings from the database */
function loadRatings() {
  // Create url and params to avoid "request has method 'get' and cannot have a body" error
  var url = new URL('/comment', "https://" + window.location.hostname);
  var params = {store: localStorage.getItem("store")};
  console.log(localStorage.getItem("store"));
  url.search = new URLSearchParams(params).toString();
  fetch(url).then(response => response.json()).then((drinks) => {
    console.log(drinks)
    const ratingListElement = document.getElementById('comment-list');
    ratingListElement.innerHTML = "";
    drinks.forEach((drink) => {
      ratingListElement.appendChild(createListElement(drink));
    })
  });
}

function storeComment() {
  fetch('/get-login-info').then(response => response.json()).then((isLoggedIn) => {
    if (!isLoggedIn) {
      window.alert("You're not logged in. Please log in to leave comment.");
      return;
    } else {
      fetch('/get-email').then(response => response.json()).then((email) => {
        processComment(email);
      }).catch(error => {
        console.error('There has been a problem with get-email:', error);
      });
    }
  }).catch(error => {
    console.error('There has been a problem with get login info:', error);
  });
}

function processComment(email) {
  var drink = document.getElementById("drink").value;
  var content = document.getElementById("content").value;
  var params = new URLSearchParams();
  params.append('drink', drink);
  var rating = document.getElementById("rating");
  params.append('rating', rating.options[rating.selectedIndex].value)
  params.append('content', content);
  params.append('store', localStorage.getItem("store"));
  params.append('email', email);
  fetch('/comment', {method: 'POST', body: params}).catch(e => {console.log(e)});
}