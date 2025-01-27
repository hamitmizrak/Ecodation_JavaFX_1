module com.hamitmizrak.ecodation_javafx {
    requires javafx.controls;
    requires javafx.fxml;

    requires org.controlsfx.controls;
    requires com.dlsc.formsfx;
    requires net.synedra.validatorfx;
    requires org.kordamp.ikonli.javafx;
    requires org.kordamp.bootstrapfx.core;

    opens com.hamitmizrak.ecodation_javafx to javafx.fxml;
    exports com.hamitmizrak.ecodation_javafx;
}