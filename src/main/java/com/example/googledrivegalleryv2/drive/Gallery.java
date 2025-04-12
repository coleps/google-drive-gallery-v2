package com.example.googledrivegalleryv2.drive;

import com.google.api.services.drive.model.File;
import com.google.api.services.drive.model.FileList;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class Gallery {
    private static final String ROOT_FOLDER_ID_PATH = "rootID.txt";

    // Gallery Root Folder ID
    public static String rootID;
    // All images in gallery
    public static ArrayList<File> files = new ArrayList<>();
    public static HashMap<String, ArrayList<File>> artistMap = new HashMap<>();
    public static HashMap<String, ArrayList<File>> tagMap = new HashMap<>();
    public static HashMap<String, ArrayList<File>> albumMap = new HashMap<>();
//    public static HashMap<File, ArtData> artMap = new HashMap<>();

    public static void start() throws IOException {
        readRootID();
        loadGalleryFiles();
    }

    private static void readRootID() throws IOException {
        java.io.File rootIDFile = new java.io.File(ROOT_FOLDER_ID_PATH);
        rootIDFile.createNewFile();
        rootID = Files.readString(Path.of(ROOT_FOLDER_ID_PATH));
        if(rootID.isEmpty() || !DriveController.fileExists(rootID)){
            createRootFolder();
        }
        rootID = Files.readString(Path.of(ROOT_FOLDER_ID_PATH));
    }

    private static void createRootFolder() throws IOException {
        String rootID = DriveController.createFolder("Google Drive Gallery");
        java.io.File rootIDFile = new java.io.File(ROOT_FOLDER_ID_PATH);
        if(!rootIDFile.exists()){
            rootIDFile.createNewFile();
        }
        Files.writeString(Path.of(ROOT_FOLDER_ID_PATH), rootID);
    }


    public static void loadGalleryFiles() throws IOException {
        String q = "mimeType contains 'image/' and trashed = false and '" + rootID + "' in parents";
        FileList result = DriveConnection.service.files().list()
                .setQ(q)
                .setFields("nextPageToken, files(id, name, description, thumbnailLink)")
                .setPageSize(1000)
                .execute();
        List<File> files = result.getFiles();

        // loop to collect all pages
        while (true){
            result = DriveConnection.service.files().list()
                    .setQ(q)
                    .setPageToken(result.getNextPageToken())
                    .setFields("nextPageToken, files(id, name, description, thumbnailLink)")
                    .setPageSize(1000)
                    .execute();
            if(result.getNextPageToken() == null) break;
            files.addAll(result.getFiles());
        }

        Gallery.files = (ArrayList<File>) files;

//        images.forEach(file -> System.out.println("Img: " + file.getName()));

//        images.forEach(file -> {
//            String description = file.getDescription();
//            String[] artist = description.split("Tags:")
//        });
    }


}
