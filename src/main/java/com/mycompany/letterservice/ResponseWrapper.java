/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.mycompany.letterservice;

/**
 * Wraps response with xml.
 * @author Misha Shikoryak
 */
public class ResponseWrapper {
    
    public enum Type{
        ERROR("<error>"), END_ERROR("</error>"),
        RESPONSE("<response>"),END_RESPONSE("</response>");
        
        private String type;
        private Type(String type) {
            this.type = type;
        }
    }
    public static final String wrap(final String body, Type leftWrapper, Type rightWrapper){
        StringBuilder b = new StringBuilder();
        b.append(leftWrapper.type);
        b.append(body);
        b.append(rightWrapper.type);
        return b.toString();
    }
}
