<%-- 
    Document   : SuccessLogging
    Created on : Aug 22, 2017, 9:52:18 PM
    Author     : nekres
--%>

<%@page contentType="text/html" pageEncoding="UTF-8"%>
<!DOCTYPE html>
<html>
    <head>
        <meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
        <link rel="stylesheet" href="https://maxcdn.bootstrapcdn.com/bootstrap/3.3.7/css/bootstrap.min.css">
        <script src="https://ajax.googleapis.com/ajax/libs/jquery/3.2.1/jquery.min.js"></script>
        <script src="https://maxcdn.bootstrapcdn.com/bootstrap/3.3.7/js/bootstrap.min.js"></script>
        <link rel="stylesheet" type="text/css" href="/css/styles.css">
        <script type = "text/javascript" 
         src = "https://ajax.googleapis.com/ajax/libs/jquery/2.1.3/jquery.min.js"></script>
         <script type = "text/javascript" src = "/jquery/custom.js"></script>
        <title>Success</title>
    </head>
    <body>
        <%
            String user = null;
            if(session.getAttribute("user") == null){
               response.sendRedirect("index.html");
            }else{
                user = (String)session.getAttribute("user");
            }
            %>
            <div class="container">
            <h2>Hi <%=user %>,login successful. Your session ID is <%=session.getId() %></h2>
            <br>
            <div class="col-md-6" id="userlist"> <p align="center">Common account list</p></div>
                <div id="msgwrap" class="col-md-6">
            <div  id="messages"overflow: scroll;
                  overflow-x:hidden;overflow-y:scroll;border-color: blueviolet;border: 1px solid black; word-wrap:break-word" ></div>
                      <form action="msg.send" method="POST">
                msg<input type="text" name="m_body" value="" size="20" />
                uid<input type="text" name="receiver_id" value="" />
                <input type="submit" value="Send" name="submit" class="" />
            </form>
            </div>
                <script type="text/javascript" language="javascript">
                $(document).ready(function (){
                    $.getJSON("msg.get?msgcount=5",function (msg){
                        $("#messages").html("<p>msg.id " + msg[0].id + ': ' + msg[0].body + "</p>"); 
                        var i;
                        for(i = 1; i <= msg.length;i++){
                       $("#messages").append("<p>msg.id " + msg[i].id + ': ' + msg[i].body + "</p>");
                   }
                    });
                    
                })
                </script>
                <script type="text/javascript" language="javascript">
                    $(document).ready(function (){
                        $("#loadUsers").ready(function (){
                           $.getJSON("users.all?",function (user){
                        var i;
                        for(i = 0; i < user.length;i++){
                            var input = document.createElement("input");
                            input.type = "button";
                            input.value = user[i].name + " with id: " + user[i].id;
                            input.setAttribute("class","btn btn-primary btn-block");
                        $("#userlist").append(input);
                        }
                    });
                    });
                });
                </script>
            </div>
    </body>
</html>
