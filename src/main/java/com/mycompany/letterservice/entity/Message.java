/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.mycompany.letterservice.entity;

import java.util.Date;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.ManyToOne;
import javax.persistence.Table;

/**
 *
 * @author nekres
 */
@Entity
@Table(name = "messages")
public class Message {
    @Id
    @GeneratedValue
    @Column(name = "id")
    private int id;
    @Column(name = "date")
    private Date date;
    @Column(name = "body",length = 1000)
    private String body;
    @ManyToOne
    private User messageSender;
    @ManyToOne
    private User messageRecipient;
    
}
