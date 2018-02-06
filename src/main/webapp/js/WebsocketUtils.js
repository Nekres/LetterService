/* 
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

var socket = null;
function doConnect(){
    var uri = 'ws://' + window.location.host + "/letter/listener";
    alert(uri);
    
      if ('WebSocket' in window) {
                socket = new WebSocket(uri);
            } else if ('MozWebSocket' in window) {
                socket = new MozWebSocket(uri);
            }else{
                alert("websockets not suppored in ur browser");
            }
    socket.onopen = function () {
                socket.send("jsk");
            };
            socket.onmessage = function (event) {
                alert("Received message:" + event.data);
            };
            socket.onclose = function (event) {
                setConnected(false);
            };
            
}
function updateTarget(target) {
            if (window.location.protocol == 'http:') {
                document.getElementById('target').value = 'ws://' + window.location.host + target;
            } else {
                document.getElementById('target').value = 'wss://' + window.location.host + target;
            }
        }

    