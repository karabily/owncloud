package com.supinfo.ourcloud.web.rest;

import com.supinfo.ourcloud.domain.OurFile;
import com.supinfo.ourcloud.repository.UploadFile;
import com.supinfo.ourcloud.security.SecurityUtils;
import com.supinfo.ourcloud.service.UserService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.io.*;
import java.util.ArrayList;
import java.util.Optional;

import org.apache.commons.net.ftp.FTPClient;
import org.apache.commons.net.ftp.FTPReply;

@RestController
@RequestMapping("/api")
public class OurFileRessource {

    private final Logger log = LoggerFactory.getLogger(OurFileRessource.class);
    private final UploadFile up;
    @Autowired
    private final UserResource userService;

    private static final int BUFFER_SIZE = 4096;
    private static final String HOST="localhost";
    private static final String USER="karabily";
    private static final String PASSWORD="z9QwkAWR";
    private static final String FILEPATH="c:/Users/Karabily/Desktop/Capture.png";
    private static final String UPLOADPATH="Capture.png";
    private static  String FTPURL="ftp://%s:%s@%s/%s;type=i";

    public OurFileRessource(UploadFile up, UserResource userService) {
        this.up = up;
        this.userService = userService;
    }


    @GetMapping("/upload")
    public void load() {
        ArrayList<OurFile> file = up.load();
        System.out.println("files: "+file.toString());
        Optional<String> user = SecurityUtils.getCurrentUserLogin();
        FTPURL = String.format(FTPURL, USER, PASSWORD, HOST, UPLOADPATH);
        System.out.println("Upload URL: " + FTPURL);
        FTPClient ftpClient = new FTPClient();

        try {

            ftpClient.connect(HOST, 21);
            showServerReply(ftpClient);
            int replyCode = ftpClient.getReplyCode();
            if (!FTPReply.isPositiveCompletion(replyCode)) {
                System.out.println("Operation failed. Server reply code: " + replyCode);
                return;
            }
            boolean success = ftpClient.login(USER, PASSWORD);
            showServerReply(ftpClient);
            if (!success) {
                System.out.println("Could not login to the server");
                return;
            }else{
                System.out.println("great");

            }
            String dirToCreate = "/upload"+userService.getUserWithAuthorities().get().getLogin();
            success = ftpClient.makeDirectory(dirToCreate);
            showServerReply(ftpClient);
            if (success) {
                System.out.println("Successfully created directory: " + dirToCreate);
            } else {
                System.out.println("Failed to create directory. See server's reply.");
            }
            // logs out
            ftpClient.logout();
            ftpClient.disconnect();
//            URL url = new URL(FTPURL);
//            URLConnection conn = url.openConnection();
//            OutputStream outputStream = conn.getOutputStream();
//            FileInputStream inputStream = new FileInputStream(FILEPATH);
//
//            byte[] buffer = new byte[BUFFER_SIZE];
//            int bytesRead = -1;
//            while ((bytesRead = inputStream.read(buffer)) != -1) {
//                outputStream.write(buffer, 0, bytesRead);
//            }
//
//            inputStream.close();
//            outputStream.close();

            System.out.println("File uploaded");
        } catch (IOException ex) {
            ex.printStackTrace();
        }


    }


    private static void showServerReply(FTPClient ftpClient) {
        String[] replies = ftpClient.getReplyStrings();
        if (replies != null && replies.length > 0) {
            for (String aReply : replies) {
                System.out.println("SERVER: " + aReply);
            }
        }
    }
}
