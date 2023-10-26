package com.example.upload.controller;

import java.io.BufferedInputStream;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;

import org.apache.commons.net.ftp.FTP;
import org.apache.commons.net.ftp.FTPClient;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

@RestController
@RequestMapping("/upload")
public class UploadController {

    @GetMapping("")
    public String Images() {
        return "upload";
    }

    @Value("${FTP_ADDRESS}")
    private String ftpAddress;

    @Value("${FTP_USER}")
    private String ftpUser;

    @Value("${FTP_PSW}")
    private String ftpPassword;

    @PostMapping("/image")
    public String UploadImages(@RequestParam("file") MultipartFile file, RedirectAttributes redirectAttributes) {

        int port = 21;
        FTPClient con = null;

        FTPClient ftpClient = new FTPClient();
        try {
            ftpClient.connect(ftpAddress, port);
            ftpClient.login(ftpUser, ftpPassword);
            ftpClient.enterLocalPassiveMode();
            ftpClient.setFileType(FTP.BINARY_FILE_TYPE);

            InputStream inputStream = new BufferedInputStream(file.getInputStream());
            System.out.println("Start uploading first file");

            boolean done = ftpClient.storeFile("/test/test2.jpg", inputStream);
            if (done) {
                System.out.println("The first file is uploaded successfully.");
            }

        } catch (IOException ex) {
            System.out.println("Error: " + ex.getMessage());
            ex.printStackTrace();
        } finally {
            try {
                if (ftpClient.isConnected()) {
                    ftpClient.logout();
                    ftpClient.disconnect();
                }
            } catch (IOException ex) {
                ex.printStackTrace();
            }
        }

        return "upload";
    }

}
