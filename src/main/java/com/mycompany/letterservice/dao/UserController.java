/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.mycompany.letterservice.dao;

import com.mycompany.letterservice.entity.User;
import java.util.List;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.Transaction;

/**
 *
 * @author root
 */
public class UserController extends AbstractController<User, Integer>{
    
    
    public UserController(SessionFactory factory) {
        super(factory);
    }
    
    
    @Override
    public List<User> getAll() {
        Session session = this.factory.openSession();
        List<User> list = session.createQuery("FROM User").list();
        session.close();
        return list;
    }
    @Override
    public User getEntityById(Integer id) {
        Session session = this.factory.openSession();
        final User u = session.get(User.class, id);
        session.close();
        return u;
    }
    @Override
    public void update(User entity) {
        Session session = this.factory.openSession();
        Transaction tx = session.beginTransaction();
        session.update(entity);
        tx.commit();
        session.close();
    }
    @Override
    public boolean delete(Integer id) {
        Session session = this.factory.openSession();
        Transaction tx = session.beginTransaction();
        User u = session.load(User.class, id);
        if(u == null){
            tx.rollback();
            session.close();
            return false;
        }else{
            session.delete(u);
            tx.commit();
            session.close();
            return true;
        }
    }
    @Override
    public void create(User entity) {
        Session session = this.factory.openSession();
        Transaction tx = session.beginTransaction();
        session.persist(entity);
        tx.commit();
        session.close();
        
    }

    @Override
    public void save(User entity) {
        Session session = this.factory.openSession();
        Transaction tx = session.beginTransaction();
        session.save(entity);
        tx.commit();
        session.close();
    }
    
}
