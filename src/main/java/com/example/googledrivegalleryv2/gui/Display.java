package com.example.googledrivegalleryv2.gui;

import javafx.application.Application;
import javafx.scene.Scene;
import javafx.scene.layout.StackPane;
import javafx.stage.Stage;

public class Display extends Application {
    @Override
    public void start(Stage stage) throws Exception {
        StackPane pane = new StackPane();
        Scene mainScene = new Scene(pane,1000,1000);

        stage.setScene(mainScene);
        stage.show();
    }
}
