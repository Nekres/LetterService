/* 
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */


function doConnect(){
    var socket = new WebSocket('ws://localhost:8084/LetterService/listener');
    socket.onopen = function(event) {
        alert();
    //socketStatus.innerHTML = 'Connected to: ' + event.currentTarget.url;
    //socketStatus.className = 'open';
    };
    socket.setReadyState();
//    socket.onmessage = function(event) {
//  alert("Получены данные " + event.data);
//    };
//    socket.send("Привет");
}
    