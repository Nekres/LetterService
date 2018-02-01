/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.mycompany.letterservice;

import java.util.HashMap;
import javax.servlet.*;
import javax.servlet.annotation.WebListener;

/**
 *
 * @author nekres
 */
@WebListener
public class CustomServletContextListener implements ServletContextListener{
    
    @Override
    public void contextInitialized(ServletContextEvent sce) {
        
        HashMap activeSessions = new HashMap();
        sce.getServletContext().setAttribute("activeSession", activeSessions);
        }

    @Override
    public void contextDestroyed(ServletContextEvent sce) {
    }

    
}
