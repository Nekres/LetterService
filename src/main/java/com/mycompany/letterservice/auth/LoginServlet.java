/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.mycompany.letterservice.auth;

import com.mycompany.letterservice.DatabaseManager;
import com.mycompany.letterservice.entity.Message;
import com.mycompany.letterservice.entity.User;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.logging.Logger;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

/**
 *
 * @author nekres
 */
@WebServlet("/login")
public class LoginServlet extends HttpServlet{
    private static final Logger logger = Logger.getLogger(LoginServlet.class.getName());
    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        String email = req.getParameter(RegistrationServlet.USER_EMAIL);
        String password = req.getParameter(RegistrationServlet.USER_PASSWORD);
        
        DatabaseManager manager = new DatabaseManager();
        manager.beginTransaction();
        logger.info("Transaction in /login executing");
        User user = manager.getUser(email, password);
        for(Message m : user.getMessage()){
        logger.info(m.toString());
        }
        
        HttpSession session = req.getSession();
        session.setAttribute("user", user.getName());
        logger.info(Boolean.toString(session.isNew()));
        PrintWriter pw = resp.getWriter();
    }
    
    
    
}
