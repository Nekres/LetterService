/* 
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

function openDialogBox(recipient_id){
    var messagesBox = document.getElementById("messages").value;
    $.getJSON("msg.get?msgcount=5&receiver_id=" + recipient_id, function (msg) {
                            for(j = 0; j < msg.length;j++)
                            for(g = 0; g < msg.length-1;g++){
                                if(msg[g].id > msg[g+1].id){
                                    var temp = msg[g];
                                    msg[g] = msg[g+1];
                                    msg[g+1] = temp;
                                }
                            }
                            $("#messages").html("<p><kbd>msg.id " + msg[0].id + ': ' + msg[0].body + "</p></kbd>");
                            var g;
                            $("#messages").html('');
                            for (g = 1; g <= msg.length; g++) {
                                console.log(msg[g].id);
                                var str = "<p><kbd>msg.id " + msg[g].id + ': ' + msg[g].body + "</p></kbd>";
                                $("#messages").append(str);
                                console.log(str);
                            }
                            $("#messages").height($("#messages").height() +1);
                            $("#messages").height($("#messages").height() -1);
                        });
                        }