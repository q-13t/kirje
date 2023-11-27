

function sendMessage(event) {
    // Get form data
    const formData = new FormData(document.getElementById('inputForm'));

    // Send form data to the server
    fetch('/api/submit', {
        method: 'POST',
        body: formData
    })
        .then(response => response.text())
        .then(data => {
            document.getElementById("fileButton").value = '';
            document.getElementById("inputText").value = '';

            console.log('Response from server:', data);
            const div = document.createElement("div");
            div.className = "OutMessage";
            const par = document.createElement("p");
            par.innerHTML = data;
            div.appendChild(par);
            document.getElementById('chatSection').appendChild(div);
        })
        .catch(error => {
            console.error('Error:', error);
        });

    // 
}
var socket = new SockJS('/gs-guide-websocket');
var stompClient = Stomp.over(socket);

stompClient.connect({}, function (frame) {
    console.log('Connected: ' + frame);
    stompClient.subscribe('/topic/greetings', function (greeting) {
        var message = JSON.parse(greeting.body).content;
        console.log('Received message: ' + message);
        // Handle the message as needed
    });
});