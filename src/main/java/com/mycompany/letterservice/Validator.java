/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.mycompany.letterservice;

import com.mycompany.letterservice.exceptions.BadEmailSyntaxException;
import com.mycompany.letterservice.exceptions.ForbiddenSymbolsException;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 *
 * @author nekres
 */
public class Validator {
    private static final int MIN_SIZE = 3;
    private static final Pattern EMAIL_REGEX = Pattern.compile("^([_a-zA-Z0-9-]+(\\.[_a-zA-Z0-9-]+)*@[a-zA-Z0-9-]+(\\.[a-zA-Z0-9-]+)*(\\.[a-zA-Z]{1,6}))?$");
    private static final Pattern DEFAULT_REGEX = Pattern.compile("^[a-zA-Z0-9_-]{3,15}$");
    
    public static enum Type{
        EMAIL, PASSWORD,NAME;
    }
    
    public static void validate(final String validate, Type type)throws BadEmailSyntaxException, ForbiddenSymbolsException{
        switch(type){
            case EMAIL:validateEmail(validate);
            break;
            case PASSWORD:
            case NAME:validateFS(validate);
        }
    }
    private static void validateEmail(final String email) throws BadEmailSyntaxException{
        Matcher match = EMAIL_REGEX.matcher(email);
       // if(!match.matches())
       //     throw new BadEmailSyntaxException(email);
    }
    private static void validateFS(final String validate) throws ForbiddenSymbolsException{
        Matcher match = DEFAULT_REGEX.matcher(validate);
       // if(!match.matches())
       //     throw new ForbiddenSymbolsException(validate);
    }
}
