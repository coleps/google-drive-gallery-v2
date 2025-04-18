package com.example.googledrivegalleryv2.drive;

import com.google.api.client.googleapis.json.GoogleJsonResponseException;
import com.google.api.client.http.FileContent;
import com.google.api.services.drive.model.File;
import com.google.api.services.drive.model.Permission;

import java.io.IOException;
import java.nio.file.Path;
import java.util.Collections;
import java.util.Map;

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
            uploadFile(p.toString(),folderPath);
        });
    }


    public static String uploadFile(java.io.File localFile, String parentID, Map<String, String> appProperties, boolean delete){
        File fileMetadata = new File();
        fileMetadata.setName(localFile.getName());
        if(parentID != null) fileMetadata.setParents(Collections.singletonList(parentID));
        fileMetadata.setAppProperties(appProperties);

        String mimeType = FileUtility.getMimeType(localFile.getPath());
        FileContent mediaContent = new FileContent(mimeType, localFile);
        try {
            File uploadedFile = DriveConnection.service.files().create(fileMetadata, mediaContent)
                    .setFields("id")
                    .execute();
            System.out.println("Uploaded File: " + localFile.getName());

            setSharePublic(uploadedFile.getId());

            if(delete) FileUtility.deleteFile(localFile.getPath());

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

    /**
     * Uploads file to Google Drive from file path
     * @param path
     * @param parentID parent folder ID
     * @return new file ID or null if file could not be uploaded
     */
    public static String uploadFile(String path, String parentID) {
        java.io.File file = new java.io.File(path);
        File fileMetadata = new File();
        fileMetadata.setName(file.getName());
        if(parentID != null) fileMetadata.setParents(Collections.singletonList(parentID));

        String mimeType = FileUtility.getMimeType(path);
        FileContent mediaContent = new FileContent(mimeType, file);
        try {
            File uploadedFile = DriveConnection.service.files().create(fileMetadata, mediaContent)
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

    /**
     * Check if file exists in Google Drive and is not in the trash
     * @param id file id
     */
    public static boolean fileExists(String id){
        try{
            File file = DriveConnection.service.files().get(id)
                    .setFields("id, name, mimeType, trashed")
                    .execute();
            return Boolean.FALSE.equals(file.getTrashed());
        } catch (GoogleJsonResponseException e) {
            if (e.getStatusCode() == 404) {
                return false; // Folder doesn't exist or no access
            } else {
                e.printStackTrace();
                return false;
            }
        } catch (IOException e) {
            e.printStackTrace();
            return false;
        }
//        return true;
    }

    /**
     * Create Drive folder without parent folder
     */
    public static String createFolder(String name){
        return createFolder(name, null);
    }

    /**
     * Create Drive folder
     * @param name
     * @param parentID
     * @return
     * @throws IOException
     */
    public static String createFolder(String name, String parentID) {
        File fileMetadata = new File();
        fileMetadata.setName(name);
        if(parentID != null) fileMetadata.setParents(Collections.singletonList(parentID));
        fileMetadata.setMimeType("application/vnd.google-apps.folder");
        try {
            File file = DriveConnection.service.files().create(fileMetadata)
                    .setFields("id")
                    .execute();
            System.out.println("Folder ID: " + file.getId());

            setSharePublic(file.getId());
            return file.getId();
        } catch (GoogleJsonResponseException e) {
            // TODO(developer) - handle error appropriately
            System.err.println("Unable to create folder: " + e.getDetails());
            return null;
        } catch (IOException e){
            System.err.println("Unable to upload file (IO exception): " + e.getMessage());
            return null;
        }
    }

    /**
     * Make file/folder viewable to anyone with the link
     * @throws IOException
     */
    public static void setSharePublic(String id) throws IOException {
        Permission newPermission = new Permission();
        newPermission.setType("anyone");
        newPermission.setRole("reader");
        DriveConnection.service.permissions().create(id,newPermission).execute();
    }
}
