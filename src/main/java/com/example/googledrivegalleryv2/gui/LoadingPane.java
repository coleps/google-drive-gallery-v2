package com.example.googledrivegalleryv2.gui;

import io.github.palexdev.materialfx.controls.MFXProgressSpinner;
import javafx.geometry.Pos;
import javafx.scene.control.Label;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.scene.shape.Rectangle;

public class LoadingPane extends BorderPane {

    public LoadingPane(String message) {

        MFXProgressSpinner spinner = new MFXProgressSpinner();
        spinner.setColor1(Color.BLACK);
        spinner.setColor2(Color.BLACK);
        spinner.setColor3(Color.BLACK);
        spinner.setColor4(Color.BLACK);

        VBox container = new VBox();
        container.setAlignment(Pos.CENTER);
        Label text = new Label(message);
        Rectangle spacer = new Rectangle(20, 20, Color.TRANSPARENT);
        container.getChildren().addAll(spinner, spacer, text);

        setCenter(container);

    }

}
