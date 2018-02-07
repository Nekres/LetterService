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
import org.hibernate.SessionFactory;
import org.apache.log4j.Logger;

/**
 *
 * @author nekres
 */
@WebServlet(value = {UserManagerServlet.USERS_GET, UserManagerServlet.USERS_ALL})
public class UserManagerServlet extends HttpServlet {

    public static final String USERS_GET = "/users.get";
    public static final String USERS_ALL = "/users.all";

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
        }
        if (req.getServletPath().equals(USERS_ALL)) {
            List<User> userList = manager.getAllUsers();
            String json = mapper.writeValueAsString(userList);
            out.write(json);
        }
        manager.commitAndClose();
    }

}
