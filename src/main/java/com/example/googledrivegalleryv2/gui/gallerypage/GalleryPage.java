package com.example.googledrivegalleryv2.gui.gallerypage;

import javafx.scene.layout.BorderPane;

public class GalleryPage extends BorderPane {
    private static GalleryPage instance;
    public static GalleryPage getInstance(){
        if(instance == null) instance = new GalleryPage();
        return instance;
    }
    private GalleryPage(){
        setStyle("-fx-background-color: rgba(230, 230, 230, 1);");
        setLeft(SelectionArea.getInstance());
//        gallery.setCenter(Borders.wrap(imageArea).lineBorder().color(Color.WHITESMOKE).thickness(0).build().build());
        setCenter(ImageArea.getInstance());
        setTop(Header.getInstance());
    }
}
