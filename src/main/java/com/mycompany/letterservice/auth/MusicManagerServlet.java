/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.mycompany.letterservice.auth;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.mycompany.letterservice.DatabaseManager;
import com.mycompany.letterservice.FileUploaderService;
import static com.mycompany.letterservice.auth.RegistrationServlet.PICTURE;
import com.mycompany.letterservice.entity.Music;
import com.mycompany.letterservice.entity.User;
import com.mycompany.letterservice.exceptions.BadPropertiesException;
import com.mycompany.letterservice.exceptions.LetterServiceException;
import java.io.IOException;
import java.io.Writer;
import java.util.Date;
import java.util.logging.Level;
import javax.servlet.ServletContext;
import javax.servlet.ServletException;
import javax.servlet.annotation.MultipartConfig;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import javax.servlet.http.Part;
import javax.sound.sampled.UnsupportedAudioFileException;
import org.apache.log4j.Logger;
import org.hibernate.SessionFactory;

/**
 *
 * @author nrs
 */
@WebServlet("/music")
@MultipartConfig
public class MusicManagerServlet extends HttpServlet{
    
    public static final String MUSIC_FILE = "music";
    
    private static final Logger logger = Logger.getLogger(MusicManagerServlet.class.getName());
    public static final FileUploaderService imageService = new FileUploaderService();

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        logger.info("Uploading new music...");
        ServletContext context = req.getServletContext();
        
        HttpSession session = req.getSession(false);
        
        if(session == null){
            logger.info("No session. Uploading abort");
            
        }
        
        Part music = req.getPart(MUSIC_FILE);
        
        if(music.getName().endsWith(".mp3")){
            String ex = "Forbidden file. Format must be .mp3";
            logger.info(ex);
            throw new BadPropertiesException(ex);
        }
        
        Writer out = resp.getWriter();
        String audioUrl = null;
        try {
            audioUrl = imageService.uploadMusic(music.getInputStream(), music.getSubmittedFileName());
        } catch (UnsupportedAudioFileException ex) {
            java.util.logging.Logger.getLogger(MusicManagerServlet.class.getName()).log(Level.SEVERE, null, ex);
        }
        
        SessionFactory sessionFactory = (SessionFactory) context.getAttribute("sessionFactory");
        DatabaseManager manager = new DatabaseManager(sessionFactory);
        manager.beginTransaction();
        
        User currentUser = (User)session.getAttribute("curr_user");
        Music m = new Music(currentUser,music.getName(),audioUrl,new Date());
        
        manager.persistObj(m);
        
        manager.commitAndClose();
        
        ObjectMapper mapper = new ObjectMapper();
        String json = mapper.writeValueAsString(audioUrl);
        out.write(json);
        logger.info("Saving to database: " + m.toString());
    }

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        super.doGet(req, resp); //To change body of generated methods, choose Tools | Templates.
    }
    
    
    
}
