/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.mycompany.letterservice;

import com.mycompany.letterservice.entity.User;
import org.apache.log4j.Logger;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.Transaction;
import org.hibernate.cfg.Configuration;
import org.hibernate.query.Query;

/**
 *
 * @author nekres
 */
public class DatabaseManager {
    public static final Logger LOGGER = Logger.getLogger(InitServlet.class);
    Configuration c;
    Session session;
    Transaction transact;
    public DatabaseManager() {
        c = new Configuration();
        c.configure("hibernate.cfg.xml");   
        session = c.buildSessionFactory().openSession();
    }
    
    public final void persistObj(final User user){
        LOGGER.debug("persisting obj to db");
        session.persist(user);
        transact.commit();
    }
    public final boolean isEmailExist(final String email){
        final String hquery = "FROM client c WHERE email LIKE :email";
        Query query = session.createQuery(hquery);
        query.setParameter("email", email);
        if(query.list().isEmpty())
            return false;
        return true;
    }
    public final Transaction beginTransaction(){
        return transact = session.beginTransaction();
    }
    public final void commitAndClose(){
        session.close();
    }
}
