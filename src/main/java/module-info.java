module com.hamitmizrak.ecodation_javafx {
    requires javafx.controls;
    requires javafx.fxml;

    requires org.controlsfx.controls;
    requires com.dlsc.formsfx;
    requires net.synedra.validatorfx;
    requires org.kordamp.ikonli.javafx;
    requires org.kordamp.bootstrapfx.core;
    requires static lombok;
    requires java.sql;

    opens com.hamitmizrak.ecodation_javafx to javafx.fxml;
    opens com.hamitmizrak.ecodation_javafx.controller to javafx.fxml;  // <-- EKLENDİ
    opens com.hamitmizrak.ecodation_javafx.dto to javafx.base; // javafx.base erişim izni
    exports com.hamitmizrak.ecodation_javafx;
}