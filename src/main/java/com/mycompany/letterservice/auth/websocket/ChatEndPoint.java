/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.mycompany.letterservice.auth.websocket;

import com.mycompany.letterservice.auth.config.HttpSessionConfigurator;
import java.io.IOException;
import java.util.Collections;
import java.util.HashSet;
import java.util.Set;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.servlet.http.HttpSession;
import javax.websocket.*;
import javax.websocket.server.ServerEndpoint;

/**
 *
 * @author nrs
 */
@ServerEndpoint(value = "/listener", configurator = HttpSessionConfigurator.class)
public class ChatEndPoint {
    private final Logger logger = Logger.getLogger(this.getClass().getName());
    private Session session;
    
    private static final Set<ChatEndPoint> chatEndPoints = Collections.synchronizedSet(new HashSet());
    
    @OnOpen
    public void open(Session session, EndpointConfig config){
        this.session = session;
        chatEndPoints.add(this);
        
        logger.info("OnOpen: " + session.getId());
        HttpSession httpSession = (HttpSession)config.getUserProperties().get(HttpSession.class.getName());
        if(httpSession == null){
            try {
                session.close();
            } catch (IOException ex) {
                logger.log(Level.SEVERE, null, ex);
            }
        }
    }
    @OnClose
    public void onClose(Session session){
        logger.info("OnClose: " + session.toString());
        
        chatEndPoints.remove(this);
        this.session = null;
    }
    @OnMessage
    public void message(String message, Session session){
        logger.info("OnMessage: " + session.toString());
        for(ChatEndPoint s: chatEndPoints){
            if(this != s)
                s.echo("User is typing...");
        }
    }
    public void echo(final String message){
        try {
            this.session.getBasicRemote().sendText(message);
        } catch (IOException ex) {
            logger.log(Level.SEVERE, null, ex);
        }
    }
    @OnError
    public void onError(Session session, Throwable throwable) {
    }
    
    void close() {
        synchronized (this.session) {
            try {
                this.session.close();
            } catch (IOException ex) {
                logger.log(Level.SEVERE, null, ex);
            }
        }
    }
}
