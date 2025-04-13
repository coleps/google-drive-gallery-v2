package com.example.googledrivegalleryv2.gui;

import javafx.scene.effect.ColorAdjust;
import javafx.scene.effect.Light;
import javafx.scene.effect.Lighting;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.paint.Color;

import java.io.FileNotFoundException;

public class ImageUtility {
    //FIXME remove error here and just return error image
    public static ImageView getImageView(String path, double length) throws FileNotFoundException {
        Image image = new Image(path);
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
    public static ImageView getThumbImageView(String path, double length) throws FileNotFoundException {
        Image image = new Image(path,true);
        ImageView imageView = new ImageView(image);
        double ratio = image.getHeight() / image.getWidth();
        imageView.setPreserveRatio(true); // keeps aspect ratio
        imageView.setSmooth(true);
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

