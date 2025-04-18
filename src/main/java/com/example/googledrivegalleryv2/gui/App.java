package com.example.googledrivegalleryv2.gui;

import com.example.googledrivegalleryv2.drive.DriveConnection;
import com.example.googledrivegalleryv2.drive.Gallery;
import com.example.googledrivegalleryv2.gui.gallerypage.GalleryPage;
import com.example.googledrivegalleryv2.gui.gallerypage.ImageArea;
import com.example.googledrivegalleryv2.gui.gallerypage.SelectionArea;
import javafx.application.Application;
import javafx.application.Platform;
import javafx.concurrent.Task;
import javafx.scene.Scene;
import javafx.stage.Stage;

import java.io.IOException;
import java.security.GeneralSecurityException;

public class App extends Application {
    private static Scene mainScene;
    public static Stage stage;
    @Override
    public void start(Stage stage) throws Exception {
        App.stage = stage;

        ImageArea imageArea = ImageArea.getInstance();


        BlowupPage blowupPage = BlowupPage.getInstance();

        mainScene = new Scene(GalleryPage.getInstance(),1000,1000);
        mainScene.getStylesheets().add(App.class.getResource("/MFXDefault.css").toExternalForm());
        mainScene.getStylesheets().add(App.class.getResource("/styles.css").toExternalForm());
        stage.setScene(mainScene);
        stage.show();
        stage.toFront();

        goToLoading("Connecting to Google Drive...");

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
                SelectionArea.getInstance().setLinks("artist");
                goToGallery();
//                goToTransfer();
            });

        }).start();
    }


    public static void goToBlowup(String fileID){
        BlowupPage.getInstance().setFile(fileID);
        mainScene.setRoot(BlowupPage.getInstance());
    }

    public static void goToGallery(){
        mainScene.setRoot(GalleryPage.getInstance());
    }

    public static void goToLoading(String message){
        mainScene.setRoot(new LoadingPane(message));
    }

    public static void goToTransfer(){
        mainScene.setRoot(TransferPage.getInstance());
    }
}
