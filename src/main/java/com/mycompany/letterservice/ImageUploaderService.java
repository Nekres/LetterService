/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.mycompany.letterservice;

import com.cloudinary.Cloudinary;
import com.cloudinary.utils.ObjectUtils;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.util.Map;
import java.util.logging.Logger;
import org.apache.commons.io.FileUtils;

/**
 *
 * @author nekres
 */
public class ImageUploaderService {
    public static final Logger logger = Logger.getLogger(ImageUploaderService.class.getName());
    public static final String TEMP_FILES = System.getProperty("java.io.tmpdir") + File.separator + "LetterService";
    public static final String CLOUD_NAME = "nekres";
    public static final String CLOUD_API_KEY = "264239233742566";
    public static final String API_SECRET = "4DnoOHNmHtuPX1CDczVWS_WYahw";
    public static final Cloudinary cloud = new Cloudinary(ObjectUtils.asMap("cloud_name",CLOUD_NAME,"api_key",CLOUD_API_KEY,
            "api_secret",API_SECRET));
    
    public static String upload(final InputStream input, int size, String pictureName) throws IOException{
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
}
