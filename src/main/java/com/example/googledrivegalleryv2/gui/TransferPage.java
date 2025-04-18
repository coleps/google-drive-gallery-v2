package com.example.googledrivegalleryv2.gui;

import com.example.googledrivegalleryv2.drive.*;
import com.example.googledrivegalleryv2.gui.gallerypage.ImageArea;
import com.example.googledrivegalleryv2.gui.gallerypage.SelectionArea;
import io.github.palexdev.materialfx.controls.MFXButton;
import io.github.palexdev.materialfx.controls.MFXScrollPane;
import io.github.palexdev.materialfx.controls.MFXTextField;
import io.github.palexdev.materialfx.enums.FloatMode;
import javafx.application.Platform;
import javafx.concurrent.Task;
import javafx.geometry.Pos;
import javafx.scene.control.Label;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.StackPane;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.scene.shape.Rectangle;
import javafx.stage.DirectoryChooser;

import java.awt.*;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.nio.file.Path;
import java.security.GeneralSecurityException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.Callable;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class TransferPage extends BorderPane {
    private static TransferPage instance;
    public static TransferPage getInstance(){
        if(instance == null) instance = new TransferPage();
        return instance;
    }

    List<InfoLine> lines = new ArrayList<>();
    VBox infoLines;
    MFXButton upload;

    private TransferPage(){
        setStyle("-fx-background-color: rgba(230, 230, 230, 1);");

        HBox header = new HBox();
        header.setSpacing(10);
        header.setAlignment(Pos.CENTER);
        header.setPrefHeight(80);
        upload = new MFXButton("Transfer Images to Google Drive");
        header.getChildren().add(upload);
        upload.setOnMouseClicked(event -> {
            transfer();
        });
        upload.setDisable(true);

        MFXButton goToGallery = new MFXButton("Continue to Gallery Instead");
        header.getChildren().add(goToGallery);
        goToGallery.setOnMouseClicked(event -> {
            ImageArea.getInstance().showImages(Gallery.files);
            SelectionArea.getInstance().setLinks("artist");
            App.goToGallery();
        });

        MFXButton folder = new MFXButton("Choose Folder");
        header.getChildren().add(folder);
        folder.setOnMouseClicked(event -> choose());

        MFXScrollPane scrollPane = new MFXScrollPane();
        scrollPane.setFitToWidth(true);
        scrollPane.setFitToHeight(true);

        infoLines = new VBox();
        scrollPane.setContent(infoLines);
        infoLines.setStyle("-fx-padding: 25 15 0 15");
        infoLines.setSpacing(20);

        setTop(header);
        setCenter(scrollPane);

//        addFilesToPage();
    }

    public void choose(){
        DirectoryChooser directoryChooser = new DirectoryChooser();
        directoryChooser.setTitle("Select Folder");

        // Optional: Set an initial directory
        // directoryChooser.setInitialDirectory(new File(System.getProperty("user.home")));

        File selectedDirectory = directoryChooser.showDialog(App.stage);
        FileTransfer.localRootPath = selectedDirectory.getPath();

        addFilesToPage();

    }

    public void addFilesToPage(){
        List<String> filePaths = FileTransfer.getAllLocalImagePaths();

        ExecutorService executor = Executors.newFixedThreadPool(10); // limit concurrency
        CountDownLatch latch = new CountDownLatch(filePaths.size());

        for (String filePath : filePaths) {
            Task<InfoLine> loadImageTask = new Task<>() {
                @Override
                protected InfoLine call() {
                    InfoLine infoLine = new InfoLine(filePath);
                    return infoLine;
                }
            };

            loadImageTask.setOnSucceeded(e -> {
                Platform.runLater(()->{
                    InfoLine infoLine = loadImageTask.getValue();
                    lines.add(infoLine);
                    infoLines.getChildren().add(infoLine);
                    latch.countDown();
                });

            });

            executor.submit(loadImageTask);
        }

        executor.shutdown();

        new Thread(() -> {
            try {
                latch.await();
                upload.setDisable(false);
            } catch (InterruptedException e) {
                Thread.currentThread().interrupt();
                System.err.println("Latch await interrupted!");
            }
        }).start();
    }

    private class InfoLine extends HBox{
        public MFXTextField title;
        public MFXTextField artist;
        public MFXTextField tags;
        public MFXTextField albums;
        public File file;

        public InfoLine(String filePath) {

            setSpacing(5);
            file = new File(filePath);

            HBox previewBox = new HBox();
            previewBox.setStyle("-fx-background-color: silver");
            previewBox.setAlignment(Pos.CENTER);
            previewBox.setMinHeight(70);
            previewBox.setMinWidth(70);
            ImageView preview = ImageUtility.getImageView(new File(filePath).toURI().toString(),70);
            previewBox.getChildren().add(preview);

            Label fileName = new Label(FileUtility.getFileOrFolderName(filePath));
            fileName.setPrefWidth(150);

            title = new MFXTextField("");
            title.setFloatingText("Title");
            title.setFloatMode(FloatMode.ABOVE);
            title.getStyleClass().add("metadata");

            artist = new MFXTextField("");
            artist.setFloatingText("Artist");
            artist.setFloatMode(FloatMode.ABOVE);
            artist.getStyleClass().add("metadata");

            tags = new MFXTextField("");
            tags.setFloatingText("Tags");
            tags.setFloatMode(FloatMode.ABOVE);
            tags.getStyleClass().add("metadata");

            albums = new MFXTextField("");
            albums.setFloatingText("Albums");
            albums.setFloatMode(FloatMode.ABOVE);
            albums.getStyleClass().add("metadata");

            getChildren().addAll(previewBox,fileName,title,artist,tags,albums);


            preview.setOnMouseClicked(event -> {
                try {
                    FileUtility.revealFileInExplorer(file);
                } catch (IOException ex) {
                    ex.printStackTrace();
                }
            });
        }
    }

    public void transfer(){
        App.goToLoading("Transferring Files to Google Drive");

        ExecutorService executor = Executors.newFixedThreadPool(5);
        CountDownLatch latch = new CountDownLatch(lines.size());

        new Thread(() -> {
            for (InfoLine infoLine : lines) {
                executor.submit(() -> {
                    try {
                        retry(() -> {
                            Map<String, String> map = new HashMap<>();
                            map.put("title",infoLine.title.getText());
                            map.put("artist",infoLine.artist.getText());
                            map.put("tags",infoLine.tags.getText());
                            map.put("albums",infoLine.albums.getText());
                            DriveController.uploadFile(infoLine.file,Gallery.rootID,map,true);
                            return null;
                        }, 5, 1000);
                    } catch (Exception e) {
                        System.err.println("Failed to upload after retries: " + infoLine.file.getName());
                        e.printStackTrace();
                    } finally {
                        latch.countDown(); // always decrement the latch
                    }
                });
            }

            executor.shutdown();

            try {
                latch.await();
//                Thread.sleep(5000);
//                Gallery.start();
                ImageArea.getInstance().showImages(Gallery.files);
                SelectionArea.getInstance().setLinks("artist");
                App.goToGallery();

                upload.setDisable(true);
                lines.clear();
                infoLines.getChildren().clear();

            } catch (InterruptedException e) {
                Thread.currentThread().interrupt();
                System.err.println("Latch await interrupted!");
            }
        }).start();

    }

    public static <T> T retry(Callable<T> action, int maxRetries, long initialDelayMillis) throws Exception {
        int attempts = 0;
        long delay = initialDelayMillis;

        while (true) {
            try {
                return action.call();
            } catch (IOException e) {
                attempts++;
                if (attempts > maxRetries) throw e;

                System.out.println("Retry " + attempts + " after " + delay + "ms due to: " + e.getMessage());
                Thread.sleep(delay);
                delay *= 2; // exponential backoff
            }
        }
    }
}
