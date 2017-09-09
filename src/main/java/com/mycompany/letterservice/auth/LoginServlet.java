/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.mycompany.letterservice.auth;

import com.mycompany.letterservice.DatabaseManager;
import com.mycompany.letterservice.SessionManager;
import com.mycompany.letterservice.entity.User;
import com.mycompany.letterservice.exceptions.NoSuchUserException;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.HashMap;
import java.util.logging.Logger;
import javax.servlet.RequestDispatcher;
import javax.servlet.ServletContext;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

/**
 *
 * @author nekres
 */
@WebServlet("/login")
public class LoginServlet extends HttpServlet {

    private static final Logger logger = Logger.getLogger(LoginServlet.class.getName());

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        logger.info(req.getServletPath());
        String email = req.getParameter(RegistrationServlet.USER_EMAIL);
        String password = req.getParameter(RegistrationServlet.USER_PASSWORD);
        DatabaseManager manager = new DatabaseManager();
        ServletContext ctx = req.getServletContext();
        
        try {
            manager.beginTransaction();
            logger.info("Transaction in /login executing");
            User user = manager.getUser(email, password);
            HttpSession session = req.getSession();
            session.setAttribute("user", user.getName());
            session.setAttribute("curr_u_id", user.getId());
            HashMap<String, HttpSession> activeSession = (HashMap<String, HttpSession>)ctx.getAttribute("activeSession");
            activeSession.put(session.getId(), session);
            SessionManager.addCookie(resp, "session_id", session.getId(), 30 * 60);
            
            logger.info(Integer.toString(user.getId()));
            resp.sendRedirect(resp.encodeRedirectURL("SuccessLogging.jsp"));
        } catch (NoSuchUserException exception) {
            RequestDispatcher dispatcher = getServletContext().getRequestDispatcher("/index.html");
            PrintWriter out = resp.getWriter();
            out.println("<font color=red>ERROR</font>");
            resp.setStatus(HttpServletResponse.SC_BAD_REQUEST);
            dispatcher.include(req, resp);
        } finally {
            manager.commitAndClose();
        }
    }

}
