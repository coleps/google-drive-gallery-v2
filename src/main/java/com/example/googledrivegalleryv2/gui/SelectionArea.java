package com.example.googledrivegalleryv2.gui;

import io.github.palexdev.materialfx.controls.MFXButton;
import io.github.palexdev.materialfx.controls.MFXScrollPane;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.control.ScrollPane;
import javafx.scene.control.ToggleButton;
import javafx.scene.layout.Background;
import javafx.scene.layout.BackgroundFill;
import javafx.scene.layout.CornerRadii;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import org.controlsfx.control.SegmentedButton;

public class SelectionArea extends MFXScrollPane {
    private double width = 200;
    private static SelectionArea instance;
    public static SelectionArea getInstance(){
        if(instance == null) instance = new SelectionArea();
        return instance;
    }

    private SelectionArea(){
        setFitToHeight(true);
        setPrefWidth(width);
        setStyle("-fx-background-color: whitesmoke;");

        VBox content = new VBox();
        content.setPrefWidth(width);
        content.setAlignment(Pos.TOP_CENTER);
        SegmentedButton selectButtons = new SegmentedButton();
        ToggleButton artistButton = new ToggleButton("Artist");
        ToggleButton tagButton = new ToggleButton("Tag");
        ToggleButton albumButton = new ToggleButton("albumButton");


        selectButtons.getButtons().addAll(artistButton,tagButton,albumButton);

        content.getChildren().addAll(selectButtons);
//        setStyleetStyle("-fx-focus-color: transparent; -fx-faint-focus-color: transparent;");
//        setHbarPolicy(ScrollPane.ScrollBarPolicy.ALWAYS);
//        setVbarPolicy(ScrollPane.ScrollBarPolicy.ALWAYS);

        setContent(content);
    }

}
