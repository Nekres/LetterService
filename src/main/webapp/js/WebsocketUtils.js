/* 
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

var socket = null;
var socketStatus = false;
function doConnect() {
    var protocol;
    var loc = window.location;
    if (loc.protocol === "https:") {  
                protocol = "wss:";  
            } else {  
                protocol = "ws:";  
            }  
    var uri = protocol + '//' + window.location.host + "/listener";
    if ('WebSocket' in window) {
        socket = new WebSocket(uri);
         console.log('Websocket connection status: opened.');
    } else if ('MozWebSocket' in window) {
        socket = new MozWebSocket(uri);
        console.log('Websocket connection status: opened.');
    } else {
        alert("websockets not suppored in ur browser");
    }
    socket.onopen = function () {
        socketStatus = true;
    };
    socket.onmessage = function (event) {
        var elem = document.getElementById("typing_status");
        var obj = JSON.parse(event.data);
        var id = parseInt($("#receiver_id").val());
        var targetId = obj.eventOwnerId;
        if(id == targetId){
            if(obj.eventType == "IS_TYPING" ){
            elem.innerHTML = "typing...";
        }
        else if(obj.eventType == "MESSAGE"){
            $.getJSON("msg.get?msgcount=1&receiver_id=" + id, function (message) {
                console.log('123');
                   var date = new Date(message.date);
                        var str = "<p>" + date.getHours() + ':' + date.getMinutes() + "<br>" + decodeURIComponent(message.body) + "</p>";
                        $("#general_page").append(str);
                       $("html, body").animate({ scrollTop: $(document).height()-$(window).height() });
            });
        }
        }
        
        setTimeout(clearStatus,1300);
    };
    socket.onclose = function (event) {
        console.log('Websocket connection status: closed.');
        socketStatus = false;
    };

}
function clearStatus(){
    var elem = document.getElementById("typing_status");
    elem.innerHTML = "";
}
function send() {
    var message = document.getElementById('mbody').value;
    socket.send();
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
    if(!socketStatus){
        doConnect();
        setTimeout(input,1);
    }else{
        socket.send(notify("IS_TYPING"));
    }
}
function notify(event_type){
    var id = parseInt($("#receiver_id").val());
    var json = JSON.stringify({
            eventOwnerId:0,
            eventType: event_type,
            targetId: [id]
        });
        return json;
}

function notifyMessage(){
    if(!socketStatus){
        doConnect();
        setTimeout(notifyMessage,1);
    }else{
        socket.send(notify("MESSAGE"));
    }
}

    