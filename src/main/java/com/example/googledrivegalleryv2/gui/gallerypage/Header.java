package com.example.googledrivegalleryv2.gui.gallerypage;

import com.example.googledrivegalleryv2.drive.Gallery;
import com.example.googledrivegalleryv2.gui.App;
import com.example.googledrivegalleryv2.gui.ImageUtility;
import io.github.palexdev.materialfx.controls.MFXTextField;
import io.github.palexdev.materialfx.enums.FloatMode;
import javafx.geometry.Pos;
import javafx.scene.image.ImageView;
import javafx.scene.layout.HBox;
import javafx.scene.layout.StackPane;
import javafx.scene.paint.Color;
import javafx.scene.shape.Rectangle;

import java.io.IOException;


public class Header extends StackPane {
    private double height = 50;
    private static Header instance;
    public static Header getInstance(){
        if(instance == null) instance = new Header();
        return instance;
    }

    private Header(){
        setPrefHeight(height);
        setStyle("-fx-background-color: whitesmoke;");
        setStyle("-fx-background-color: rgba(230, 230, 230, 1);");
        HBox content = new HBox();
        content.setSpacing(20);
        content.setPrefHeight(height);
        content.setAlignment(Pos.CENTER);
        MFXTextField searchbar = new MFXTextField();
        searchbar.getStyleClass().add("searchbar");
        searchbar.setPrefWidth(500);
        searchbar.setLeadingIcon(new Rectangle(10,10,Color.TRANSPARENT));
        searchbar.setAnimated(false);
        searchbar.setFloatMode(FloatMode.DISABLED);

        HBox uploadButton = new HBox();
        uploadButton.setAlignment(Pos.CENTER);
        uploadButton.setMaxHeight(30);
        uploadButton.setPrefWidth(30);
        ImageView upload = ImageUtility.getImageView(Header.class.getResource("/uploadfolder.png").toExternalForm(),30);
        ImageUtility.colorImg(upload, Color.GREY);
        uploadButton.getStyleClass().add("image-button");
        uploadButton.getChildren().add(upload);

        HBox refreshButton = new HBox();
        refreshButton.setAlignment(Pos.CENTER);
        refreshButton.setPrefWidth(30);
        refreshButton.setMaxHeight(30);
        ImageView refresh = ImageUtility.getImageView(Header.class.getResource("/refresh.png").toExternalForm(),20);
        ImageUtility.colorImg(refresh, Color.GREY);
        refreshButton.getStyleClass().add("image-button");
        refreshButton.getChildren().add(refresh);

        uploadButton.setOnMouseEntered(event -> ImageUtility.colorImg(upload, Color.WHITE));
        uploadButton.setOnMouseExited(event -> ImageUtility.colorImg(upload, Color.GREY));
        refreshButton.setOnMouseEntered(event -> ImageUtility.colorImg(refresh, Color.WHITE));
        refreshButton.setOnMouseExited(event -> ImageUtility.colorImg(refresh, Color.GREY));

        uploadButton.setOnMouseClicked(event -> App.goToTransfer());
        refreshButton.setOnMouseClicked(event -> {
            try {
                Gallery.loadGalleryFiles();
                ImageArea.getInstance().showImages(Gallery.files);
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
        });

        content.getChildren().addAll(uploadButton, refreshButton, searchbar);

        getChildren().add(content);
    }
}
