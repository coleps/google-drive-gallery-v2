package com.example.googledrivegalleryv2.gui;

import com.example.googledrivegalleryv2.drive.DriveConnection;
import com.example.googledrivegalleryv2.drive.Gallery;
import io.github.palexdev.materialfx.controls.MFXProgressSpinner;
import javafx.application.Application;
import javafx.application.Platform;
import javafx.concurrent.Task;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Label;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.scene.shape.Rectangle;
import javafx.stage.Stage;

import java.io.IOException;
import java.security.GeneralSecurityException;

public class App extends Application {
    @Override
    public void start(Stage stage) throws Exception {

        ImageArea imageArea = new ImageArea();

        BorderPane gallery = new BorderPane();
        gallery.setLeft(SelectionArea.getInstance());
//        gallery.setCenter(Borders.wrap(imageArea).lineBorder().color(Color.WHITESMOKE).thickness(0).build().build());
        gallery.setCenter(imageArea);
        gallery.setTop(Header.getInstance());

        Scene mainScene = new Scene(gallery,1000,1000);
        mainScene.getStylesheets().add(App.class.getResource("/MFXDefault.css").toExternalForm());
        mainScene.getStylesheets().add(App.class.getResource("/styles.css").toExternalForm());
        stage.setScene(mainScene);
        stage.show();
        stage.toFront();

        mainScene.setRoot(new LoadingPane("Connecting to Google Drive..."));


        Task<Void> loadDrive = new Task<Void>() {
            @Override
            protected Void call() throws Exception {
                DriveConnection.start();
                Gallery.start();

                return null;
            }
            @Override
            protected void succeeded(){
                Platform.runLater(()->{
                    imageArea.showImages(Gallery.files);
                    mainScene.setRoot(gallery);
                });

            }
        };
//        new Thread(loadDrive).start();
        new Thread(()->{
            try {
                DriveConnection.start();
                Gallery.start();
            } catch (IOException e) {
                throw new RuntimeException(e);
            } catch (GeneralSecurityException e) {
                throw new RuntimeException(e);
            }
            Platform.runLater(()->{
                imageArea.showImages(Gallery.files);
                mainScene.setRoot(gallery);
            });

        }).start();
    }
    public static class LoadingPane extends BorderPane {

        public LoadingPane(String message){

            MFXProgressSpinner spinner = new MFXProgressSpinner();
            spinner.setColor1(Color.BLACK);
            spinner.setColor2(Color.BLACK);
            spinner.setColor3(Color.BLACK);
            spinner.setColor4(Color.BLACK);

            VBox container = new VBox();
            container.setAlignment(Pos.CENTER);
            Label text = new Label(message);
            Rectangle spacer = new Rectangle(20,20,Color.TRANSPARENT);
            container.getChildren().addAll(spinner,spacer,text);

            setCenter(container);

        }

    }
}
