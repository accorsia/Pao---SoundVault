module com.unimn.soundvault {
    requires javafx.controls;
    requires javafx.fxml;
            
        requires org.controlsfx.controls;
            requires com.dlsc.formsfx;
            requires net.synedra.validatorfx;
    requires java.sql;

    opens com.unimn.soundvault to javafx.fxml;
    exports com.unimn.soundvault;
    exports com.unimn.soundvault.controllers;
    opens com.unimn.soundvault.controllers to javafx.fxml;
}