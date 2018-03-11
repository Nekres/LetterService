/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.mycompany.letterservice.auth.websocket;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.mycompany.letterservice.auth.config.HttpSessionConfigurator;
import com.mycompany.letterservice.entity.User;
import com.mycompany.letterservice.exceptions.BadPropertiesException;
import java.io.IOException;
import java.util.Arrays;
import java.util.Collections;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import org.apache.log4j.Logger;
import javax.servlet.http.HttpSession;
import javax.websocket.*;
import javax.websocket.server.ServerEndpoint;

/**
 * This class is used to maintain control of websocket sessions. It subscribes users for another users events
 * based on their friendship.
 * @author Misha Shikoryak
 * @version 1.0
 * @since 2017-08-25
 */
@ServerEndpoint(value = "/listener", configurator = HttpSessionConfigurator.class)
public class ChatEndPoint {
    private static final Logger logger = Logger.getLogger(ChatEndPoint.class.getName());
    private static final Map<User,ChatEndPoint> chatEndPoints = Collections.synchronizedMap(new HashMap());
    
    private Session session;
    private User user;
    private Map<User, ChatEndPoint> subscribers = Collections.synchronizedMap(new HashMap());
    
    @OnOpen
    public void open(Session session, EndpointConfig config){
        this.session = session;
        
        logger.info("OnOpen: " + session.getId());
        HttpSession httpSession = (HttpSession)config.getUserProperties().get(HttpSession.class.getName());
        
         if(httpSession == null){
            logger.info("No http session. Closed.");
            try {
                session.close();
            } catch (IOException ex) {
                logger.debug("ioex", ex);
            }
        }
        
        user = (User)httpSession.getAttribute("curr_user");
        chatEndPoints.put(user,this);
        logger.info(user.getName());
        findSubs(user);//fills subscribes set with active friends
        
    }
    @OnClose
    public void onClose(Session session){
        logger.info("OnClose: " + session.toString());
        //unsubscribe everyone on close
        subscribers.values().forEach((t) -> {
            t.unsubscribe(this.user);
            System.out.println(t.toString());
        });
        chatEndPoints.remove(this.user);
        this.session = null;
    }
    @OnMessage
    public void message(String message, Session session) throws BadPropertiesException, JsonProcessingException{
        logger.info("User sends message with content: " +  message);
        
//Read JSON message from client
        final ObjectMapper mapper = new ObjectMapper();
        Event event;
        try{
            event = mapper.readValue(message, Event.class);
            event.setEventOwnerId(this.user.getId());
            logger.info("Json is correct.");
        }catch(IOException ex){
            BadPropertiesException bpe = new BadPropertiesException("Bad json.");
            bpe.initCause(ex);
            throw bpe;
        }
        if(event.getEventType() == Event.EventType.PING){
            echo("Success.");
            return;
        }
        
        //Sending event notification to each one who present at 'targetId' field
            List<Integer> values = Arrays.asList(event.getTargetId());
            Set<User> users = subscribers.keySet();
            for(User u: users){
                if(values.contains(u.getId())){
                    ChatEndPoint endPoint = subscribers.get(u);
                    String callback = mapper.writeValueAsString(event);
                    endPoint.echo(callback);
                    logger.info("Echoing to " + u.getName() + " with id " + u.getId() + " message: " + message);
                }
            }
    }
    /**
     * Sends message to this endPoint.
     * @param message - text message to send
     */
    public synchronized void echo(final String message){
        try {
            this.session.getBasicRemote().sendText(message);
        } catch (IOException ex) {
            ex.printStackTrace();
        }
    }
    /**
     * Subscribes user to specific websocket-session(endPoint) to listen its events.
     * @param user - user to subscribe
     * @param endPoint - websocket-session on which subscribe
     */
    public void subscribe(final User user, final ChatEndPoint endPoint){
        subscribers.put(user, endPoint);
    }
    /**
     * Unsubscribes user from current user. Used when websocket closing connection.
     * @param user 
     */
    public void unsubscribe(final User user){
        subscribers.remove(user);
    }
    /**
     * This method is used to subscribe user-1 websocket-session to events of another user-2 only 
     * and if the second and first user are friends. Of course it is works only if user-2 is currently online
     * 
     * @param user - user to subscribe for event 
     */
    private void findSubs(final User user){
        final Set<User> users = new HashSet<>(chatEndPoints.keySet());
        users.retainAll(user.getSubscribers());
        users.forEach((t) -> {
            ChatEndPoint endPoint = chatEndPoints.get(t);
            subscribe(t, endPoint);
            endPoint.subscribe(user, this);
        });
    }
    /**
     * Use to check if two users are friends.
     * @param first 
     * @param second
     * @return true if they are
     */
    private boolean friendOf(final User first, final User second){
        return first.getSubscribers().contains(second) && second.getSubscribers().contains(first);
    }
    @OnError
    public void onError(Session session, Throwable throwable) {
        throwable.printStackTrace();
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

    public static Map<User, ChatEndPoint> getChatEndPoints() {
        return chatEndPoints;
    }
    
    
}
