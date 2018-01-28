/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.mycompany.letterservice.auth;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.mycompany.letterservice.*;
import com.mycompany.letterservice.entity.*;
import com.mycompany.letterservice.exceptions.BadPropertiesException;
import com.mycompany.letterservice.exceptions.LetterServiceException;
import java.io.*;
import java.nio.file.Paths;
import java.util.Date;
import java.util.logging.Logger;
import javax.servlet.ServletException;
import javax.servlet.annotation.MultipartConfig;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.*;
import javax.servlet.http.Part;

/**
 *
 * @author nekres
 */
@WebServlet("/register")
@MultipartConfig
public class RegistrationServlet extends HttpServlet {
    public static final String LOGIN = "login";
    public static final String USER_NAME = "name";
    public static final String USER_SURNAME = "surname";
    public static final String USER_EMAIL = "email";
    public static final String USER_PASSWORD = "password";
    public static final String PICTURE = "picture";
    public static final Logger logger = Logger.getLogger(RegistrationServlet.class.getName());
    
    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        req.setCharacterEncoding("UTF-8");
        resp.setCharacterEncoding("UTF-8");
        resp.setContentType("text/xml");
        PrintWriter writer = resp.getWriter();
        //receiving fields
        String name = req.getParameter(USER_NAME);
        String surname = req.getParameter(USER_SURNAME);
        String email = req.getParameter(USER_EMAIL);
        String password = req.getParameter(USER_PASSWORD);
        Part picture = req.getPart(PICTURE);
        
        //jackson
            ObjectMapper mapper = new ObjectMapper();
        try {
            if(name.isEmpty() || surname.isEmpty() || email.isEmpty() || password.isEmpty() || picture.getSize() == 0){
            throw new BadPropertiesException("Check your request parameters");
        }
            Validator.validate(email, Validator.Type.EMAIL);
            Validator.validate(name, Validator.Type.NAME);
            Validator.validate(password, Validator.Type.PASSWORD);
             //upload avatar if validation passed
            String pictureName = Paths.get(picture.getSubmittedFileName()).getFileName().toString();
            InputStream filestream = picture.getInputStream();
            String photoUrl = ImageUploaderService.upload(filestream, (int)picture.getSize(), pictureName);
            
            User u = new User();
            u.setName(name);
            u.setPhotoUrl(photoUrl);
            u.setSurname(surname);
            
            Account acc = new Account();
            acc.setPassword(password);
            acc.setRegistrationDate(new Date());
            acc.setEmail(email);
            acc.setUser(u);
            
            
            //db
            DatabaseManager manager = new DatabaseManager();
            manager.beginTransaction();
            
            manager.checkOnEmailExist(email);
            manager.persistObj(u);
            manager.persistObj(acc);
            
            writer.append(mapper.writeValueAsString(new Status("Account successfully created")));
            manager.commitAndClose();
            logger.info("Saving to database: " + acc.toString());
        } catch (LetterServiceException ex) {
            writer.append(mapper.writeValueAsString(new Status(ex.getMessage())));
            resp.setStatus(HttpServletResponse.SC_BAD_REQUEST);
            logger.info("Exception with message: " + ex.getMessage());
        }
        }

        @Override
        protected void doGet
        (HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
            req.getRequestDispatcher("index.html").forward(req, resp);
        }

}
