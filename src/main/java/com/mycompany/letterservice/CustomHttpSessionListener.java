/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.mycompany.letterservice;

import java.util.Date;
import java.util.HashMap;
import javax.servlet.ServletContext;
import javax.servlet.annotation.WebListener;
import javax.servlet.http.*;
import org.jboss.logging.Logger;

/**
 *
 * @author nekres
 */
@WebListener
public class CustomHttpSessionListener implements HttpSessionListener{
    private final Logger logger = Logger.getLogger("SessionListener");
    @Override
    public void sessionCreated(HttpSessionEvent hse) {
        HttpSession session = hse.getSession();
        session.setMaxInactiveInterval(1 * 60 * 60);
        ServletContext ctx = session.getServletContext();
        HashMap<String, HttpSession> activeSession = (HashMap<String, HttpSession>)ctx.getAttribute("activeSession");
        activeSession.put(session.getId(), session);
        ctx.setAttribute("activeSession", activeSession);
        logger.info("Session created " + new Date().toString());
    }

    @Override
    public void sessionDestroyed(HttpSessionEvent hse) {
        HttpSession session = hse.getSession();
        ServletContext ctx = session.getServletContext();
        HashMap<String, HttpSession> map = (HashMap<String, HttpSession>)ctx.getAttribute("activeSession");
        map.remove(session.getId());
        logger.info("Session destroyed " + new Date().toString());
    }
    
}
