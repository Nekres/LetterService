/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.mycompany.letterservice.auth.websocket;

import com.mycompany.letterservice.auth.config.HttpSessionConfigurator;
import com.mycompany.letterservice.entity.User;
import java.io.IOException;
import java.util.Collection;
import java.util.Collections;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;
import org.apache.log4j.Logger;
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
    private static final Map<User,ChatEndPoint> chatEndPoints = Collections.synchronizedMap(new HashMap());
    private Session session;
    private User user;
    private Map<User, ChatEndPoint> subscribers = Collections.synchronizedMap(new HashMap());
    
    @OnOpen
    public void open(Session session, EndpointConfig config){
        this.session = session;
        
        logger.info("OnOpen: " + session.getId());
        HttpSession httpSession = (HttpSession)config.getUserProperties().get(HttpSession.class.getName());
        user = (User)httpSession.getAttribute("curr_user");
        chatEndPoints.put(user,this);
        findSubs(user);//fills subscribes set with active friends
        
        logger.info(httpSession.getAttribute("user"));
        if(httpSession == null){
            try {
                session.close();
            } catch (IOException ex) {
                logger.debug("ioex", ex);
            }
        }
    }
    @OnClose
    public void onClose(Session session){
        logger.info("OnClose: " + session.toString());
        //unsubscribe everyone on close
        subscribers.values().forEach((t) -> {
            t.unsubscribe(this.user);
        });
        chatEndPoints.remove(this.user);
        this.session = null;
    }
    @OnMessage
    public void message(String message, Session session){
        logger.info("OnMessage: " + session.toString());
            subscribers.values().forEach((t) -> {
                t.echo("User is typing...");
            });
    }
    public void echo(final String message){
        try {
            this.session.getBasicRemote().sendText(message);
        } catch (IOException ex) {
            logger.debug("",ex);
        }
    }
    public void subscribe(final User user, final ChatEndPoint endPoint){
        subscribers.put(user, endPoint);
    }
    public void unsubscribe(final User user){
        subscribers.remove(user);
    }
    private void findSubs(final User user){
        final Set<User> users = new HashSet<>(chatEndPoints.keySet());
        users.retainAll(user.getSubscribers());
        users.forEach((t) -> {
            ChatEndPoint endPoint = chatEndPoints.get(t);
            subscribe(t, endPoint);
            endPoint.subscribe(user, this);
        });
    }
    @OnError
    public void onError(Session session, Throwable throwable) {
    }
    
    void close() {
        synchronized (this.session) {
            try {
                this.session.close();
            } catch (IOException ex) {
                logger.debug("",ex);
            }
        }
    }
}
