package com.example.googledrivegalleryv2.gui.gallerypane;

import javafx.scene.layout.BorderPane;

public class GalleryPane extends BorderPane {
    private static GalleryPane instance;
    public static GalleryPane getInstance(){
        if(instance == null) instance = new GalleryPane();
        return instance;
    }
    private GalleryPane(){
        setLeft(SelectionArea.getInstance());
//        gallery.setCenter(Borders.wrap(imageArea).lineBorder().color(Color.WHITESMOKE).thickness(0).build().build());
        setCenter(ImageArea.getInstance());
        setTop(Header.getInstance());
    }
}
