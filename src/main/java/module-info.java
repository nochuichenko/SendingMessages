module application.sendingmessages {
    requires javafx.controls;
    requires javafx.fxml;
    requires java.mail;
    requires org.apache.logging.log4j.core;
    requires org.apache.logging.log4j;


    opens application to javafx.fxml;
    exports application;
}