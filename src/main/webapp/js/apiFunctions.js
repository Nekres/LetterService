/* 
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */


function registrationCallback(){
    $(document).ready(function (){
        $("#register_form").ajaxForm({
            url : 'register',
            dataType : 'json',
            success: function (response){
                alert("The server says " + response);
            }
        });
    });
}
function unathorizedCheck(){
    $(document).ready(function(){
        var request = $.ajax({
            url:'login',
            data: 'session=' + getCookie("session_id")
        });
        request.fail(function(jqXHR, textStatus) {
            window.location.replace("index.html");
        });
    });
}