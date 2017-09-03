/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.mycompany.letterservice.auth;

import com.mycompany.letterservice.DatabaseManager;
import com.mycompany.letterservice.entity.User;
import java.io.IOException;
import java.util.logging.Logger;
import javax.servlet.RequestDispatcher;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

/**
 *
 * @author nekres
 */
@WebServlet(value = {"/msg.send","/msg.get"})
public class MessagingServlet extends HttpServlet{
    public static final Logger logger = Logger.getLogger(MessagingServlet.class.getName());
    public static final String SEND_MESSAGE = "/msg.send";
    public static final String GET_MESSAGE = "/msq.get";
    
    public static final String RECEIVER_ID = "receiver_id";
    public static final String MSG_BODY = "m_body";
    public static final String CURR_U_ID = "curr_u_id";

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
    }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        if(req.getServletPath().equals(SEND_MESSAGE)){
            HttpSession session = req.getSession();
            if(session == null){
                resp.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
            }
            
            int user_id = Integer.parseInt(session.getAttribute(CURR_U_ID).toString());
            String messageBody = req.getParameter(MSG_BODY);
            int receiverId = Integer.parseInt(req.getParameter(RECEIVER_ID));
            logger.info("\nHandle user with user_id: " + user_id + "and msg_body: " + messageBody + "\n + receiver_id :" + receiverId);
            DatabaseManager manager = new DatabaseManager();
            manager.beginTransaction();
            
            User currentUser = manager.getUserById(receiverId);
            logger.info(currentUser.toString());
            resp.setStatus(HttpServletResponse.SC_ACCEPTED);
        }
        
    }
    
    
    
    
}
