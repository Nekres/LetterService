/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.mycompany.letterservice.auth.websocket;


/**
 * Pojo that contains information about user event.
 * 
 * @author Misha Shikoryak
 */
public class Event {
    //user_id
    private int eventOwnerId; 
    private EventType eventType;
    /**
     * Id of users to be notified about event.
     */
    private Integer[] targetId;

    public Event(int eventOwnerId, EventType eventType) {
        this.eventOwnerId = eventOwnerId;
        this.eventType = eventType;
    }

    public Event() {
    }
    
    
    /**
     * User events can be different types. Message means user sends message.
     * IS_TYPING that he is typing right now. COME_ONLINE means that he just logged in, and GOES_OFFLINE means the reverse thing.
     * ONLINE_ACTIVITY used for notificate user listeners if user is chatting now, collapsed browser tab, or take focus of another window.
     */
    
    public enum EventType{
        
        MESSAGE, IS_TYPING, COME_ONLINE, GOES_OFFLINE, ONLINE_ACTIVITY, PING;
        
    }

    public Integer[] getTargetId() {
        return targetId;
    }

    public void setTargetId(Integer[] targetId) {
        this.targetId = targetId;
    }
    
    
    public int getEventOwnerId() {
        return eventOwnerId;
    }

    public EventType getEventType() {
        return eventType;
    }

    public void setEventOwnerId(int eventOwnerId) {
        this.eventOwnerId = eventOwnerId;
    }

    public void setEventType(EventType eventType) {
        this.eventType = eventType;
    }
    
    
    
    
    
}
