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
                    const img = document.createElement("img");
                    var reader = new FileReader();
                    reader.onload = function (e) {
                        img.src = e.target.result;
                    }
                    reader.readAsBinaryString(FileButtonEl.files[0])
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
    let filesLIEl = document.getElementById("FileList");
    let fileButtonEL = document.getElementById("fileButton");
    fileArray = Array.from(document.getElementById("fileButton").files);
    if (fileArray.length = !0) {
        for (let index = 0; index < fileButtonEL.files.length; index++) {
            const file = fileButtonEL.files[index];
            let ulEl = document.createElement("ul");
            let btnEL = document.createElement("button");
            btnEL.type = 'button';
            btnEL.innerHTML = "X";
            btnEL.addEventListener('click', (event) => {
                let eventParent = event.target.parentElement;
                let pointer = 0;
                for (let index = 0; index < filesLIEl.children.length; index++) {
                    if (filesLIEl.children[index].id == eventParent.id) {
                        pointer = index;
                        break;
                    }
                }
                filesLIEl.removeChild(filesLIEl.children[pointer]);
                let newFileList = new DataTransfer();
                for (let index = 0; index < fileButtonEL.files.length; index++) {
                    if (index != pointer) {
                        newFileList.items.add(fileButtonEL.files[index]);
                    }
                }
                fileButtonEL.files = newFileList.files;
                if (filesLIEl.children.length == 0) {
                    SelectedFiles.style.visibility = "hidden";
                }
                console.log(fileButtonEL.files);
            });
            ulEl.id = index;
            ulEl.innerHTML = file.name;
            ulEl.appendChild(btnEL);
            filesLIEl.appendChild(ulEl);
        }
    }
    document.getElementById('SelectedFiles').style.visibility = 'visible'
}

function setListSize() {
    let filesEl = document.getElementById("FileList");
    if (filesEl.children[0].style.display == 'block' || window.getComputedStyle(filesEl.children[0], null).display == 'block') {
        for (let index = 0; index < filesEl.children.length - 1; index++) {
            filesEl.children[index].style.display = 'none';
        }
    } else {
        for (let index = 0; index < filesEl.children.length; index++) {
            filesEl.children[index].style.display = 'block';
        }
    }
}