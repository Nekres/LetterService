/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.mycompany.letterservice;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 *
 * @author nekres
 */
public class SessionManager {
    
    public static String getCookieValue(HttpServletRequest request, String name){
        Cookie[] cookie = request.getCookies();
        if(cookie != null){
            for(Cookie c : cookie){
                if(name.equals(c.getName())){
                    return c.getValue();
                }
            }
        }
        return null;
    }
    
    public static void addCookie(HttpServletResponse response, String name, String value, int maxAge){
        Cookie cookie = new Cookie(name, value);
        cookie.setPath("/");
        cookie.setMaxAge(maxAge);
        response.addCookie(cookie);
    }
    
    public static void removeCookie(HttpServletResponse response, String name){
        addCookie(response, name, null, 0);
    }
}
