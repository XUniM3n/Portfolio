'use strict';


let regStatusAlert = document.querySelector('#reg-status-alert');
let errorAlert = document.querySelector('#error-alert');
let usernameForm = document.querySelector('#username');
let passwordForm = document.querySelector('#password');
let loginForm = document.querySelector('#login-form');

let stompClient = null;
let username = null;


function connect() {
    username = document.querySelector('#username').innerText.trim();

    let socket = new SockJS('/ws');
    stompClient = Stomp.over(socket);

    stompClient.connect({}, onConnected, onError);
}

// Connect to WebSocket Server.
connect();

function onConnected() {
    // Subscribe to the Public Topic
    stompClient.subscribe('/user/queue/signup', onMessageReceived);
}


function onError(error) {
    errorAlert.textContent = 'Could not connect to WebSocket server. Please refresh this page to try again!';
    errorAlert.style.display = 'block';
}


function signup(event) {
    regStatusAlert.classList.add('alert-info');
    regStatusAlert.innerHTML = 'Registering and downloading cat for you...';
    regStatusAlert.style.diplay = 'block';

    if (stompClient) {
        let regForm = {
            username: usernameForm.value,
            password: passwordForm.value
        };
        stompClient.send("/app/signup.signup", {}, JSON.stringify(regForm));
    }
    event.preventDefault();
}


function onMessageReceived(payload) {
    let message = JSON.parse(payload.body);

    regStatusAlert.classList.remove('alert-info');
    regStatusAlert.classList.add('alert-success');
    regStatusAlert.innerHTML = message.content;
    regStatusAlert.style.display = 'block';
}


loginForm.addEventListener('submit', signup, true);