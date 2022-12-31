module com.maman14.calender {
    requires javafx.controls;
    requires javafx.fxml;
    requires java.desktop;


    opens com.maman14.calender to javafx.fxml;
    exports com.maman14.calender;
}