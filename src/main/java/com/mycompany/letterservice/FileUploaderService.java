/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.mycompany.letterservice;

import com.cloudinary.Cloudinary;
import com.cloudinary.Transformation;
import com.cloudinary.utils.ObjectUtils;
import com.mycompany.letterservice.auth.json.Sound;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.util.Arrays;
import java.util.Map;
import javax.sound.sampled.AudioFileFormat;
import javax.sound.sampled.AudioSystem;
import javax.sound.sampled.UnsupportedAudioFileException;
import org.apache.log4j.Logger;
import org.apache.commons.io.FileUtils;
import org.tritonus.share.sampled.file.TAudioFileFormat;

/**
 *
 * @author nekres
 */
public class FileUploaderService {
    
    public static final Logger logger = Logger.getLogger(FileUploaderService.class.getName());
    public static final String TEMP_FILES = System.getProperty("java.io.tmpdir") + File.separator + "LetterService";
    public static final String CLOUD_NAME = "nekres";
    public static final String CLOUD_API_KEY = "264239233742566";
    public static final String API_SECRET = "4DnoOHNmHtuPX1CDczVWS_WYahw";
    public static final Cloudinary cloud = new Cloudinary(ObjectUtils.asMap("cloud_name",CLOUD_NAME,"api_key",CLOUD_API_KEY,
            "api_secret",API_SECRET));
    
    public String uploadImage(final InputStream input, String pictureName) throws IOException{
        logger.info("File uploading started.");
        
        File tmp = new File(TEMP_FILES);
        if(!tmp.exists())
            tmp.mkdir();
        File targetFile = new File(TEMP_FILES + File.separator + pictureName);
        targetFile.createNewFile();
        FileUtils.copyInputStreamToFile(input, targetFile);
        
        Map uploadResult = cloud.uploader().upload(targetFile, ObjectUtils.emptyMap());
          logger.info("::" + uploadResult.entrySet().iterator().next());
          logger.info(uploadResult.get("url").toString());
          
        targetFile.delete();
        logger.info("Uploaded.\n");
      return uploadResult.get("url").toString();
    }
    
    public Sound uploadMusic(final InputStream input, String filename, String author, String name) throws IOException, UnsupportedAudioFileException{
        logger.info("Music uploading started");
        
        File tmp = new File(TEMP_FILES);
        if(!tmp.exists())
            tmp.mkdir();
        File targetFile = new File(TEMP_FILES + File.separator + filename);
        targetFile.createNewFile();
        FileUtils.copyInputStreamToFile(input, targetFile);
       // String duration = getDuration(targetFile);
        Map uploadResult = cloud.uploader().upload(targetFile, 
        ObjectUtils.asMap("resource_type", "video"));
        logger.info(uploadResult.get("url").toString());
        Sound sound = new Sound();
        sound.setName(name);
        sound.setAuthor(author);
        sound.setUrl(uploadResult.get("url").toString());
        sound.setDuration(getDuration(targetFile));
        targetFile.delete();
        return sound;
    }
    
    private final String getDuration(final File file) throws IOException, UnsupportedAudioFileException{
        AudioFileFormat fileFormat = AudioSystem.getAudioFileFormat(file);
        String duration = null;
        if(fileFormat instanceof TAudioFileFormat){
            Map<?,?> properties = ((TAudioFileFormat)fileFormat).properties();
            String key = "duration";
            Long ms = (Long)properties.get(key);
            int mili = (int) (ms / 1000);
            int seconds = (mili / 1000) % 60;
            int min = (mili / 1000) / 60;
            duration = min + "." + seconds;
            
        }
       return duration;
    }
}
