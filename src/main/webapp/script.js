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