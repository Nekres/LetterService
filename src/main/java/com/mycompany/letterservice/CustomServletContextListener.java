/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.mycompany.letterservice;

import java.util.HashMap;
import javax.servlet.*;
import javax.servlet.annotation.WebListener;
import org.apache.log4j.Logger;
import org.hibernate.SessionFactory;
import org.hibernate.cfg.Configuration;

/**
 *
 * @author nekres
 */
@WebListener
public class CustomServletContextListener implements ServletContextListener{
    private final Logger logger = Logger.getLogger("ServlerContextListener");
    @Override
    public void contextInitialized(ServletContextEvent sce) {
        logger.info("context created");
        logger.debug("context created");
        logger.warn("");
        ServletContext context = sce.getServletContext();
        
        Configuration c = new Configuration();
        c.configure("hibernate.cfg.xml");
        SessionFactory factory = c.buildSessionFactory();
        context.setAttribute("sessionFactory", factory);
                
        HashMap activeSessions = new HashMap();
        context.setAttribute("activeSession", activeSessions);
    }

    @Override
    public void contextDestroyed(ServletContextEvent sce) {
    }

    
}
