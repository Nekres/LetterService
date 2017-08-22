/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.mycompany.letterservice.exceptions;

/**
 *
 * @author nekres
 */
public class BadEmailSyntaxException extends LetterServiceException{
    
    private String message;

    public BadEmailSyntaxException(String message){
        this.message = "Email " + message + " is bad.";
    }
    @Override
    public String getMessage() {
        return this.message; //To change body of generated methods, choose Tools | Templates.
    }
    
    
}
