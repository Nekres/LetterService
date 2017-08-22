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
public class ForbiddenSymbolsException extends LetterServiceException{
    
    private String message;

    public ForbiddenSymbolsException(String message) {
        this.message = message + " contains forbidden symbols.";
    }

    @Override
    public String getMessage() {
        return this.message; 
    }
    
    
}
