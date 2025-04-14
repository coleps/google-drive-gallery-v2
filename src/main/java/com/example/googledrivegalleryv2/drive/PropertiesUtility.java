package com.example.googledrivegalleryv2.drive;

import com.google.api.services.drive.model.File;

import java.io.IOException;
import java.util.Arrays;
import java.util.Map;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public class PropertiesUtility {

    public static String[] getAppPropertyArray(String fileID, String property){
        return arrayFromListString(getAppProperty(fileID,property));
    }
    public static String getAppProperty(String fileID, String property){
        File file = Gallery.idToFileMap.get(fileID);
        if(file.getAppProperties()==null) return "";
        String s = file.getAppProperties().get(property);
        if(s != null) return capitalizeString(s);
        return "";
    }

    /**
     * Capitalizes the first letter of each word in a sentence
     */
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
        File file = Gallery.idToFileMap.get(fileID);
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

    /**
     * Returns an array of sentences derived from a comma separated string
     * For instance, "hi, what's up, yes" returns ["hi", "what's up", "yes"]
     * Takes care of errant commas and spaces
     */
    public static String[] arrayFromListString(String s){
        String[] a = s.split(",");
        return Arrays.stream(a).filter(element -> !element.isEmpty()).map(element -> {
            String[] words = element.split(" +");
            String wordsString = "";
            for (int i = 0; i < words.length; i++){
                if(words[i].isEmpty()) continue;
                wordsString += words[i];
                if(i < words.length-1) wordsString += " ";
            }
            return wordsString;
        }).toArray(String[]::new);
    }
}
