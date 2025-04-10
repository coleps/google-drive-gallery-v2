package com.example.googledrivegalleryv2.drive;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;

public class FileUtility {
    /**
     * Returns progressively built up paths
     * Eg "a/b/c" returns ["a","a/b","a/b/c"]
     */
    public static String[] pathSteps(String path){
        String[] pathParts = path.split("/");
        String[] paths = new String[pathParts.length];
        paths[0] = pathParts[0];
        for (int i = 1; i < pathParts.length; i++){
            paths[i] = paths[i-1] + "/" + pathParts[i];
        }
        return paths;
    }

    /**
     * Returns the file or folder name from a path
     */
    public static String getFileOrFolderName(String path){
        String name = path;
        if(name.contains("/")) name = name.substring(name.lastIndexOf('/')+1);
        return name;
    }

    /**
     * Get mime type of local file
     * @return returns null if mime type cannot be found, ot IO error
     */
    public static String getMimeType(String filePath){
        Path path = Path.of(filePath);
        try {
            return Files.probeContentType(path);
        } catch (IOException e) {
            return null;
        }
    }

    /**
     * Returns if local file is image
     */
    public static boolean fileIsImage(String filePath){
        String mimeType = getMimeType(filePath);
        return mimeType != null && mimeType.startsWith("image/");
    }

    /**
     * Delete local file
     */
    public static void deleteFile(String filePath){
        try {
            Files.delete(Path.of(filePath));
        } catch (IOException e) {
            System.out.println("Could not delete file " + filePath);
            throw new RuntimeException(e);
        }
    }
}
