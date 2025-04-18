package com.example.googledrivegalleryv2.gui;

import com.example.googledrivegalleryv2.drive.Gallery;
import com.example.googledrivegalleryv2.drive.PropertiesUtility;
import com.example.googledrivegalleryv2.gui.gallerypage.SelectionArea;
import io.github.palexdev.materialfx.controls.MFXButton;
import io.github.palexdev.materialfx.controls.MFXTextField;
import io.github.palexdev.materialfx.enums.FloatMode;
import javafx.application.Platform;
import javafx.geometry.Pos;
import javafx.geometry.Side;
import javafx.scene.image.ImageView;
import javafx.scene.layout.StackPane;
import javafx.scene.layout.VBox;
import javafx.util.Duration;
import org.controlsfx.control.HiddenSidesPane;

import java.util.HashMap;
import java.util.Map;

public class BlowupPage extends HiddenSidesPane {
    private static final String DRIVE_IMG_URL_PREFIX = "https://drive.google.com/uc?export=view&id=";
    public static HashMap<String, ImageView> imageCache = new HashMap<>();
    private static BlowupPage instance;
    public static BlowupPage getInstance(){
        if(instance == null) instance = new BlowupPage();
        return instance;
    }
    private StackPane content;
    MFXButton save;
    MFXTextField title;
    MFXTextField artist;
    MFXTextField tags;
    MFXTextField albums;

    String id;

    private BlowupPage(){
        content = new StackPane();
        content.setStyle("-fx-background-color: white;");


        VBox sideBar = new VBox();
        sideBar.setSpacing(45);
        sideBar.setPrefWidth(300);
        sideBar.setAlignment(Pos.TOP_CENTER);
        sideBar.setStyle("-fx-background-color: rgba(230, 230, 230, 1); -fx-padding: 20 0 0 0;");
        save = new MFXButton("Save");
        save.setPrefWidth(80);
//        save.setStyle(" -fx-border-radius: 50px; -fx-background-radius:50px");

        title = new MFXTextField("deuebfde");
        title.setFloatingText("Title");
        title.setFloatMode(FloatMode.ABOVE);
//        title.setPrefWidth(170);
        title.getStyleClass().add("metadata");

        artist = new MFXTextField("deuebfde");
        artist.setFloatingText("Artist");
        artist.setFloatMode(FloatMode.ABOVE);
//        artist.setPrefWidth(170);
        artist.getStyleClass().add("metadata");

        tags = new MFXTextField("deuebfde");
        tags.setFloatingText("Tags");
        tags.setFloatMode(FloatMode.ABOVE);
//        tags.setPrefWidth(170);
        tags.getStyleClass().add("metadata");

        albums = new MFXTextField("deuebfde");
        albums.setFloatingText("Albums");
        albums.setFloatMode(FloatMode.ABOVE);
//        albums.setPrefWidth(170);
        albums.getStyleClass().add("metadata");

        sideBar.getChildren().addAll(save,title,artist,tags,albums);

        setContent(content);
        setLeft(sideBar);
        setAnimationDelay(Duration.ZERO);


        double triggerDistance = 300;
        setOnMouseMoved(event -> {
            if (event.getSceneX() < triggerDistance) {
                setPinnedSide(Side.LEFT); // Show
            } else if (getPinnedSide() == Side.LEFT) {
                setPinnedSide(null); // Hide
            }
        });
        content.setOnMouseClicked((event -> App.goToGallery()));
        save.setOnMouseClicked((event -> save()));
    }
    public void setFile(String id){
        this.id = id;
        content.getChildren().clear();
        content.getChildren().add(new LoadingPane("Loading Image..."));

        title.setText(PropertiesUtility.getAppProperty(id,"title"));
        artist.setText(PropertiesUtility.getAppProperty(id,"artist"));
        tags.setText(PropertiesUtility.getAppProperty(id,"tags"));
        albums.setText(PropertiesUtility.getAppProperty(id,"albums"));

        new Thread(()->{
            ImageView imageView;
            try {
                if(imageCache.containsKey(id)) imageView = imageCache.get(id);
                else{
                    String url = DRIVE_IMG_URL_PREFIX + id;
                    imageView = ImageUtility.getImageView(url, 0);
                    imageView.fitWidthProperty().bind(widthProperty());
                    imageView.fitHeightProperty().bind(heightProperty());
                    imageView.setPreserveRatio(true);
                    imageCache.put(id,imageView);
                }
                //FIXME remove try catch
            } catch (Exception e) {
                throw new RuntimeException(e);
            }
            Platform.runLater(()->{
                content.getChildren().clear();
                content.getChildren().add(imageView);
            });

        }).start();
    }

    public void save(){
        Map<String, String> map = new HashMap<>();
        map.put("title",title.getText());
        map.put("artist",artist.getText());
        map.put("tags",tags.getText());
        map.put("albums",albums.getText());
        PropertiesUtility.updateAppProperties(id,map);

        //FIXME super inefficient
        Gallery.addAllPropertiesToMaps();
        SelectionArea.getInstance().setLinks(SelectionArea.linksType);
    }
}
