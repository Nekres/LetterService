/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.mycompany.letterservice.auth;

import com.mycompany.letterservice.SessionManager;
import static com.mycompany.letterservice.auth.LoginServlet.COOKIE_NO_EXPIRE;
import java.io.IOException;
import java.io.PrintWriter;
import org.apache.log4j.Logger;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

/**
 *
 * @author nrs
 */
@WebServlet("/logout")
public class LogoutServlet extends HttpServlet{
    private static final Logger logger = Logger.getLogger("Logout:");
    
    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException{
        logger.info("/logout request");
        resp.setContentType("text/html");
        HttpSession session = req.getSession(false);
        
        if(session != null){
            synchronized(session){
            session.invalidate();
            logger.info("Session destroyed.");
            }
        }
        resp.setHeader("Location", resp.encodeRedirectURL("index.html"));
        resp.flushBuffer();
    }
    
    
}
