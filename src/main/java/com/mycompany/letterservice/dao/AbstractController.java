/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.mycompany.letterservice.dao;

import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.List;
import org.hibernate.SessionFactory;

/**
 *
 * @author root
 */
public abstract class AbstractController <E, K>{
    protected SessionFactory factory;
    
    public abstract List<E> getAll();
    public abstract E getEntityById(K id);
    public abstract void update(E entity);
    public abstract boolean delete(K id);
    public abstract void create(E entity);

    public AbstractController(SessionFactory factory) {
        this.factory = factory;
    }
    
   
}
