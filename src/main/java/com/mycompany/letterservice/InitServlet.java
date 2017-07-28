/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.mycompany.letterservice;

import com.mycompany.letterservice.entity.User;
import java.io.IOException;
import java.io.PrintWriter;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 *
 * @author nekres
 */
@WebServlet("/reg")
public class InitServlet extends HttpServlet{
    
    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        resp.setContentType("text/xml");
        PrintWriter writer = resp.getWriter();
        
        String name = req.getParameter("name");
        String surname = req.getParameter("surname");
        String email = req.getParameter("email");
        
        User u = new User();
        u.setName(name);
        u.setSurname(surname);
        u.setEmail(email);
        
        DatabaseManager manager = new DatabaseManager();
        manager.beginTransaction();
        if(!manager.isEmailExist(email)){
            manager.persistObj(u);
            writer.append(generateResponse("OK"));
        }
        else
            writer.append(generateResponse("<error>Email already exist</error>"));
        manager.commitAndClose();
    }

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        
        
    }
    
    
    public String generateResponse(final String type){
        StringBuilder b = new StringBuilder("<?xml version=\"1.0\" encoding=\"UTF-8\"?>");
        b.append("<response>").append(type).append("</response>");
        return b.toString();
    }
}
