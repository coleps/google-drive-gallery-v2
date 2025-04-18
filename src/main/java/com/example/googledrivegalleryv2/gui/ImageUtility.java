package com.example.googledrivegalleryv2.gui;

import com.example.googledrivegalleryv2.gui.gallerypage.ImageArea;
import javafx.scene.effect.ColorAdjust;
import javafx.scene.effect.Light;
import javafx.scene.effect.Lighting;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.paint.Color;

import java.io.FileNotFoundException;

public class ImageUtility {
    //FIXME remove error here and just return error image
    public static ImageView getImageView(String path, double length) {
        Image image = new Image(path);
        if(image.isError()) {
            image = new Image(ImageArea.class.getResource("/img-error.jpg").toExternalForm());
        }
        ImageView imageView = new ImageView(image);
        double ratio = image.getHeight() / image.getWidth();
        if(image.getHeight()>image.getWidth()){
            imageView.setFitHeight(length);
            imageView.setFitWidth(length/ratio);
        }
        else{
            imageView.setFitWidth(length);
            imageView.setFitHeight(length*ratio);
        }
        return imageView;
    }
    public static void colorImg(ImageView img, Color color){
        Lighting lighting = new Lighting(new Light.Distant(45, 90, color));
        ColorAdjust bright = new ColorAdjust(0, 1, 1, 1);
        lighting.setContentInput(bright);
        lighting.setSurfaceScale(0.0);

        img.setEffect(lighting);
    }
}

