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
public class BadPropertiesException extends LetterServiceException{

    private String message;

    public BadPropertiesException(String message) {
        this.message = message;
    }
    
    
    @Override
    public String getMessage() {
        return this.getMessage(); //To change body of generated methods, choose Tools | Templates.
    }
    
}
