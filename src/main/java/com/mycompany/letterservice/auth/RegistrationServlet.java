/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.mycompany.letterservice.auth;

import com.mycompany.letterservice.DatabaseManager;
import com.mycompany.letterservice.ResponseWrapper;
import com.mycompany.letterservice.Validator;
import com.mycompany.letterservice.entity.Account;
import com.mycompany.letterservice.entity.Message;
import com.mycompany.letterservice.entity.User;
import com.mycompany.letterservice.exceptions.LetterServiceException;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.Date;
import java.util.List;
import java.util.logging.Logger;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 *
 * @author nekres
 */
@WebServlet("/register")
public class RegistrationServlet extends HttpServlet {
    public static final Logger logger = Logger.getLogger(RegistrationServlet.class.getName());
    
    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        resp.setContentType("text/xml");
        PrintWriter writer = resp.getWriter();

        String name = req.getParameter("name");
        String email = req.getParameter("email");
        String password = req.getParameter("password");
    
        try {
            Validator.validate(email, Validator.Type.EMAIL);
            Validator.validate(name, Validator.Type.NAME);
            Validator.validate(password, Validator.Type.PASSWORD);
            
            User u = new User();
            u.setName(name);
            u.setEmail(email);
            
            Account acc = new Account();
            acc.setPassword(password);
            acc.setRegistrationDate(new Date());
            acc.setUser(u);
            
            DatabaseManager manager = new DatabaseManager();
            manager.beginTransaction();
            
//            Message message = new Message();
//            message.setBody("Message from uid 24 to uid 25");
//            message.setDate(new Date());
//            
//            List<User> users = manager.getObj(User.class);
//                logger.info(Integer.toString(users.get(0).getId()));
//                users.get(0).getMessage().add(message);
//            manager.updateObj(users.get(0));
//            manager.persistObj(message);
            manager.checkOnEmailExist(email);
            manager.persistObj(u);
            manager.persistObj(acc);
            
            writer.append(ResponseWrapper.wrap("OK",ResponseWrapper.Type.RESPONSE,ResponseWrapper.Type.END_RESPONSE));
            manager.commitAndClose();
            logger.info("Saving to database: " + acc.toString());
        } catch (LetterServiceException ex) {
            writer.append(ResponseWrapper.wrap(ex.getMessage(), ResponseWrapper.Type.ERROR, ResponseWrapper.Type.END_ERROR));
            logger.info("Exception with message: " + ex.getMessage());
        }
        }

        @Override
        protected void doGet
        (HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
            req.getRequestDispatcher("index.html").forward(req, resp);
        }

}
