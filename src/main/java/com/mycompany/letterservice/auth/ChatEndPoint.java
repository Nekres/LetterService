/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.mycompany.letterservice.auth;

import java.util.logging.Logger;
import javax.enterprise.context.ApplicationScoped;
import javax.websocket.OnClose;
import javax.websocket.OnMessage;
import javax.websocket.OnOpen;
import javax.websocket.Session;
import javax.websocket.server.ServerEndpoint;

/**
 *
 * @author nrs
 */
@ApplicationScoped
@ServerEndpoint("/listener")
public class ChatEndPoint {
    private final Logger logger = Logger.getLogger(this.getClass().getName());
    @OnOpen
    public void open(Session session){
        logger.info("OnOpen: " + session.toString());
    }
    @OnClose
    public void close(Session session){
        logger.info("OnClose: " + session.toString());
    }
    @OnMessage
    public void message(Session session){
        logger.info("OnMessage: " + session.toString());
    }
    
}
