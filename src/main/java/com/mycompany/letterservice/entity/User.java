/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.mycompany.letterservice.entity;

import java.util.HashSet;
import java.util.Set;
import javax.persistence.*;
/**
 *
 * @author nekres
 */
@Entity
@Table(name = "client")
public class User {
    @Id
    @GeneratedValue
    @Column(name = "user_id",nullable = false)
    private int id;
    @Column(name = "name",length = 50,nullable = false)
    private String name;
    @OneToMany(cascade = CascadeType.ALL)
    @JoinTable(name = "user_received_messages", joinColumns = {@JoinColumn(name = "user_id")},inverseJoinColumns ={@JoinColumn(name = "message_id")})
    private Set<Message> receivedMessage = new HashSet<>(0);
    
    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Set<Message> getMessage() {
        return receivedMessage;
    }

    public void setMessage(Set<Message> messages) {
        this.receivedMessage = messages;
    }

    @Override
    public String toString() {
        return "User{" + "id=" + id + ", name=" + name + ", receivedMessage=" + receivedMessage + '}';
    }
    
    
    
}
