/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.mycompany.letterservice.auth;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.mycompany.letterservice.DatabaseManager;
import com.mycompany.letterservice.entity.User;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.List;
import javax.servlet.ServletContext;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import org.hibernate.SessionFactory;
import org.apache.log4j.Logger;

/**
 *
 * @author nekres
 */
@WebServlet(value = {UserManagerServlet.USERS_GET, UserManagerServlet.USERS_ALL, UserManagerServlet.SUBSCRIBE})
public class UserManagerServlet extends HttpServlet {
    private static final Logger logger = Logger.getLogger(UserManagerServlet.class.getName());
    
    public static final String USERS_GET = "/users.get";
    public static final String USERS_ALL = "/users.all";
    public static final String SUBSCRIBE = "/users.subscribe";
    public static final String SUBSCRIBER = "sub";

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        req.setCharacterEncoding("UTF-8");
        resp.setCharacterEncoding("UTF-8");
        ObjectMapper mapper = new ObjectMapper();
        ServletContext context = req.getServletContext();
        SessionFactory sessionFactory = (SessionFactory) context.getAttribute("sessionFactory");

        DatabaseManager manager = new DatabaseManager(sessionFactory);
        PrintWriter out = resp.getWriter();
        manager.beginTransaction();
        if (req.getServletPath().equals(USERS_GET)) {
            String id = req.getParameter("id");
            if (id == null) {
                manager.commitAndClose();
                out.write(mapper.writeValueAsString(new com.mycompany.letterservice.entity.Status("null")));
                return;
            }
            User user = manager.getUserById(Integer.parseInt(id));
            String json = mapper.writeValueAsString(user);
            out.write(json);
        } else if (req.getServletPath().equals(USERS_ALL)) {
            List<User> userList = manager.getAllUsers();
            String json = mapper.writeValueAsString(userList);
            out.write(json);
        }
        manager.commitAndClose();
    }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        logger.info("/subscribe");
        HttpSession session = req.getSession(false);
        ServletContext context = req.getServletContext();
        String subscribeTarget = req.getParameter(SUBSCRIBER);
        int subscribeTargetId;

        if (session == null) {
            resp.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
            return;
        }
        if (subscribeTarget == null) {
            resp.setStatus(HttpServletResponse.SC_BAD_REQUEST);
            return;
        }
        try {
            subscribeTargetId = Integer.parseInt(subscribeTarget);
        } catch (NumberFormatException nfe) {
            resp.setStatus(HttpServletResponse.SC_BAD_REQUEST);
            return;
        }
        
        SessionFactory sessionFactory = (SessionFactory) context.getAttribute("sessionFactory");
        DatabaseManager manager = new DatabaseManager(sessionFactory);
        PrintWriter out = resp.getWriter();
        manager.beginTransaction();
        logger.info("here drop down");
        User subscriber = (User)session.getAttribute("curr_user");
        User user = manager.getUserById(subscribeTargetId);
        subscriber.getSubscribers().add(user);
        user.getSubscribers().add(subscriber);
        
        manager.commitAndClose();

    }

}
