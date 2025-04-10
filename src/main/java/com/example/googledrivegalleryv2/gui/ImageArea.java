package com.example.googledrivegalleryv2.gui;

import com.google.api.services.drive.model.File;
import io.github.palexdev.materialfx.controls.MFXScrollPane;
import javafx.concurrent.Task;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.TilePane;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.scene.shape.Rectangle;

import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class ImageArea extends MFXScrollPane {
    private double imgWidth = 150;
    private TilePane tilePane;
    private static final String DRIVE_IMG_URL_PREFIX = "https://drive.google.com/uc?export=view&id=";
    private static final String IMG_ERROR_PATH = "/img-error.jpg";

    public ImageArea(){
        setFitToWidth(true);

        tilePane = new TilePane();
        tilePane.setHgap(10);
        tilePane.setVgap(10);
        tilePane.setStyle("-fx-padding: 15 15 15 15");

        setContent(tilePane);

//        setStyle("-fx-border-color: black");
//        setStyle("-fx-border: 5px solid black");
        setStyle("-fx-background-radius: 10px;");
//        setStyle("-fx-focus-color: transparent; -fx-faint-focus-color: transparent;");

//        setHbarPolicy(ScrollBarPolicy.NEVER);
//        setVbarPolicy(ScrollPane.ScrollBarPolicy.ALWAYS);


    }

    /**
     * Version with multiple threads
     */
    public void showImages(List<File> imgs){
        setLoading("Loading images...");
        tilePane.getChildren().clear();
        ExecutorService executor = Executors.newFixedThreadPool(6); // limit concurrency

        for (File file : imgs) {
            Task<ImageView> loadImageTask = new Task<>() {
                @Override
                protected ImageView call() {
                    String path = file.getThumbnailLink();
                    try {
                        return ImageUtility.getImageView(path, imgWidth);
                    } catch (FileNotFoundException e) {
                        Image errorImage = new Image(ImageArea.class.getResource(IMG_ERROR_PATH).toExternalForm());
                        return new ImageView(errorImage);
                    }
                }
            };

            loadImageTask.setOnSucceeded(e -> {
                tilePane.getChildren().add(loadImageTask.getValue());
            });

            executor.submit(loadImageTask);
        }
        executor.shutdown();
        setNotLoading();
    }

    /**
     * Version with one thread
     */
//    public void showImages(List<File> imgs){
//        setLoading("Loading images...");
//        tilePane.getChildren().clear();
//        Task<List<ImageView>> loadImagesTask = new Task<>() {
//            @Override
//            protected List<ImageView> call() throws Exception {
//                List<ImageView> views = new ArrayList<>();
//                for (File file : imgs) {
////                    String path = DRIVE_IMG_URL_PREFIX + file.getId();
//                    String path = file.getThumbnailLink();
//                    ImageView imageView;
//                    try {
//                        imageView = ImageUtility.getThumbImageView(path, imgWidth);
//                    } catch (FileNotFoundException e) {
//                        Image errorImage = new Image(
//                                ImageArea.class.getResource(IMG_ERROR_PATH).toExternalForm()
//                        );
//                        imageView = new ImageView(errorImage);
//                    }
//                    views.add(imageView);
//                }
//                return views;
//            }
//        };
//        loadImagesTask.setOnSucceeded(e -> {
//            setNotLoading();
//            tilePane.getChildren().addAll(loadImagesTask.getValue());
//        });
//        loadImagesTask.setOnFailed(e -> {
//            setNotLoading();
//            System.out.println("Failed to load images: " + loadImagesTask.getException());
//        });
//        new Thread(loadImagesTask).start();
//    }

//    public void showImages(List<File> imgs){
//        tilePane.getChildren().clear();
//        imgs.forEach(file -> {
//            ImageView imageView;
//            String path = DRIVE_IMG_URL_PREFIX + file.getId();
//            try {
//                imageView = ImageUtility.getImageView(path, imgWidth);
//            } catch (FileNotFoundException e) {
//                Image image = new Image(ImageArea.class.getResource(IMG_ERROR_PATH).toExternalForm());
//                imageView = new ImageView(image);
//            }
//            tilePane.getChildren().add(imageView);
//        });
//    }

    public void setLoading(String message){
        App.LoadingPane loadingPane = new App.LoadingPane(message);
        Rectangle spacer = new Rectangle(0,100,Color.TRANSPARENT);
        loadingPane.setTop(spacer);
        setContent(loadingPane);
    }
    public void setNotLoading(){
        setContent(tilePane);
    }
}
