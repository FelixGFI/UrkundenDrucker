module com.example.urkundendrucker {
    requires javafx.controls;
    requires javafx.fxml;
    requires javafx.graphics;
    requires layout;
    requires kernel;
    requires opencsv;


    opens com.example.urkundendrucker to javafx.fxml;
    exports com.example.urkundendrucker;
}