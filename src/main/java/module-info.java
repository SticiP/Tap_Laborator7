module app.tap_laborator7 {
    requires javafx.controls;
    requires javafx.fxml;

    requires org.controlsfx.controls;
    requires com.dlsc.formsfx;
    requires org.kordamp.bootstrapfx.core;

    opens app.tap_laborator7 to javafx.fxml;
    exports app.tap_laborator7;
}