package com.example.googledrivegalleryv2.drive;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.List;

public class FileTransfer {
    private static final String LOCAL_ROOT_FOLDER_PATH = "rootID.txt";
    public static String localRootPath;

    private static void readLocalRoot() throws IOException {
        java.io.File localRoot = new java.io.File(LOCAL_ROOT_FOLDER_PATH);
        localRoot.createNewFile();
        localRootPath = Files.readString(Path.of(LOCAL_ROOT_FOLDER_PATH));
    }

    /**
     * Get the paths of all images in the local root folder
     */
    public static List<String> getAllLocalImagePaths() {
        List<String> paths = new ArrayList<>();

        if(localRootPath == null || localRootPath.isEmpty()) return paths;

        Path path = Path.of(localRootPath);
        try {
            Files.walk(path).forEach(p ->{
                if(p.equals(path)) return;
                if(Files.isDirectory(p)) return;
                if(p.toString().contains("DS_Store")) return;

                if(FileUtility.fileIsImage(p.toString())) paths.add(p.toString());
            });
        } catch (IOException e) {
            return paths;
        }

        return paths;
    }

}
