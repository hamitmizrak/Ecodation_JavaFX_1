module com.hamitmizrak.vatcalculation {
    requires javafx.controls;
    requires javafx.fxml;
    requires java.sql;

    requires org.controlsfx.controls;
    requires com.dlsc.formsfx;
    requires net.synedra.validatorfx;
    requires org.kordamp.ikonli.javafx;
    requires org.kordamp.bootstrapfx.core;
    requires static lombok;
    requires org.apache.poi.ooxml;
    requires java.mail;
    requires java.desktop;

    opens com.hamitmizrak.vatcalculation to javafx.fxml;
    opens com.hamitmizrak.vatcalculation.controller to javafx.fxml;  // <-- EKLENDİ
    opens com.hamitmizrak.vatcalculation.dto to javafx.base; // javafx.base erişim izni

    exports com.hamitmizrak.vatcalculation;
}
