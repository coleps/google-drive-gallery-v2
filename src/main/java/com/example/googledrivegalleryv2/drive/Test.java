package com.example.googledrivegalleryv2.drive;

import java.io.IOException;
import java.security.GeneralSecurityException;

public class Test {
    public static void main(String[] args) {
        String filePath = "/Users/colesullivan/Downloads/circle1.png";
        try {
            DriveConnection.start();
//            DriveController.createFolder("new folder yay");
//            DriveController.uploadFile(filePath,"1M1e7Fn63liOTOb_eisf3n86-uaiMqzPo");
//            System.out.println("file exists: " + DriveController.fileExists("1M1e7Fn63liOTOb_eisf3n86-uaiMqzPo"));
//            Gallery.readRootID();
//            System.out.println(Gallery.rootID);
            Gallery.start();
        } catch (IOException | GeneralSecurityException e) {
            throw new RuntimeException(e);
        }
    }
}
