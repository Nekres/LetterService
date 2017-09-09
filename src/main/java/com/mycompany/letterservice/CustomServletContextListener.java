/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.mycompany.letterservice;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.util.HashMap;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.servlet.ServletContext;
import javax.servlet.ServletContextEvent;
import javax.servlet.ServletContextListener;
import javax.servlet.annotation.WebListener;

/**
 *
 * @author nekres
 */
@WebListener
public class CustomServletContextListener implements ServletContextListener{

    @Override
    public void contextInitialized(ServletContextEvent sce) {
        File f = new File("session.out");
        ServletContext ctx = sce.getServletContext();
        
        if(f.exists()){
            try {
                FileInputStream fis = new FileInputStream(f);
                ObjectInputStream oin = new ObjectInputStream(fis);
                HashMap map = (HashMap)oin.readObject();
                ctx.setAttribute("activeSession", map);
            } catch (FileNotFoundException ex) {
                Logger.getLogger(CustomServletContextListener.class.getName()).log(Level.SEVERE, null, ex);
            } catch (IOException ex) {
                Logger.getLogger(CustomServletContextListener.class.getName()).log(Level.SEVERE, null, ex);
            } catch (ClassNotFoundException ex) {
                Logger.getLogger(CustomServletContextListener.class.getName()).log(Level.SEVERE, null, ex);
            }
        }else{
        HashMap activeSessions = new HashMap();
        ctx.setAttribute("activeSession", activeSessions);
        }
        }

    @Override
    public void contextDestroyed(ServletContextEvent sce) {
        try {
            //Serialize to not to lose session reference during server restarts.
            ServletContext ctx = sce.getServletContext();
            FileOutputStream fos = new FileOutputStream("session.out");
            ObjectOutputStream oos = new ObjectOutputStream(fos);
            HashMap map = (HashMap)ctx.getAttribute("activeSession");
            oos.writeObject(map);
            oos.flush();
            oos.close();
        } catch (FileNotFoundException ex) {
            Logger.getLogger(CustomServletContextListener.class.getName()).log(Level.SEVERE, null, ex);
        } catch (IOException ex) {
            Logger.getLogger(CustomServletContextListener.class.getName()).log(Level.SEVERE, null, ex);
        }
        
    }
    
}
