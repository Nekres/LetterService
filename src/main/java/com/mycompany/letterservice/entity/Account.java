/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.mycompany.letterservice.entity;

import java.util.Date;
import javax.persistence.*;

/**
 *
 * @author nekres
 */
@Entity
@Table(name = "account")
public class Account {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "account_id",nullable = false,unique = true)
    private int id;
    @Column(name = "registration_date", nullable = false,updatable = false)
    private Date registrationDate;
    @Column(name = "password",nullable = false)
    private String password;
    @OneToOne(cascade = CascadeType.ALL)
    @JoinColumn(name = "user_id",nullable = false)
    private User user;
    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public Date getRegistrationDate() {
        return registrationDate;
    }

    public void setRegistrationDate(Date registrationDate) {
        this.registrationDate = registrationDate;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }

    @Override
    public String toString() {
        return "Account{" + "id=" + id + ", registrationDate=" + registrationDate + ", password=" + password + ", user=" + user + '}';
    }
    
    
}
