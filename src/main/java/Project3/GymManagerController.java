package Project3;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.control.Button;
import javafx.scene.control.ChoiceBox;
import javafx.scene.control.Label;
import javafx.scene.layout.*;
import javafx.stage.Stage;
import javafx.scene.Scene;

import java.net.URL;
import java.util.ResourceBundle;

public class GymManagerController implements Initializable {

    ObservableList<String> locations = FXCollections.observableArrayList("Edison", "Bridgewater", "Franklin", "Piscataway", "Somerville");
    String[] teachers = {"Davis", "Kim", "Denise", "Emma", "Jennifer"};
    @FXML
    ChoiceBox chooseLocation = new ChoiceBox();
    @FXML
    ChoiceBox chooseLocation1 = new ChoiceBox();
    @FXML
    ChoiceBox chooseTeacher = new ChoiceBox();

    @Override
    public void initialize(URL arg0, ResourceBundle arg1){
        chooseLocation.getItems().addAll(locations);
        chooseLocation1.getItems().addAll(locations);
        chooseTeacher.getItems().addAll(teachers);
    }

}