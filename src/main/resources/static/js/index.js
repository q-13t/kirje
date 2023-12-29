let socket;
document.addEventListener('DOMContentLoaded', event => {
    let el = document.getElementById('server_data').value;
    new QRCode("qrcode", {
        text: "https://kirje/" + el,
        colorDark: "#000000",
        colorLight: "#ffffff",
    });
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
        } else {
            displayMessage(message, true)
            console.log("DISPLAY MESSAGE");
        }
    });
});

function displayFileNames() {
    let filesLIEl = document.getElementById("FileList");
    let fileButtonEL = document.getElementById("fileButton");
    let files = document.getElementById('SelectedFiles');
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
                    files.style.display = 'none'
                }
                files.scroll({ top: files.scrollHeight, behavior: 'smooth' });
            });
            ulEl.id = index;
            pEL.innerHTML = file.name;
            ulEl.appendChild(pEL);
            ulEl.appendChild(btnEL);
            filesLIEl.appendChild(ulEl);
        }
    }
    files.style.display = 'inline-block';
    files.scroll({ top: files.scrollHeight, behavior: 'smooth' });
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
    // document.getElementById('SelectedFiles').style.visibility = "hidden";
    document.getElementById('SelectedFiles').style.display = 'none'
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
    } else {
        div.className = 'OutMessage';
    }
    if (message.hasOwnProperty('Files')) {
        for (const file of message.Files) {
            const value = Object.values(file)[0];
            switch (Object.keys(file)[0]) {
                case 'png':
                case 'jpeg':
                case 'jpg':
                    let img = document.createElement("img");
                    console.log(value.includes("base64,"));
                    if (!value.includes("base64,")) {
                        img.src = "data:image/jpg;base64," + value;
                    } else {
                        img.src = value;
                    }
                    div.appendChild(img);
                    break;
                case 'mp4':
                    let vid = document.createElement('video');
                    if (!value.includes('base64,')) {
                        vid.src = "data:video/mp4;base64," + value;
                    } else {
                        vid.src = value;
                    }
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
    let chat = document.getElementById('chatSection');
    chat.appendChild(div);
    chat.scroll({ top: chat.scrollHeight, behavior: 'smooth' });
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
    filesEl.scroll({ top: filesEl.scrollHeight, behavior: 'smooth' });
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
    if (inputTextEl.value != '' || fileButtonEl.value != '') {
        let data = {};
        if (fileButtonEl.files.length != 0) {
            let Files = [];
            for (let index = 0; index < fileButtonEl.files.length; index++) {
                let file = {};
                let type = fileButtonEl.files[index].name.split('.');
                file[type[type.length - 1]] = await getBase64(fileButtonEl.files[index]);
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
}

