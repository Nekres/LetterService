/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.mycompany.letterservice.auth;

import com.mycompany.letterservice.*;
import com.mycompany.letterservice.entity.*;
import com.mycompany.letterservice.exceptions.BadPropertiesException;
import com.mycompany.letterservice.exceptions.LetterServiceException;
import java.io.*;
import java.net.URLEncoder;
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
        if(picture == null || name == null || password == null || surname == null){
            throw new BadPropertiesException("Check your request parameters");
        }
        
        try {
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
            
            DatabaseManager manager = new DatabaseManager();
            manager.beginTransaction();
            
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
