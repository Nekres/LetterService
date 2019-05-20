/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.mycompany.letterservice.auth;

import com.mycompany.letterservice.*;
import com.mycompany.letterservice.auth.websocket.ChatEndPoint;
import com.mycompany.letterservice.entity.User;
import com.mycompany.letterservice.exceptions.NoSuchUserException;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.Date;
import java.util.HashMap;
import org.apache.log4j.Logger;
import javax.servlet.*;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.*;
import org.hibernate.SessionFactory;
import org.slf4j.LoggerFactory;

/**
 *
 * @author nekres
 */
@WebServlet("/login")
public class LoginServlet extends HttpServlet {
    public static final int COOKIE_EXPIRE_TIME = 30*60;
    public static final int COOKIE_NO_EXPIRE = -1;
    public static final String SESSION_PARAM = "session";
    public static final String ADMIN_LOGIN = "NEKRES";

    //public static final String AD
    
    private static final Logger logger = Logger.getLogger(LoginServlet.class.getName());
    private final org.slf4j.Logger telegramLogger = LoggerFactory.getLogger("Telegram Notification");

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        ServletContext ctx = req.getServletContext();
        if(req.getParameter("play") != null){
            telegramLogger.info(interceptRequest(req));
            return;
        }
        String session = req.getParameter(SESSION_PARAM);
        if(session == null){
            resp.setStatus(HttpServletResponse.SC_BAD_REQUEST);
            return;
        }
        HashMap<String, HttpSession> activeSession = (HashMap<String, HttpSession>)ctx.getAttribute("activeSession");
        telegramLogger.info("Login attempt. Adress " + req.getRemoteAddr());
        logger.info("Login attempt...");
        logger.info("Session id: " + session);
        if(!activeSession.containsKey(session)){
            resp.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
            logger.info("Login attempt result: Unathorized.");
        }else{
            logger.info("Login attempt result: Success.");
        }
    }
    
    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        req.setCharacterEncoding("UTF-8");
        resp.setCharacterEncoding("UTF-8");
        logger.info(req.getServletPath());
        String email = req.getParameter(RegistrationServlet.USER_EMAIL);
        String password = req.getParameter(RegistrationServlet.USER_PASSWORD);
        ServletContext ctx = req.getServletContext();
        SessionFactory sessionFactory = (SessionFactory)ctx.getAttribute("sessionFactory");
        DatabaseManager manager = new DatabaseManager(sessionFactory);

        try {
            manager.beginTransaction();
            logger.info("Transaction in /login executing");
            
            User user = manager.getUser(email, password);
            HttpSession session = req.getSession();
            session.setAttribute("user", user.getName());
            session.setAttribute("curr_u_id", user.getId());
            session.setAttribute(("curr_user"), user);
            
            HashMap<String, HttpSession> activeSession = (HashMap<String, HttpSession>)ctx.getAttribute("activeSession");
            activeSession.put(session.getId(), session);
            
            SessionManager.addCookie(resp, "session_id", session.getId(), COOKIE_NO_EXPIRE);
            SessionManager.addCookie(resp, "user_id", Integer.toString(user.getId()), COOKIE_NO_EXPIRE);
            SessionManager.addCookie(resp, "user_name", user.getName(), COOKIE_NO_EXPIRE);
            SessionManager.addCookie(resp, "user_surname", user.getSurname(), COOKIE_NO_EXPIRE);
            SessionManager.addCookie(resp, "user_photo", user.getPhotoUrl(), COOKIE_NO_EXPIRE);
            
            user.setLastOnline(new Date());
            
            resp.sendRedirect(resp.encodeRedirectURL("SuccessLogging.html"));
            
            
        } catch (NoSuchUserException exception) {
            RequestDispatcher dispatcher = ctx.getRequestDispatcher("/index.html");
            PrintWriter out = resp.getWriter();
            out.println("<font color=red>ERROR</font>");
            resp.setStatus(HttpServletResponse.SC_BAD_REQUEST);
            dispatcher.include(req, resp);
        } finally {
            manager.commitAndClose();
        }
    }

    private String interceptRequest(HttpServletRequest req){
        return "Intercepted. IP: " + getClientIpAddr(req) + " OS: " + getClientOS(req) + " Browser: " + getClientBrowser(req)
    }
    //http://stackoverflow.com/a/18030465/1845894
    public String getClientIpAddr(HttpServletRequest request) {
        String ip = request.getHeader("X-Forwarded-For");
        if (ip == null || ip.length() == 0 || "unknown".equalsIgnoreCase(ip)) {
            ip = request.getHeader("Proxy-Client-IP");
        }
        if (ip == null || ip.length() == 0 || "unknown".equalsIgnoreCase(ip)) {
            ip = request.getHeader("WL-Proxy-Client-IP");
        }
        if (ip == null || ip.length() == 0 || "unknown".equalsIgnoreCase(ip)) {
            ip = request.getHeader("HTTP_CLIENT_IP");
        }
        if (ip == null || ip.length() == 0 || "unknown".equalsIgnoreCase(ip)) {
            ip = request.getHeader("HTTP_X_FORWARDED_FOR");
        }
        if (ip == null || ip.length() == 0 || "unknown".equalsIgnoreCase(ip)) {
            ip = request.getRemoteAddr();
        }
        return ip;
    }

    //http://stackoverflow.com/a/18030465/1845894
    public String getClientOS(HttpServletRequest request) {
        final String browserDetails = request.getHeader("User-Agent");

        //=================OS=======================
        final String lowerCaseBrowser = browserDetails.toLowerCase();
        if (lowerCaseBrowser.contains("windows")) {
            return "Windows";
        } else if (lowerCaseBrowser.contains("mac")) {
            return "Mac";
        } else if (lowerCaseBrowser.contains("x11")) {
            return "Unix";
        } else if (lowerCaseBrowser.contains("android")) {
            return "Android";
        } else if (lowerCaseBrowser.contains("iphone")) {
            return "IPhone";
        } else {
            return "UnKnown, More-Info: " + browserDetails;
        }
    }

    //http://stackoverflow.com/a/18030465/1845894
    public String getClientBrowser(HttpServletRequest request) {
        final String browserDetails = request.getHeader("User-Agent");
        final String user = browserDetails.toLowerCase();

        String browser = "";

        //===============Browser===========================
        if (user.contains("msie")) {
            String substring = browserDetails.substring(browserDetails.indexOf("MSIE")).split(";")[0];
            browser = substring.split(" ")[0].replace("MSIE", "IE") + "-" + substring.split(" ")[1];
        } else if (user.contains("safari") && user.contains("version")) {
            browser = (browserDetails.substring(browserDetails.indexOf("Safari")).split(" ")[0]).split(
                    "/")[0] + "-" + (browserDetails.substring(
                    browserDetails.indexOf("Version")).split(" ")[0]).split("/")[1];
        } else if (user.contains("opr") || user.contains("opera")) {
            if (user.contains("opera"))
                browser = (browserDetails.substring(browserDetails.indexOf("Opera")).split(" ")[0]).split(
                        "/")[0] + "-" + (browserDetails.substring(
                        browserDetails.indexOf("Version")).split(" ")[0]).split("/")[1];
            else if (user.contains("opr"))
                browser = ((browserDetails.substring(browserDetails.indexOf("OPR")).split(" ")[0]).replace("/",
                        "-")).replace(
                        "OPR", "Opera");
        } else if (user.contains("chrome")) {
            browser = (browserDetails.substring(browserDetails.indexOf("Chrome")).split(" ")[0]).replace("/", "-");
        } else if ((user.indexOf("mozilla/7.0") > -1) || (user.indexOf("netscape6") != -1) || (user.indexOf(
                "mozilla/4.7") != -1) || (user.indexOf("mozilla/4.78") != -1) || (user.indexOf(
                "mozilla/4.08") != -1) || (user.indexOf("mozilla/3") != -1)) {
            //browser=(userAgent.substring(userAgent.indexOf("MSIE")).split(" ")[0]).replace("/", "-");
            browser = "Netscape-?";

        } else if (user.contains("firefox")) {
            browser = (browserDetails.substring(browserDetails.indexOf("Firefox")).split(" ")[0]).replace("/", "-");
        } else if (user.contains("rv")) {
            browser = "IE";
        } else {
            browser = "UnKnown, More-Info: " + browserDetails;
        }

        return browser;
    }

    public String getUserAgent(HttpServletRequest request) {
        return request.getHeader("User-Agent");
    }

    }
