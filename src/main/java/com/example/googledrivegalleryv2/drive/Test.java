package com.example.googledrivegalleryv2.drive;

import java.io.IOException;
import java.security.GeneralSecurityException;
import java.util.ArrayList;
import java.util.Arrays;

public class Test {
    public static void main(String[] args) {
        String filePath = "/Users/colesullivan/Downloads/circle1.png";

//        System.out.println(Arrays.toString(PropertiesUtility.arrayFromListString(
//                "")));
//        System.out.println(PropertiesUtility.arrayFromListString("").length);

        try {
            DriveConnection.start();
//            DriveController.createFolder("new folder yay");
//            DriveController.uploadFile(filePath,"1M1e7Fn63liOTOb_eisf3n86-uaiMqzPo");
//            System.out.println("file exists: " + DriveController.fileExists("1M1e7Fn63liOTOb_eisf3n86-uaiMqzPo"));
//            Gallery.readRootID();
//            System.out.println(Gallery.rootID);
            Gallery.start();

            System.out.println(Arrays.asList(Gallery.artistToIDsMap));
            System.out.println(Arrays.asList(Gallery.tagToIDsMap));
            System.out.println(Arrays.asList(Gallery.albumToIDsMap));
        } catch (IOException | GeneralSecurityException e) {
            throw new RuntimeException(e);
        }
    }
}
