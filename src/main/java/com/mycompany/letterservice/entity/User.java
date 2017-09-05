/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.mycompany.letterservice.entity;

import java.util.HashSet;
import java.util.Objects;
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
//    @Column(name = "surname", length = 50, nullable = false)
//    private String surname;
//    @Column(name = "photo_url", length = 250, nullable = false)
//    private String photoUrl;
//    @OneToMany(cascade = CascadeType.ALL)
//    @JoinTable(name = "user_received_messages", joinColumns = {@JoinColumn(name = "user_id")},inverseJoinColumns ={@JoinColumn(name = "message_id")})
//    private Set<Message> receivedMessage = new HashSet<>(0);
    
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

//    public String getSurname() {
//        return surname;
//    }
//
//    public void setSurname(String surname) {
//        this.surname = surname;
//    }
//
//    public String getPhotoUrl() {
//        return photoUrl;
//    }
//
//    public void setPhotoUrl(String photoUrl) {
//        this.photoUrl = photoUrl;
//    }

//    public Set<Message> getReceivedMessage() {
//        return receivedMessage;
//    }
//
//    public void setReceivedMessage(Set<Message> receivedMessage) {
//        this.receivedMessage = receivedMessage;
//    }

    @Override
    public String toString() {
        return "User{" + "id=" + id + ", name=" + name + '}';
    }
    
    
    
    @Override
    public int hashCode() {
        int hash = 7;
        hash = 29 * hash + this.id;
        hash = 29 * hash + Objects.hashCode(this.name);
        return hash;
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj) {
            return true;
        }
        if (obj == null) {
            return false;
        }
        if (getClass() != obj.getClass()) {
            return false;
        }
        final User other = (User) obj;
        if (this.id != other.id) {
            return false;
        }
        if (!Objects.equals(this.name, other.name)) {
            return false;
        }
        return true;
    }
    
    
    
    
}
