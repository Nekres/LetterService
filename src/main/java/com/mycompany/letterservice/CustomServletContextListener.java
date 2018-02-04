/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.mycompany.letterservice;

import java.util.HashMap;
import javax.servlet.*;
import javax.servlet.annotation.WebListener;
import org.hibernate.SessionFactory;
import org.hibernate.cfg.Configuration;

/**
 *
 * @author nekres
 */
@WebListener
public class CustomServletContextListener implements ServletContextListener{
    
    @Override
    public void contextInitialized(ServletContextEvent sce) {
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
