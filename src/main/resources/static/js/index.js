let socket;

document.addEventListener('DOMContentLoaded', event => {
    let el = document.getElementById('server_data').value;
    var qrcode = new QRCode("qrcode", {
        text: "https://kirje/" + el,
        colorDark: "#000000",
        colorLight: "#ffffff",
    }
    );
    console.log('Server Data: [' + el + ']');
    socket = new WebSocket("ws://" + el);
    // Connection opened
    socket.addEventListener("open", (event) => {
        console.log("Connected!");
    });

    // Listen for messages
    socket.addEventListener("message", (data) => {
        let message = JSON.parse(data.data)
        console.log("Message from: " + message.Origin + " : " + message);
        if (message.hasOwnProperty('Info')) {
            if (message.Info.includes('has connected!')) {
                document.getElementById('qrcode').style.display = 'none';
                document.getElementById('qrbtn').value = 'Show QR code';
                displayGeneralMessage(message.Info, true);
            } else if (message.Info.includes('Last Connection')) {
                document.getElementById('qrcode').style.display = 'inline-block';
                document.getElementById('qrbtn').value = 'Hide QR code';
            } else if (message.Info.includes('has left the room')) {
                displayGeneralMessage(message.Info, true);
                console.log("DISPLAY LEFT THE ROOM");
            }
        } else if (message.hasOwnProperty('Message')) {
            displayMessage(message, true)
            console.log("DISPLAY MESSAGE");
        }
    });
});

function displayFileNames() {
    let filesLIEl = document.getElementById("FileList");
    let fileButtonEL = document.getElementById("fileButton");
    fileArray = Array.from(document.getElementById("fileButton").files);
    if (fileArray.length = !0) {
        for (let index = 0; index < fileButtonEL.files.length; index++) {
            const file = fileButtonEL.files[index];
            let ulEl = document.createElement("ul");
            let btnEL = document.createElement("button");
            let pEL = document.createElement("p");
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
                    document.getElementById('SelectedFiles').style.display = 'none'
                } else {
                    setListSize()
                }
            });
            ulEl.id = index;
            pEL.innerHTML = file.name;
            ulEl.appendChild(pEL);
            ulEl.appendChild(btnEL);
            filesLIEl.appendChild(ulEl);
        }
    }
    document.getElementById('SelectedFiles').style.display = 'inline-block';
}


function QRManager() {
    let qrimg = document.getElementById('qrcode');
    let qrbtn = document.getElementById('qrbtn');
    if (qrimg.style.display == 'none') {
        qrimg.style.display = 'inline-block';
        qrbtn.value = 'Hide QR code'
    } else {
        qrimg.style.display = 'none';
        qrbtn.value = 'Show QR code';
    }
}



function displayGeneralMessage(message) {
    let generalMessage = document.createElement('p');
    generalMessage.innerHTML = message;
    generalMessage.classList.add('generalMessage');
    document.getElementById('chatSection').appendChild(generalMessage);
}

document.addEventListener("keyup", event => {
    if (event.shiftKey && event.key == 'Enter') {
        document.getElementById("inputText").append('\n');
    } else if (!event.shiftKey && event.key == 'Enter') {
        document.getElementById("sendMessageButton").click();
    }
}
)

function clearFileList() {
    document.getElementById('SelectedFiles').style.visibility = "hidden";
    document.getElementById("FileList").innerHTML = '';
    document.getElementById("fileButton").value = '';
}

// TODO: check correctness
function displayMessage(message, incoming) {
    const div = document.createElement("div");
    if (incoming) {
        div.className = "InMessage";
        let origin = document.createElement('p');
        origin.innerHTML = message.Origin;
        div.appendChild(origin);
        if (message.Message.hasOwnProperty('Files')) {
            for (const file of message.Files) {
                const value = Object.values(file)[0];
                switch (Object.keys(file)[0]) {
                    case 'png':
                    case 'img':
                    case 'jpg':
                        let img = document.createElement("img");
                        img.src = 'data:image/png;base64,' + value;
                        div.appendChild(img);
                        break;
                    case 'vid':
                    case 'mp4':
                        let vid = document.createElement('video');
                        const ojbURL = URL.createObjectURL(value);
                        vid.src = ojbURL;
                        vid.controls = true;
                        div.appendChild(vid);
                        URL.revokeObjectURL(value);
                        break;
                    default:
                        break;
                }
            }
        }
        if (message.Message.hasOwnProperty('Text') && message.Message.Text != '') {
            const par = document.createElement("p");
            par.innerHTML = message.Message.Text;
            div.appendChild(par);
        }
    } else {
        div.className = 'OutMessage';
        if (message.hasOwnProperty('Files')) {
            for (const file of message.Files) {
                const value = Object.values(file)[0];
                switch (Object.keys(file)[0]) {
                    case 'png':
                    case 'img':
                    case 'jpg':
                        let img = document.createElement("img");
                        img.src = value;
                        div.appendChild(img);
                        break;
                    case 'vid':
                    case 'mp4':
                        let vid = document.createElement('video');
                        const ojbURL = URL.createObjectURL(value);
                        vid.src = ojbURL;
                        vid.controls = true;
                        div.appendChild(vid);
                        URL.revokeObjectURL(value);
                        break;
                    default:
                        break;
                }
            }
        }
        if (message.hasOwnProperty('Text') && message.Text != '') {
            const par = document.createElement("p");
            par.innerHTML = message.Text;
            div.appendChild(par);
        }
    }
    document.getElementById('chatSection').appendChild(div);
}

