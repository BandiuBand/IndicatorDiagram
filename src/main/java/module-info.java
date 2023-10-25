module com.bandiu.javafxapp.indicatordiagram {
    requires javafx.controls;
    requires javafx.fxml;

    requires org.controlsfx.controls;
    //requires com.dlsc.formsfx;
    requires com.sun.jna;
    requires org.apache.poi.poi;
    requires org.apache.poi.ooxml;
    opens com.bandiu.javafxapp to javafx.fxml;
    exports com.bandiu.javafxapp;
    exports com.bandiu.javafxapp.model;
    opens com.bandiu.javafxapp.model to javafx.fxml;
    exports com.bandiu.javafxapp.view;

}