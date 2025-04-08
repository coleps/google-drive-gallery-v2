package com.example.googledrivegalleryv2.drive;

import com.google.api.client.googleapis.json.GoogleJsonResponseException;
import com.google.api.client.http.FileContent;
import com.google.api.services.drive.model.File;
import com.google.api.services.drive.model.Permission;

import java.io.IOException;
import java.nio.file.Path;
import java.util.Collections;

public class DriveController {

    /**
     * Upload all files from a local folder to Google Drive
     * @param folderPath local folder path
     * @throws IOException
     */
    public static void uploadAllImages(String folderPath) throws IOException {
        Path path = Path.of(folderPath);
        java.nio.file.Files.walk(path).forEach(p ->{
            if(p.equals(path)) return;
            if(java.nio.file.Files.isDirectory(p)) return;
            if(p.toString().contains("DS_Store")) return;
            try {
                addFileFromPath(p.toString(),folderPath);
            } catch (IOException e) {
                e.printStackTrace();
            }
        });
    }

    public static String addFileFromPath(String filePath, String folderPath) throws IOException {
        String localRelativePath = filePath.replace(folderPath + "/","");
//        if(localRelativePath.charAt(0)=='/') localRelativePath = localRelativePath.substring(1);
        localRelativePath = "GDriveGallery/" + localRelativePath;
        String name = localRelativePath;
        if(name.contains("/")) name = name.substring(name.lastIndexOf('/')+1);

        localRelativePath = localRelativePath.substring(0,localRelativePath.lastIndexOf('/'));

        String[] iterativePaths = pathSteps(localRelativePath);
        String parentID = Hierarchy.hierarchy.rootID;
        boolean needsNewFolders = false; // boolean to prevent needing to reload hierarchy every loop iter
        for(String path : iterativePaths){
            String id = Hierarchy.hierarchy.getFolderID(path);
            System.out.println(path + " id: " + id);
            if(needsNewFolders || id == null) {
                parentID = createFolder(fileOrFolderName(path),parentID);
                needsNewFolders = true;
            }
            else parentID = id;
        }


        String id = uploadFile(filePath,name,parentID);


        Hierarchy.loadHeirarchy();

        return id;
    }

    /**
     * Uploads file to Google Drive
     * @param path
     * @param name
     * @param parentID parent folder ID
     * @return new file ID or null if file could not be uploaded
     */
    public static String uploadFile(String path, String name, String parentID) {
        java.io.File file = new java.io.File(path);
        File fileMetadata = new File();
        fileMetadata.setName(file.getName());
        fileMetadata.setParents(Collections.singletonList(parentID));

        String mimeType = FileUtility.getMimeType(path);
        FileContent mediaContent = new FileContent(mimeType, file);
        try {
            File uploadedFile = DriveQuickstart.service.files().create(fileMetadata, mediaContent)
                    .setFields("id")
                    .execute();
            System.out.println("Uploaded File: " + file.getName());

            setSharePublic(uploadedFile.getId());

            return uploadedFile.getId();
        } catch (GoogleJsonResponseException e) {
            // TODO(developer) - handle error appropriately
            System.err.println("Unable to upload file: " + e.getDetails());
            return null;
        } catch (IOException e){
            System.err.println("Unable to upload file (IO exception): " + e.getMessage());
            return null;
        }

    }

    public static String createFolder(String name, String parentID) throws IOException {
        File fileMetadata = new File();
        fileMetadata.setName(name);
        if(parentID != null) fileMetadata.setParents(Collections.singletonList(parentID));
        fileMetadata.setMimeType("application/vnd.google-apps.folder");
        try {
            File file = DriveQuickstart.service.files().create(fileMetadata)
                    .setFields("id")
                    .execute();
            System.out.println("Folder ID: " + file.getId());

            setSharePublic(file.getId());
            return file.getId();
        } catch (GoogleJsonResponseException e) {
            // TODO(developer) - handle error appropriately
            System.err.println("Unable to create folder: " + e.getDetails());
            throw e;
        }
    }

    public static void setSharePublic(String id) throws IOException {
        Permission newPermission = new Permission();
        newPermission.setType("anyone");
        newPermission.setRole("reader");
        DriveQuickstart.service.permissions().create(id,newPermission).execute();
    }
}
