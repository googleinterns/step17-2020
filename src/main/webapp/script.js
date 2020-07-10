function determineLogin() {
  fetch('/get-login-info').then(response => response.json()).then((isLoggedIn) => {
    const element = document.getElementById('form');
    const textElement = document.createElement('span');
    const form = document.createElement('form');
    form.method='GET';
    form.action='/login';
    const button = document.createElement('button');
    if (!isLoggedIn) {
      button.innerHTML = "Login";
    } else {
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
    loadComments(EMAIL);
  }).catch(error => {
    console.error('There has been a problem with your operation:', error);
  });
}

function loadComments(email) {
  var url = new URL('/comment', "https://" + window.location.hostname);
  var params = {email: email};
  url.search = new URLSearchParams(params).toString();
  fetch(url).then(response => response.json()).then((drinks) => {
    const ratingListElement = document.getElementById('list');
    ratingListElement.innerHTML = "";

    drinks.forEach((drink) => 
    {
      ratingListElement.appendChild(createListElement(drink));
    })

  });
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

