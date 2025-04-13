package com.example.googledrivegalleryv2.gui;

import com.example.googledrivegalleryv2.drive.DriveConnection;
import com.example.googledrivegalleryv2.drive.Gallery;
import com.example.googledrivegalleryv2.gui.gallerypane.GalleryPane;
import com.example.googledrivegalleryv2.gui.gallerypane.Header;
import com.example.googledrivegalleryv2.gui.gallerypane.ImageArea;
import com.example.googledrivegalleryv2.gui.gallerypane.SelectionArea;
import javafx.application.Application;
import javafx.application.Platform;
import javafx.concurrent.Task;
import javafx.scene.Scene;
import javafx.scene.layout.BorderPane;
import javafx.stage.Stage;

import java.io.IOException;
import java.security.GeneralSecurityException;

public class App extends Application {
    private static Scene mainScene;
    @Override
    public void start(Stage stage) throws Exception {

        ImageArea imageArea = ImageArea.getInstance();


        BlowupPane blowupPane = BlowupPane.getInstance();

        mainScene = new Scene(GalleryPane.getInstance(),1000,1000);
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
                    goToGallery();
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
                goToGallery();
            });

        }).start();
    }


    public static void goToBlowup(String fileID){
        BlowupPane.getInstance().setFile(fileID);
        mainScene.setRoot(BlowupPane.getInstance());
    }

    public static void goToGallery(){
        mainScene.setRoot(GalleryPane.getInstance());
    }

    public static void goToLoading(String message){
        mainScene.setRoot(new LoadingPane(message));
    }
}
