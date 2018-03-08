/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.mycompany.letterservice;

import com.mycompany.letterservice.auth.RegistrationServlet;
import com.mycompany.letterservice.entity.*;
import com.mycompany.letterservice.exceptions.*;
import java.util.List;
import org.apache.log4j.Logger;
import javax.persistence.NoResultException;
import org.apache.log4j.Priority;
import org.hibernate.*;
import org.hibernate.cfg.Configuration;
import org.hibernate.query.Query;

/**
 *
 * @author nekres
 */
public class DatabaseManager {
    private static final Logger logger = Logger.getLogger(DatabaseManager.class.getName());

    private final SessionFactory factory;
    private Session session;
    private Transaction transact;
    
    
    public DatabaseManager(final SessionFactory factory) {
        this.factory = factory;
    }
    
    public final void persistObj(final Object obj) throws PropertyValueException{
        session.save(obj);
    }
    
    public final void updateObj(final Object obj){
        session.update(obj);
    }
    public final <T> List<T> getObjects(T type){
        Query query = session.createQuery("from "+ type.getClass().getName());
        List<T> list = query.list();
        return list;
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
    
    public final List<Message> getUserMessagesByUid(final int userId, final int rec_id, final int count){
        Query query = session.createQuery("FROM Message m where m.senderId = :userId and m.receiverId = :rec_id");
        Query aQuery = session.createQuery("FROM Message m where m.senderId = :rec_id and m.receiverId = :userId");
        query.setParameter("userId", userId);
        query.setParameter("rec_id", rec_id);
        aQuery.setParameter("userId", userId);
        aQuery.setParameter("rec_id", rec_id);
        List<Message> list = query.list();
        list.addAll(aQuery.list());
        return list;
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
    
    public final List<User> getAllUsers() throws NoSuchUserException{
        String q = "from User";
        Query query = session.createQuery(q);
        List<User> list = query.list();
        if(list == null || list.isEmpty())
            throw new NoSuchUserException("Database is empty.");
        else return list;
    }
    
    public final void checkOnEmailExist(final String email) throws EmailAlreadyExistException{
        final String hquery = "FROM Account a WHERE email LIKE :email";
        Query query = session.createQuery(hquery);
        query.setParameter("email", email);
        if(!query.list().isEmpty())
            throw new EmailAlreadyExistException("Email " + email + " is busy. Try another one.");
    }
    public final List<User> getUsersByName(final String name) throws NoSuchUserException{
        Query query = session.createQuery("FROM User WHERE name = :name");
        query.setParameter("name", name);
        List<User> list = null;
        if((list = query.list()).isEmpty()){
            throw new NoSuchUserException("Users with name \"" + name + "\" not found.");
        }
        return list;
    }
    public final void setSubscriber(){
      //  Query query = session.createQ
    }
    public final Transaction beginTransaction(){
        session = factory.openSession();
        return transact = session.beginTransaction();
    }
    public final void commitAndClose(){
        transact.commit();
        session.clear();
        session.close();
    }
    public final void closeSession(){
        session.clear();
        session.close();
    }
}
