/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.mycompany.letterservice.auth;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.mycompany.letterservice.DatabaseManager;
import com.mycompany.letterservice.entity.Message;
import com.mycompany.letterservice.entity.User;
import java.io.IOException;
import java.io.Writer;
import java.util.Date;
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
@WebServlet(value = {"/msg.send", "/msg.get"})
public class MessagingServlet extends HttpServlet {

    public static final Logger logger = Logger.getLogger(MessagingServlet.class.getName());
    public static final String SEND_MESSAGE = "/msg.send";
    public static final String GET_MESSAGE = "/msg.get";
    
    public static final String RECEIVER_ID = "receiver_id";
    public static final String MSG_BODY = "m_body";
    public static final String CURR_U_ID = "curr_u_id";
    public static final String LAST_MESSAGES_COUNT = "msgcount";
    
    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        ObjectMapper mapper = new ObjectMapper();
        DatabaseManager manager = new DatabaseManager();
        Writer out = resp.getWriter();
        HttpSession session = req.getSession(false);
        String line = req.getParameter(LAST_MESSAGES_COUNT);
        manager.beginTransaction();
        
        if (req.getServletPath().equals(GET_MESSAGE)) {
            try {
                logger.info("msg.get request come with " + line);
                int count = Integer.parseInt(line);
                if (session == null) {
                    out.write(mapper.writeValueAsString(new com.mycompany.letterservice.entity.Error("You need to log in to access this api.")));
                    resp.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
                    return;
                }
                manager.getUserMessagesByUid(2, 5);
                
            } catch (NumberFormatException nfe) {
                resp.setStatus(HttpServletResponse.SC_BAD_REQUEST);
                out.write(mapper.writeValueAsString(new com.mycompany.letterservice.entity.Error("param must be integer")));
            }
        }
    }
    
    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        if (req.getServletPath().equals(SEND_MESSAGE)) {
            HttpSession session = req.getSession(false);
            if (session == null) {
                resp.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
                return;
            }
            
            int user_id = Integer.parseInt(session.getAttribute(CURR_U_ID).toString());
            String messageBody = req.getParameter(MSG_BODY);
            int receiverId = Integer.parseInt(req.getParameter(RECEIVER_ID));
            logger.info("\nHandle user with user_id: " + user_id + "and msg_body: " + messageBody + "\n + receiver_id :" + receiverId);
            DatabaseManager manager = new DatabaseManager();
            manager.beginTransaction();
            User currentUser = manager.getUserById(receiverId);
            Message m = new Message(messageBody);
            m.setDate(new Date());
            m.setSenderId(user_id);
            m.setReceiverId(receiverId);
            manager.persistObj(m);
            manager.updateObj(currentUser);
            logger.info(currentUser.toString());
            resp.setStatus(HttpServletResponse.SC_ACCEPTED);
            manager.commitAndClose();
        }
        
    }
    
}
