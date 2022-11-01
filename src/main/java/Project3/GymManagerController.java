package Project3;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.*;
import javafx.scene.text.Text;

import java.net.URL;
import java.util.ResourceBundle;

public class GymManagerController implements Initializable {

    private ObservableList<String> locations = FXCollections.observableArrayList("Edison", "Bridgewater", "Franklin", "Piscataway", "Somerville");
    private String[] teachers = {"Davis", "Kim", "Denise", "Emma", "Jennifer"};

    private static final int ADULT = 18;
    private final int STANDARD_AND_FAMILY_EXPIRATION = 3;
    private final int PREMIUM_EXPIRATION = 12;
    @FXML
    private ChoiceBox chooseLocation = new ChoiceBox();
    @FXML
    private ChoiceBox chooseLocation1 = new ChoiceBox();
    @FXML
    private ChoiceBox chooseTeacher = new ChoiceBox();
    @FXML
    private TextField checkInFirstName = new TextField();
    @FXML
    private TextField checkInLastName = new TextField();
    @FXML
    private DatePicker checkInDob = new DatePicker();
    @FXML
    private TextField addFirstName = new TextField();
    @FXML
    private TextField addLastName = new TextField();
    @FXML
    private RadioButton standard = new RadioButton();
    @FXML
    private RadioButton family = new RadioButton();
    @FXML
    private RadioButton premium = new RadioButton();
    @FXML
    private DatePicker addDob = new DatePicker();

    private ClassSchedule classes = new ClassSchedule();
    private MemberDatabase memData = new MemberDatabase();

    @FXML
    Button memCheckIn;

    @FXML
    Button add;

    @FXML
    Button print;
    @FXML
    Button printDate;
    @FXML
    Button printFee;
    @FXML
    Button printName;
    @FXML
    Button printCounty;


    @Override
    public void initialize(URL arg0, ResourceBundle arg1){
        chooseLocation.getItems().addAll(locations);
        chooseLocation1.getItems().addAll(locations);
        chooseTeacher.getItems().addAll(teachers);
    }

    @FXML
    public void checkInMem(){
        String dateOfBirth = checkInDob.getValue().getMonthValue() + "/" + checkInDob.getValue().getDayOfMonth() + "/" + checkInDob.getValue().getYear();
        Member memToCheckIn = new Member(checkInFirstName.getText(), checkInLastName.getText(), new Date(dateOfBirth));
        System.out.println(memToCheckIn.fullName());
        System.out.println(memToCheckIn.dob().toString());
        /*
        FitnessClass fitClass = new FitnessClass(instructor, className, location);
        int fitClassIndex = classes.getClassIndex(fitClass);
        FitnessClass classToCheckInto = classes.returnList()[fitClassIndex];
        Member memToCheckIn = new Member(memberFirstName.getText(), memLastName, dob);
        classToCheckInto.checkInMember(memToCheckIn, classes);
         */
    }

    /**
     * Performs checks to make sure that member data is valid
     * Checks location, if the member is already in database, and if the member's date of birth and
     * expiration date are valid
     */
    public void addMember() {
        Member memToAdd = createMem();
        for (int i = 0; i < memData.size(); i++) {
            if (memData.returnList()[i].equals(memToAdd)) {
                System.out.println(memToAdd.fullName() + " is already in the database.");
                return;
            }
        }
        if (!isOldEnough(memToAdd.dob())) return;
        if (memData.add(memToAdd)) System.out.println(memToAdd.fullName() + " added.");
    }

    /**
     * Creates a member using the information inputted
     * Determines if the member created is Premium or Family
     *
     * @return a Member with a first name, last name, date of birth, expiration date, and location
     */
    @FXML
    public Member createMem(){
        String dateOfBirth = addDob.getValue().getMonthValue() + "/" + addDob.getValue().getDayOfMonth() + "/" + addDob.getValue().getYear();
        Date expirationDate = new Date();
        Member memToCheckIn;
        if(standard.isSelected()){
            expirationDate.setExpire(STANDARD_AND_FAMILY_EXPIRATION);
            memToCheckIn = new Member(addFirstName.getText(), addLastName.getText(), new Date(dateOfBirth),
                    expirationDate, Location.valueOf(chooseLocation1.getValue().toString().toUpperCase()));
        }
        else if(family.isSelected()){
            expirationDate.setExpire(STANDARD_AND_FAMILY_EXPIRATION);
            memToCheckIn = new Family(addFirstName.getText(), addLastName.getText(), new Date(dateOfBirth),
                    expirationDate, Location.valueOf(chooseLocation1.getValue().toString().toUpperCase()), 1);
        }
        else if(premium.isSelected()){
            expirationDate.setExpire(PREMIUM_EXPIRATION);
            memToCheckIn = new Family(addFirstName.getText(), addLastName.getText(), new Date(dateOfBirth),
                    expirationDate, Location.valueOf(chooseLocation1.getValue().toString().toUpperCase()), 3);
        }
        else{
            return null;
        }
        return memToCheckIn;
    }

    /**
     * Checks if the member is less than 18 years old
     * Compares the member's age to the current date
     *
     * @param checkDateOfBirth the member who is checking in's date of birth
     * @return false if the member is under 18, else true
     */

    //MOVE TO CONTROLLER
    private boolean isOldEnough(Date checkDateOfBirth) {
        Date currentDate = new Date();
        String dob = checkDateOfBirth.toString();
        if (currentDate.compareTo(checkDateOfBirth) <= 0) {
            System.out.println("DOB " + dob + ": cannot be today or a future date!");
            return false;
        }
        if (currentDate.getYear() - checkDateOfBirth.getYear() < ADULT) {
            System.out.println("DOB " + dob + ": must be 18 or older to join!");
            return false;
        } else if (currentDate.getYear() - checkDateOfBirth.getYear() == ADULT) {
            if (currentDate.getMonth() < checkDateOfBirth.getMonth()) {
                System.out.println("DOB " + dob + ": must be 18 or older to join!");
                return false;
            } else if (currentDate.getMonth() == checkDateOfBirth.getMonth()) {
                if (currentDate.getDay() < checkDateOfBirth.getDay()) {
                    System.out.println("DOB " + dob + ": must be 18 or older to join!");
                    return false;
                }
            }
        }
        return true;
    }

    public void print(){
        memData.print();
    }
    public void printCounty(){
        memData.printByCounty();
    }

    public void printExpire(){
        memData.printByExpirationDate();
    }
    public void printName(){
        memData.printByName();
    }

    public void printFee(){
        memData.printWithFees();
    }

}