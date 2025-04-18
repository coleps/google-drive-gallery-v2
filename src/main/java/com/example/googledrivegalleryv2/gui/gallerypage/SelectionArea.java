package com.example.googledrivegalleryv2.gui.gallerypage;

import com.example.googledrivegalleryv2.drive.Gallery;
import io.github.palexdev.materialfx.controls.MFXButton;
import io.github.palexdev.materialfx.controls.MFXScrollPane;
import io.github.palexdev.materialfx.controls.MFXTextField;
import io.github.palexdev.materialfx.enums.FloatMode;
import javafx.application.Platform;
import javafx.event.Event;
import javafx.geometry.Pos;
import javafx.scene.control.Label;
import javafx.scene.control.ScrollPane;
import javafx.scene.control.TextField;
import javafx.scene.control.ToggleButton;
import javafx.scene.input.ScrollEvent;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.scene.shape.Rectangle;
import org.controlsfx.control.SegmentedButton;

import java.util.*;

public class SelectionArea extends VBox {
    private double width = 200;
    private static SelectionArea instance;
    public static SelectionArea getInstance(){
        if(instance == null) instance = new SelectionArea();
        return instance;
    }

    VBox linksContainer;
    public static String linksType;
    public Label highlightedLabel = new Label();
    TextField filterbar;

    private SelectionArea(){

//        setFitToHeight(true);
        setPrefWidth(width);
        setStyle("-fx-background-color: rgba(230, 230, 230, 1);");

        VBox content = new VBox();
        content.setSpacing(10);
        content.setPrefWidth(width);
        content.setAlignment(Pos.TOP_CENTER);
        SegmentedButton selectButtons = new SegmentedButton();
        ToggleButton artistButton = new ToggleButton("Artist");
        ToggleButton tagButton = new ToggleButton("Tag");
        ToggleButton albumButton = new ToggleButton("Album");

        filterbar = new TextField();
        filterbar.getStyleClass().add("filterbar");
        filterbar.setPrefWidth(140);
        filterbar.setMaxWidth(140);
        filterbar.setPrefHeight(10);
//        filterbar.setAnimated(false);
//        filterbar.setFloatMode(FloatMode.DISABLED);
        filterbar.setOnKeyReleased(event -> setLinks(linksType));

        Label allImages = new Label("All Images");
        allImages.setPrefWidth(140);
        allImages.setAlignment(Pos.CENTER);
        allImages.getStyleClass().add("link-highlight");
        highlightedLabel = allImages;
        allImages.setOnMouseClicked(event -> {
            if(highlightedLabel == allImages) return;
            ImageArea.getInstance().showImages(Gallery.files);
            allImages.getStyleClass().remove("link");
            allImages.getStyleClass().add("link-highlight");
            highlightedLabel.getStyleClass().remove("link-highlight");
            highlightedLabel.getStyleClass().add("link");
            highlightedLabel = allImages;
        });

        MFXScrollPane linksPane = new MFXScrollPane();
        linksPane.setFitToWidth(true);
        linksContainer = new VBox();
        linksContainer.prefWidthProperty().bind(linksPane.widthProperty().subtract(2));
        linksPane.setContent(linksContainer);
        linksContainer.setAlignment(Pos.TOP_CENTER);
        linksPane.setStyle("-fx-background-color: transparent");
        linksPane.setHbarPolicy(ScrollPane.ScrollBarPolicy.NEVER);


        selectButtons.getButtons().addAll(artistButton,tagButton,albumButton);

        content.getChildren().addAll(allImages,selectButtons,filterbar,linksPane);
        getChildren().addAll(content);


        artistButton.setSelected(true);

        artistButton.setOnMouseClicked(event -> setLinks("artist"));
        tagButton.setOnMouseClicked(event -> setLinks("tag"));
        albumButton.setOnMouseClicked(event -> setLinks("album"));

    }

    public void setLinks(String type){
        linksType = type;
        linksContainer.getChildren().clear();
        List<String> links = new ArrayList<>();
        switch (type){
            case "artist" -> links = new ArrayList<>(Gallery.artistToIDsMap.keySet());
            case "tag" -> links = new ArrayList<>(Gallery.tagToIDsMap.keySet());
            case "album" -> links = new ArrayList<>(Gallery.albumToIDsMap.keySet());
        }
        Collections.sort(links);
        for(String link : links){
            String filterText = filterbar.getText().toLowerCase();
            if(!link.toLowerCase().contains(filterText)) continue;
            Label linkLabel = new Label(link);
            linksContainer.getChildren().add(linkLabel);
            linkLabel.getStyleClass().add("link");
            linkLabel.prefWidthProperty().bind(linksContainer.widthProperty().subtract(25));
            linkLabel.setWrapText(true);

            linkLabel.setOnMouseClicked(event->{
                Map<String, List<String>> map = new HashMap<>();
                switch (type){
                    case "artist" -> map = Gallery.artistToIDsMap;
                    case "tag" -> map = Gallery.tagToIDsMap;
                    case "album" -> map = Gallery.albumToIDsMap;
                }
                ImageArea.getInstance().showImagesFromIds(map.get(link));
                linkLabel.getStyleClass().remove("link");
                linkLabel.getStyleClass().add("link-highlight");
                highlightedLabel.getStyleClass().remove("link-highlight");
                highlightedLabel.getStyleClass().add("link");
                highlightedLabel = linkLabel;
            });
        }
    }

}
