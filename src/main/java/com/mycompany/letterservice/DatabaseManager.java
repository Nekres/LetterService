/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.mycompany.letterservice;

import com.mycompany.letterservice.entity.User;
import com.mycompany.letterservice.exceptions.EmailAlreadyExistException;
import java.util.List;
import org.hibernate.PropertyValueException;
import org.hibernate.Session;
import org.hibernate.Transaction;
import org.hibernate.cfg.Configuration;
import org.hibernate.query.Query;

/**
 *
 * @author nekres
 */
public class DatabaseManager {
    static Configuration c = new Configuration();
    static {c.configure("hibernate.cfg.xml");}
    Session session;
    Transaction transact;
    public DatabaseManager() {
        session = c.buildSessionFactory().openSession();
    }
    
    public final void persistObj(final Object obj) throws PropertyValueException{
        session.save(obj);
    }
    
    public final void updateObj(final Object obj){
        session.update(obj);
    }
    public final List<User> getObj(Class type){
        Query query = session.createQuery("from "+ type.getName());
        List<User> users = query.list();
        return users;
    }
    
    public final void checkOnEmailExist(final String email) throws EmailAlreadyExistException{
        final String hquery = "FROM User c WHERE email LIKE :email";
        Query query = session.createQuery(hquery);
        query.setParameter("email", email);
        if(!query.list().isEmpty())
            throw new EmailAlreadyExistException("Email " + email + " is busy. Try another one.");
    }
    public final Transaction beginTransaction(){
        return transact = session.beginTransaction();
    }
    public final void commitAndClose(){
        transact.commit();
        session.close();
    }
}
