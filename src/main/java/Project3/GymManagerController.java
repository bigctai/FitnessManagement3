package Project3;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.ChoiceBox;

import java.net.URL;
import java.util.ResourceBundle;

public class GymManagerController implements Initializable {

    private ObservableList<String> locations = FXCollections.observableArrayList("Edison", "Bridgewater", "Franklin", "Piscataway", "Somerville");
    private String[] teachers = {"Davis", "Kim", "Denise", "Emma", "Jennifer"};
    @FXML
    private ChoiceBox chooseLocation = new ChoiceBox();
    @FXML
    private ChoiceBox chooseLocation1 = new ChoiceBox();
    @FXML
    private ChoiceBox chooseTeacher = new ChoiceBox();
    private String memFirstName;
    private String memLastName;
    private Date dob;
    private Time timeOfClass;
    private String instructor;
    private String className;
    private Location location;
    private ClassSchedule classes;


    Button memCheckIn = new Button();

    @Override
    public void initialize(URL arg0, ResourceBundle arg1){
        chooseLocation.getItems().addAll(locations);
        chooseLocation1.getItems().addAll(locations);
        chooseTeacher.getItems().addAll(teachers);
        EventHandler<ActionEvent> event = new EventHandler<ActionEvent>(){
            public void handle(ActionEvent event) {
                FitnessClass fitClass = new FitnessClass(instructor, className, location);
                int fitClassIndex = classes.getClassIndex(fitClass);
                FitnessClass classToCheckInto = classes.returnList()[fitClassIndex];
                Member memToCheckIn = new Member(memFirstName, memLastName, dob);
                classToCheckInto.checkInMember(memToCheckIn, classes);
            }
        };
        memCheckIn.setOnAction(event);
    }
}