package com.example.googledrivegalleryv2.gui.gallerypane;

import io.github.palexdev.materialfx.controls.MFXTextField;
import io.github.palexdev.materialfx.enums.FloatMode;
import javafx.geometry.Pos;
import javafx.scene.layout.HBox;
import javafx.scene.layout.StackPane;
import javafx.scene.paint.Color;
import javafx.scene.shape.Rectangle;

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
        HBox content = new HBox();
        content.setPrefHeight(height);
        content.setAlignment(Pos.CENTER);
        MFXTextField searchbar = new MFXTextField();
        searchbar.getStyleClass().add("searchbar");
        searchbar.setPrefWidth(500);
        searchbar.setLeadingIcon(new Rectangle(10,10,Color.TRANSPARENT));
        searchbar.setAnimated(false);
        searchbar.setFloatMode(FloatMode.DISABLED);
        content.getChildren().add(searchbar);

        getChildren().add(content);
    }
}
