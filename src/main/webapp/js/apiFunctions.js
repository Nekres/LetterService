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
$(function () {
                $(document).on('change', ':file', function () {
                    var input = $(this),
                            numFiles = input.get(0).files ? input.get(0).files.length : 1,
                            label = input.val().replace(/\\/g, '/').replace(/.*\//, '');
                    input.trigger('fileselect', [numFiles, label]);
                });

                $(document).ready(function () {
                    $(':file').on('fileselect', function (event, numFiles, label) {

                        var input = $(this).parents('.input-group').find(':text'),
                                log = numFiles > 1 ? numFiles + ' files selected' : label;

                        if (input.length) {
                            input.val(log);
                        } else {
                            if (log)
                                alert(log);
                        }

                    });
                });

            });