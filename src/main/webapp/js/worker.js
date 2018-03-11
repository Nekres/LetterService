/* 
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

function timedCount(){
    console.log("pinging");
    postMessage('onmessage');
    setTimeout("timedCount()",1000 * 50);
}

timedCount();