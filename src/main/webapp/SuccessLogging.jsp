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
            <h2>Hi <%=user %> ,login successful. Your session ID is <%=session.getId() %></h2>
            <form action="msg.send" method="POST">
                msg<input type="text" name="m_body" value="" size="20" />
                uid<input type="text" name="receiver_id" value="" />
                <input type="submit" value="Send" name="submit" />
            </form>
    </body>
</html>
