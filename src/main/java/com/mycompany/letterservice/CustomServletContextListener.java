/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.mycompany.letterservice;

import java.util.HashMap;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import javax.servlet.*;
import javax.servlet.annotation.WebListener;
import org.apache.log4j.Logger;
import org.hibernate.SessionFactory;
import org.hibernate.cfg.Configuration;
import org.slf4j.LoggerFactory;

/**
 *
 * @author nekres
 */
@WebListener
public class CustomServletContextListener implements ServletContextListener{
    private final Logger logger = Logger.getLogger("ServlerContextListener");
    private final org.slf4j.Logger telegramLogger = LoggerFactory.getLogger("Telegram Notification");
    private ScheduledExecutorService scheduler;
    
    @Override
    public void contextInitialized(ServletContextEvent sce) {
        logger.info("context created");
        telegramLogger.info("Web app started...");
        scheduler = Executors.newSingleThreadScheduledExecutor();
        
        ServletContext context = sce.getServletContext();
        HashMap activeSessions = new HashMap();
        context.setAttribute("activeSession", activeSessions);
        
        Configuration c = new Configuration();
        c.configure("hibernate.cfg.xml");
        SessionFactory factory = c.buildSessionFactory();
        context.setAttribute("sessionFactory", factory);
                

    }

    @Override
    public void contextDestroyed(ServletContextEvent sce) {
        
        SessionFactory sessionFactory = (SessionFactory)sce.getServletContext().getAttribute("sessionFactory");
        sessionFactory.close();
    }

    
}
