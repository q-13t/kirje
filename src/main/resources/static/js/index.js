

function sendMessage(event) {
    // Get form data
    const formData = new FormData(document.getElementById('inputForm'));
    var FileButtonEl = document.getElementById("fileButton");
    var inputTextEl = document.getElementById("inputText");

    if (FileButtonEl.value != '' || inputTextEl.value != '') {
        // Send form data to the server
        fetch('/api/submit', {
            method: 'POST',
            body: formData
        })
            .then(response => response.text())
            .then(data => {
                console.log('Response from server:', data);

                const div = document.createElement("div");
                div.className = "OutMessage";

                if (FileButtonEl.value != '') {
                    const reader = new FileReader();
                    const img = document.createElement("img");
                    reader.onload = () => {
                        img.src = reader.result;
                    }
                    // reader.readAsDataURL();
                    div.appendChild(img);
                    ClearFileSelection();
                }

                if (inputTextEl.value != '') {
                    const par = document.createElement("p");
                    par.innerHTML = inputTextEl.value;
                    div.appendChild(par);
                }

                document.getElementById('chatSection').appendChild(div);

                FileButtonEl.value = '';
                inputTextEl.value = '';
            })
            .catch(error => {
                console.error('Error:', error);
            });
    }
}
// var socket = new SockJS('/gs-guide-websocket');
// var stompClient = Stomp.over(socket);

// stompClient.connect({}, function (frame) {
//     console.log('Connected: ' + frame);
//     stompClient.subscribe('/topic/greetings', function (greeting) {
//         var message = JSON.parse(greeting.body).content;
//         console.log('Received message: ' + message);
//         // Handle the message as needed
//     });
// });
function displayFileNames() {
    let SelectedFileEl = document.getElementById("SelectedFile");
    SelectedFileEl.innerHTML = document.getElementById("fileButton").value;
    SelectedFileEl.style.visibility = "visible";

}
function ClearFileSelection() {
    let SelectedFileEl = document.getElementById("SelectedFile");
    SelectedFileEl.innerHTML = '';
    SelectedFileEl.style.visibility = "hidden";
    document.getElementById("fileButton").value = '';
}