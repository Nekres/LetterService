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
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 *
 * @author nekres
 */
@WebServlet(value = {"/users.get"})
public class UserManagerServlet extends HttpServlet{
    private static final ObjectMapper mapper = new ObjectMapper();
    
    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        DatabaseManager manager = new DatabaseManager();
        manager.beginTransaction();
        String name = req.getParameter("name");
        PrintWriter out = resp.getWriter();
        if(name == null){
            manager.commitAndClose();
            out.write(mapper.writeValueAsString(new com.mycompany.letterservice.entity.Error("null")));
            return;
        }
        List<User> userList = manager.getUsersByName(name);
        if(userList.isEmpty())
             out.write(mapper.writeValueAsString(new com.mycompany.letterservice.entity.Error("users with name " + name + " not found")));
        String json = mapper.writeValueAsString(userList);
        out.write(json);
        manager.commitAndClose();
    }
    
    
}
