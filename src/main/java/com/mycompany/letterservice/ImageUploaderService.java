/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.mycompany.letterservice;

import com.cloudinary.Cloudinary;
import com.cloudinary.utils.ObjectUtils;
import java.io.*;
import java.nio.ByteBuffer;
import java.nio.channels.Channels;
import java.nio.channels.ReadableByteChannel;
import java.util.Map;
import java.util.logging.Logger;
import javax.servlet.http.Part;

/**
 *
 * @author nekres
 */
public class ImageUploaderService {
    public static final Logger logger = Logger.getLogger(ImageUploaderService.class.getName());
    public static final String CLOUD_NAME = "nekres";
    public static final String CLOUD_API_KEY = "264239233742566";
    public static final String API_SECRET = "4DnoOHNmHtuPX1CDczVWS_WYahw";
    public static final Cloudinary cloud = new Cloudinary(ObjectUtils.asMap("cloud_name",CLOUD_NAME,"api_key",CLOUD_API_KEY,
            "api_secret",API_SECRET));
    
    public static void upload(final InputStream input, Part picture) throws IOException{
      byte buff[] = new byte[input.available()];
      input.read(buff);
      
      File targetFile = new File("/home/nekres/temp/1.jpg");
      if(!targetFile.exists()){
          targetFile.getParentFile().mkdirs();
          targetFile.createNewFile();
      }
      OutputStream out = new FileOutputStream(targetFile);
      out.write(buff);
      out.flush();
      
      Map uploadResult = cloud.uploader().upload(targetFile, ObjectUtils.emptyMap());
      logger.info(uploadResult.get("url").toString());
      logger.info("\n\n\nfinished\n");
    }
}
