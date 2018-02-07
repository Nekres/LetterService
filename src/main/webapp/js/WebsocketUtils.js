/* 
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

var socket = null;
var socketStatus = false;
function doConnect() {
    var uri = 'ws://localhost:' + window.location.port + "/listener";
    if ('WebSocket' in window) {
        socket = new WebSocket(uri);
    } else if ('MozWebSocket' in window) {
        socket = new MozWebSocket(uri);
    } else {
        alert("websockets not suppored in ur browser");
    }
    alert("success");
    socket.onopen = function () {
        socketStatus = true;
        socket.send("jsk");
    };
    socket.onmessage = function (event) {
        var elem = document.getElementById("typing_status");
        elem.innerHTML = "typing...";
        setTimeout(clearStatus,1000);
    };
    socket.onclose = function (event) {
        setConnected(false);
        socketStatus = false;
    };

}
function clearStatus(){
    var elem = document.getElementById("typing_status");
    elem.innerHTML = "";
}
function send() {
    var message = document.getElementById('mbody').value;
    socket.send(message);
    var today = new Date();
}
function updateTarget(target) {
    if (window.location.protocol == 'http:') {
        document.getElementById('target').value = 'ws://' + window.location.host + target;
    } else {
        document.getElementById('target').value = 'wss://' + window.location.host + target;
    }
}
function input() {
    socket.send('');
}

    