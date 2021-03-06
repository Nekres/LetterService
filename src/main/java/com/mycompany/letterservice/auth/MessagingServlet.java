/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.mycompany.letterservice.auth;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.mycompany.letterservice.DatabaseManager;
import com.mycompany.letterservice.entity.Message;
import com.mycompany.letterservice.entity.Status;
import com.mycompany.letterservice.entity.User;
import java.io.*;
import java.io.Writer;
import java.net.URLDecoder;
import java.util.*;
import org.apache.log4j.Logger;
import javax.servlet.*;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.*;
import org.hibernate.SessionFactory;

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
        req.setCharacterEncoding("UTF-8");
        resp.setCharacterEncoding("UTF-8");
        ObjectMapper mapper = new ObjectMapper();

        ServletContext context = req.getServletContext();
        SessionFactory sessionFactory = (SessionFactory) context.getAttribute("sessionFactory");

        DatabaseManager manager = new DatabaseManager(sessionFactory);
        Writer out = resp.getWriter();
        HttpSession session = req.getSession(false);
        String line = req.getParameter(LAST_MESSAGES_COUNT);
        if (line == null || line.isEmpty()) {
            out.write(mapper.writeValueAsString(new com.mycompany.letterservice.entity.Status("wrong params")));
            return;
        }
        if (session == null) {
            out.write(mapper.writeValueAsString(new com.mycompany.letterservice.entity.Status("You need to log in to access this api.")));
            resp.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
            return;
        }

        if (req.getServletPath().equals(GET_MESSAGE)) {
            String rec_id = req.getParameter(RECEIVER_ID);
            try {
                logger.info("msg.get request come with " + line + "rec_id" + rec_id);
                
                int count = Integer.parseInt(line);
                String user_id = session.getAttribute("curr_u_id").toString();
                manager.beginTransaction();
                if(count > 1){
                List<Message> messages = manager.getUserMessagesByUid(Integer.parseInt(user_id), Integer.parseInt(rec_id), count);
                if (messages.isEmpty()) {
                    out.write(mapper.writeValueAsString(new com.mycompany.letterservice.entity.Status("user have no messages yet.")));
                } else {
                    logger.info("LOAD MESSAGES: " + messages.toString());
                    out.write(mapper.writeValueAsString(messages));
                }
                
                }else{
                    logger.info("else");
                    Message message = manager.getUserMessageById(Integer.parseInt(user_id), Integer.parseInt(rec_id));
                    if(message == null){
                        out.write(mapper.writeValueAsString(new Status("user have no messages yes")));
                        logger.info("No messages");
                    }
                    else{
                        out.write(mapper.writeValueAsString(message));
                        logger.info(message.toString());
                    }
                }
            } catch (NumberFormatException nfe) {
                resp.setStatus(HttpServletResponse.SC_BAD_REQUEST);
                out.write(mapper.writeValueAsString(new com.mycompany.letterservice.entity.Status("")));
            } finally {
                manager.closeSession();
            }
        }
    }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        req.setCharacterEncoding("UTF-8");
        if (req.getServletPath().equals(SEND_MESSAGE)) {
            HttpSession session = req.getSession(false);
            if (session == null) {
                resp.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
                return;
            }

            int user_id = Integer.parseInt(session.getAttribute(CURR_U_ID).toString());
            String messageBody = URLDecoder.decode(req.getParameter(MSG_BODY), "UTF-8");
            int receiverId = 0;
            try {
                receiverId = Integer.parseInt(req.getParameter(RECEIVER_ID));
            } catch (NumberFormatException ex) {
                resp.setStatus(HttpServletResponse.SC_BAD_REQUEST);
                PrintWriter out = resp.getWriter();
                ObjectMapper mapper = new ObjectMapper();
                out.write(mapper.writeValueAsString(new com.mycompany.letterservice.entity.Status("You have to specify 'receiver_id'.")));
            }

            logger.info("\nMessage send: user_id: " + user_id + "and msg_body: " + messageBody + "\n + receiver_id :" + receiverId);

            ServletContext context = req.getServletContext();
            SessionFactory sessionFactory = (SessionFactory) context.getAttribute("sessionFactory");
            DatabaseManager manager = new DatabaseManager(sessionFactory);

            manager.beginTransaction();
            User currentUser = manager.getUserById(receiverId);
            Message m = new Message(user_id, receiverId, new Date(), messageBody);
            manager.persistObj(m);
            logger.info(currentUser.toString());
            resp.setStatus(HttpServletResponse.SC_ACCEPTED);
            manager.commitAndClose();
        }

    }

}
