/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.mycompany.letterservice;

import java.util.HashMap;
import javax.servlet.ServletContext;
import javax.servlet.annotation.WebListener;
import javax.servlet.http.HttpSession;
import javax.servlet.http.HttpSessionEvent;
import javax.servlet.http.HttpSessionListener;

/**
 *
 * @author nekres
 */
@WebListener
public class CustomHttpSessionListener implements HttpSessionListener{

    @Override
    public void sessionCreated(HttpSessionEvent hse) {
        HttpSession session = hse.getSession();
        ServletContext ctx = session.getServletContext();
        HashMap<String, HttpSession> activeSession = (HashMap<String, HttpSession>)ctx.getAttribute("activeSession");
        activeSession.put(session.getId(), session);
        ctx.setAttribute("activeSession", activeSession);
    }

    @Override
    public void sessionDestroyed(HttpSessionEvent hse) {
        HttpSession session = hse.getSession();
        ServletContext ctx = session.getServletContext();
        HashMap<String, HttpSession> map = (HashMap<String, HttpSession>)ctx.getAttribute("activeSession");
        map.remove(session.getId());
    }
    
}
