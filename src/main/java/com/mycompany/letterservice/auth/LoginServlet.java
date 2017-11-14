/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.mycompany.letterservice.auth;

import com.mycompany.letterservice.*;
import com.mycompany.letterservice.entity.User;
import com.mycompany.letterservice.exceptions.NoSuchUserException;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.HashMap;
import java.util.logging.Logger;
import javax.servlet.*;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.*;

/**
 *
 * @author nekres
 */
@WebServlet("/login")
public class LoginServlet extends HttpServlet {
    public static final int COOKIE_EXPIRE_TIME = 30*60;
    private static final Logger logger = Logger.getLogger(LoginServlet.class.getName());

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        req.setCharacterEncoding("UTF-8");
        resp.setCharacterEncoding("UTF-8");
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
            
            SessionManager.addCookie(resp, "session_id", session.getId(), COOKIE_EXPIRE_TIME);
            SessionManager.addCookie(resp, "user_id", Integer.toString(user.getId()), COOKIE_EXPIRE_TIME);
            SessionManager.addCookie(resp, "user_name", user.getName(), COOKIE_EXPIRE_TIME);
            SessionManager.addCookie(resp, "user_surname", user.getSurname(), COOKIE_EXPIRE_TIME);
            SessionManager.addCookie(resp, "user_photo", user.getPhotoUrl(), COOKIE_EXPIRE_TIME);
            logger.info(Integer.toString(user.getId()));
            
            
            resp.sendRedirect(resp.encodeRedirectURL("SuccessLogging.html"));
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
