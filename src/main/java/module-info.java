module com.example.project3 {
    requires javafx.controls;
    requires javafx.fxml;
    requires org.testng;


    opens Project3 to javafx.fxml;
    exports Project3;
}