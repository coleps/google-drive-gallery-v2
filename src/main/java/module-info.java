module com.example.googledrivegalleryv2 {
    requires javafx.controls;
    requires javafx.fxml;

    requires org.controlsfx.controls;
    requires org.kordamp.bootstrapfx.core;

    opens com.example.googledrivegalleryv2 to javafx.fxml;
    exports com.example.googledrivegalleryv2;
}