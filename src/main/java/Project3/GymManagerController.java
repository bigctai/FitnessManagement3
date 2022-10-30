package Project3;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.Node;
import javafx.scene.control.Button;
import javafx.scene.control.ChoiceBox;
import javafx.scene.control.Label;
import javafx.scene.layout.*;
import javafx.stage.Stage;
import javafx.scene.Scene;

public class GymManagerController {

    ObservableList<String> locations = FXCollections.observableArrayList("Edison", "Bridgewater", "Franklin", "Piscataway", "Somerville");
    @FXML
    ChoiceBox chooseLocation = new ChoiceBox();

    @FXML
    public void initialize() {
        chooseLocation.setItems(locations);
    }

}