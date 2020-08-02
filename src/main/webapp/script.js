/*
    These functions are strictly being used for the user's profile
    to ensure the associated store is being displayed rather than the user's
    email next to each review
*/
function determineLoginUser() {
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
      displayEmailUser();
      button.innerHTML = "Logout";
    }
    form.appendChild(button);
    element.appendChild(textElement);
    element.appendChild(form);
  }).catch(error => {
    console.error('There has been a problem with your operation:', error);
  });
}

function displayEmailUser() {
  fetch('/get-email').then(response => response.json()).then((email) => {
    document.getElementById('email').innerText = email;
   loadCommentsUser(email);
  }).catch(error => {
    console.error('There has been a problem with your operation:', error);
  });
}

function loadCommentsUser(email) {
  var url = new URL('/comment', "https://" + window.location.hostname);
  var params = {email: email};
  url.search = new URLSearchParams(params).toString();
  fetch(url).then(response => response.json()).then((drinks) => {
    const ratingListElement = document.getElementById('list');

    drinks.forEach((drink) => 
    {
      ratingListElement.appendChild(createListElementUser(drink));
    })

  });
}

/** Creates an <li> element containing text as "store, drink, rating, and content". */
function createListElementUser(drink) {
  const ratingElement = document.createElement('li');
  ratingElement.className = 'drink';

  const drinkElement = document.createElement('span');
  var store = idToStore(drink.store)
  drinkElement.innerText = store +", "+drink.drink + ", " + drink.rating + "/5, " + drink.content;

  ratingElement.appendChild(drinkElement);
  return ratingElement;
}
/**This function turns the store id into a readable store */
function idToStore(storeId){
    //todo
}

//end of user's profile functions

/*
    These functions will strictly be used for storepages 
    to display each user's email alongside their own comment
    rather than the store
*/
/**This function determines the login status of the user */
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
/**This function displays the current user's email then calls the store's load comments function */
function displayEmail() {
  fetch('/get-email').then(response => response.json()).then((email) => {
    document.getElementById('email').innerText = email;
   loadComments(email);
  }).catch(error => {
    console.error('There has been a problem with your operation:', error);
  });
}

/**This function loads the store's comments */
function loadComments(email) { 
  var url = new URL('/comment', "https://" + window.location.hostname);
  var params = {email: email};
  url.search = new URLSearchParams(params).toString();
  fetch(url).then(response => response.json()).then((drinks) => {
    const ratingListElement = document.getElementById('list');

    drinks.forEach((drink) => 
    {
      ratingListElement.appendChild(createListElement(drink));
    })

  });
}

/** Creates an <li> element containing text as "email, drink, rating, and content". */
function createListElement(drink) {
  const ratingElement = document.createElement('li');
  ratingElement.className = 'drink';

  const drinkElement = document.createElement('span');
  drinkElement.innerText = drink.email +", "+drink.drink + ", " + drink.rating + "/5, " + drink.content;

  ratingElement.appendChild(drinkElement);
  return ratingElement;
}




//The functions below will be used for username feature
function requestUsername() {//todo: check to see if this email associated with the user already has a username first
  var email = document.getElementById("userEmail").value;
  var username = document.getElementById("username").value;
  var params = new URLSearchParams();
  params.append('username', username);
  params.append('email', email);
  fetch('/create-username', {method: 'POST', body: params}).catch(e => {console.log(e)});

}
function displayUsername() {
  fetch('/get-username').then(response => response.json()).then((username) => {
    document.getElementById('username-container').innerText = username;
  }).catch(error => {
    console.error('There has been a problem with your operation:', error);
  });
}

function getBeverage() {
    var beverage = document.getElementById("beverageRequested").value;
    var params = new URLSearchParams();
    params.append('beverageRequested', beverage);
    fetch('/recommend', {method: 'POST', body: params}).catch(e => {console.log(e)});

}
