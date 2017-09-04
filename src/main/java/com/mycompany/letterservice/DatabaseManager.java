/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.mycompany.letterservice;

import com.mycompany.letterservice.auth.RegistrationServlet;
import com.mycompany.letterservice.entity.User;
import com.mycompany.letterservice.exceptions.EmailAlreadyExistException;
import com.mycompany.letterservice.exceptions.NoSuchUserException;
import java.util.List;
import java.util.logging.Logger;
import javax.persistence.NoResultException;
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
    private static final Logger logger = Logger.getLogger(DatabaseManager.class.getName());
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
    public final User getUser(final String email, final String password) throws NoSuchUserException{
        String q = "select user from Account as acc inner join acc.user as user where acc.email = :email and acc.password = :password";
        Query query = session.createQuery(q);
        query.setParameter(RegistrationServlet.USER_EMAIL, email);
        query.setParameter(RegistrationServlet.USER_PASSWORD, password);
        User u = null;
        try{
        u = (User)query.getSingleResult();
        }catch(NoResultException nre){
            throw new NoSuchUserException("User with email :" + email + " and password " + password + " not found.");
        }
        return u;
    }
    
    public final User getUserById(final int id) throws NoSuchUserException{
        String q = "from User where user_id = :id";
        Query query = session.createQuery(q);
        query.setParameter("id", id);
        User u = null;
        try{
            u = (User)query.getSingleResult();
        }catch(NoResultException nre){
            throw new NoSuchUserException("User with this id not exist");
        }
        return u;
    }
    
    public final void checkOnEmailExist(final String email) throws EmailAlreadyExistException{
        final String hquery = "FROM Account a WHERE email LIKE :email";
        Query query = session.createQuery(hquery);
        query.setParameter("email", email);
        if(!query.list().isEmpty())
            throw new EmailAlreadyExistException("Email " + email + " is busy. Try another one.");
    }
//    public final List<User> getUsersByName(final String name){
//        List<User> users = session.createQuery(cd)
//    }
    public final Transaction beginTransaction(){
        return transact = session.beginTransaction();
    }
    public final void commitAndClose(){
        transact.commit();
        session.close();
    }
}
