/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.mycompany.letterservice.auth;

import java.io.IOException;
import java.util.logging.Logger;
import javax.websocket.OnClose;
import javax.websocket.OnError;
import javax.websocket.OnMessage;
import javax.websocket.OnOpen;
import javax.websocket.Session;
import javax.websocket.server.ServerEndpoint;

/**
 *
 * @author nrs
 */
@ServerEndpoint("/listener")
public class ChatEndPoint {
    private final Logger logger = Logger.getLogger(this.getClass().getName());
    @OnOpen
    public void open(Session session){
        logger.info("OnOpen: " + session.getId());
        
    }
    @OnClose
    public void close(Session session){
        logger.info("OnClose: " + session.toString());
    }
    @OnMessage
    public void message(String message, Session session) throws IOException{
        logger.info("OnMessage: " + session.toString());
        
        session.getBasicRemote().sendText(message);
    }
    
    @OnError
    public void onError(Session session, Throwable throwable) {
    }
}
