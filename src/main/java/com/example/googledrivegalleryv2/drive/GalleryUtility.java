package com.example.googledrivegalleryv2.drive;

import com.example.googledrivegalleryv2.gui.ImageUtility;
import com.google.api.services.drive.model.File;
import javafx.application.Platform;
import javafx.scene.image.ImageView;

import java.io.IOException;
import java.util.Map;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public class GalleryUtility {
    public static String getAppProperty(String fileID, String property){
        File file = Gallery.idMap.get(fileID);
        if(file.getAppProperties()==null) return "";
        String s = file.getAppProperties().get(property);
        if(s != null) return capitalizeString(s);
        return "";
    }

    public static String capitalizeString(String s){
        return Stream.of(s.trim().split("\\s"))
                .filter(word -> word.length() > 0)
                .map(word -> word.substring(0, 1).toUpperCase() + word.substring(1))
                .collect(Collectors.joining(" "));
    }

    public static void setAppProperties(String fileID, Map<String, String> map){
        map.forEach((entry, value) -> map.put(entry,value.toLowerCase()));
        File updatedFile = new File();
        updatedFile.setAppProperties(map);
        File file = Gallery.idMap.get(fileID);
        file.setAppProperties(map);

        new Thread(()->{
            try {
                DriveConnection.service
                        .files()
                        .update(fileID, updatedFile)
                        .setFields("id, appProperties")
                        .execute();
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
        }).start();
    }
}
