package com.example.googledrivegalleryv2.gui;

import com.google.api.services.drive.model.File;
import io.github.palexdev.materialfx.controls.MFXProgressSpinner;
import io.github.palexdev.materialfx.controls.MFXScrollPane;
import javafx.geometry.Insets;
import javafx.scene.control.ScrollPane;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.Background;
import javafx.scene.layout.BackgroundFill;
import javafx.scene.layout.CornerRadii;
import javafx.scene.layout.TilePane;
import javafx.scene.paint.Color;
import org.controlsfx.tools.Borders;

import java.io.FileNotFoundException;
import java.util.List;

public class ImageArea extends MFXScrollPane {
    private double imgWidth = 300;
    private TilePane tilePane;
    private static final String DRIVE_IMG_URL_PREFIX = "https://drive.google.com/uc?export=view&id=";
    private static final String IMG_ERROR_PATH = "/img-error.jpg";

    public ImageArea(){
        setFitToWidth(true);

        tilePane = new TilePane();
        tilePane.setHgap(10);
        tilePane.setVgap(10);

        setContent(tilePane);

//        setStyle("-fx-border-color: black");
//        setStyle("-fx-border: 5px solid black");
        setStyle("-fx-background-radius: 10px;");
//        setStyle("-fx-focus-color: transparent; -fx-faint-focus-color: transparent;");

//        setHbarPolicy(ScrollBarPolicy.NEVER);
//        setVbarPolicy(ScrollPane.ScrollBarPolicy.ALWAYS);


    }

    public void showImages(List<File> imgs){
        tilePane.getChildren().clear();
        imgs.forEach(file -> {
            ImageView imageView;
            String path = DRIVE_IMG_URL_PREFIX + file.getId();
            try {
                imageView = ImageUtility.getImageView(path, imgWidth);
            } catch (FileNotFoundException e) {
                Image image = new Image(ImageArea.class.getResource(IMG_ERROR_PATH).toExternalForm());
                imageView = new ImageView(image);
            }
            MFXProgressSpinner spinner = new MFXProgressSpinner();
//            spinner.setColor1(Color.BLACK);
//            spinner.setColor2(Color.BLACK);
//            spinner.setColor3(Color.BLACK);
//            spinner.setColor4(Color.BLACK);
            tilePane.getChildren().add(spinner);
        });
    }
}