function setListSize() {
    let filesEl = document.getElementById("FileList");
    if (filesEl.children[0].style.display == 'flex' || window.getComputedStyle(filesEl.children[0], null).display == 'flex') {//collapses
        for (let index = 0; index < filesEl.children.length - 1; index++) {
            filesEl.children[index].style.display = 'none';
        }
        document.getElementById("FileListExpansionBtn").innerHTML = "Expand"
    } else {//Expands
        for (let index = 0; index < filesEl.children.length; index++) {
            filesEl.children[index].style.display = 'flex';
        }
        document.getElementById("FileListExpansionBtn").innerHTML = "Collapse"
    }
}

const getBase64 = file => new Promise((resolve, reject) => {
    const reader = new FileReader();
    reader.readAsDataURL(file);
    reader.onload = () => resolve(reader.result);
    reader.onerror = reject;
});


async function sendMessage(event) {
    var fileButtonEl = document.getElementById("fileButton");
    let inputTextEl = document.getElementById("inputText");
    if (inputTextEl.value != '' || fileButtonEL.files.length != 0) {
        let data = {};
        if (fileButtonEl.files.length != 0) {
            let Files = [];
            for (let index = 0; index < fileButtonEl.files.length; index++) {
                let file = {};
                file[fileButtonEl.files[index].name.split('.')[1]] = await getBase64(fileButtonEl.files[index]);
                Files.push(file);
            }
            data["Files"] = Files;
        }
        if (inputTextEl.value != '') { data["Text"] = inputTextEl.value; }

        socket.send(JSON.stringify(data));
        inputTextEl.value = '';
        clearFileList();
        displayMessage(data, false);
    }

    // if (FileButtonEl.value != '' || inputTextEl.value != '') {
    //     socket.send(toString(inputTextEl));
    // fetch('/api/submit', {
    //         method: 'POST',
    //         body: formData
    //     })
    //         .then(response => response.text())
    //         .then(data => {
    //             console.log('Response from server:', data);

    //             const div = document.createElement("div");
    //             div.className = "OutMessage";
    //             console.log(FileButtonEl.files.length);
    //             if (FileButtonEl.files.length != 0) {
    //                 for (let index = 0; index < FileButtonEl.files.length; index++) {
    //                     const file = FileButtonEl.files[index];
    //                     const fileFormat = file.name.split('.')[1];
    //                     switch (fileFormat) {
    //                         case 'png':
    //                         case 'jpg':
    //                             let img = document.createElement("img");
    //                             img.file = file;
    //                             div.appendChild(img);
    //                             const reader = new FileReader();
    //                             reader.onload = (e) => {
    //                                 img.src = e.target.result;
    //                             };
    //                             reader.readAsDataURL(file);
    //                             break;
    //                         case 'mp4':
    //                             let vid = document.createElement('video');
    //                             const ojbURL = URL.createObjectURL(file);
    //                             vid.src = ojbURL;
    //                             vid.controls = true;
    //                             div.appendChild(vid);
    //                             URL.revokeObjectURL(file);
    //                             break;
    //                         default:
    //                             break;
    //                     }
    //                     clearFileList();
    //                 }
    //             }
    //             if (inputTextEl.value != '') {
    //                 const par = document.createElement("p");
    //                 par.innerHTML = inputTextEl.value;
    //                 div.appendChild(par);
    //             }
    //             document.getElementById('chatSection').appendChild(div);

    //             FileButtonEl.value = '';
    //             inputTextEl.value = '';
    //         })
    //         .catch(error => {
    //             console.error('Error:', error);
    //         });
    // }
    // }
}
