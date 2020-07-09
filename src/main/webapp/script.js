function determineLogin() {
  fetch('/get-login-info').then(response => response.json()).then((isLoggedIn) => {
    const element = document.getElementById('form');
    const textElement = document.createElement('span');
    const form = document.createElement('form');
    form.method='GET';
    form.action='/login';
    const button = document.createElement('button');
    if (!isLoggedIn) {
      textElement.innerText = "Please log in to view this page";
      button.innerHTML = "Login";
    } else {
      textElement.innerText = "You're logged in!";
      displayEmail();
      button.innerHTML = "Logout";
    }
    form.appendChild(button);
    element.appendChild(textElement);
    element.appendChild(form);
  }).catch(error => {
    console.error('There has been a problem with your operation:', error);
  });
}

function displayEmail() {
  fetch('/get-email').then(response => response.json()).then((EMAIL) => {
    document.getElementById('login-container').innerText = EMAIL;
  }).catch(error => {
    console.error('There has been a problem with your operation:', error);
  });
}

