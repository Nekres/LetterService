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
public class AuthenticationRequiresException extends LetterServiceException{
    public static final String DEFAULT_MESSAGE = "Authentification needs to reach this resource";
    private String message;

    public AuthenticationRequiresException(String message) {
        this.message = message;
    }

    public AuthenticationRequiresException() {
        this.message = DEFAULT_MESSAGE;
    }
    
    

    @Override
    public String getMessage() {
        return this.message; //To change body of generated methods, choose Tools | Templates.
    }
    
    
}
