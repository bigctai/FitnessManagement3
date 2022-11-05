package Project3;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.*;
import javafx.scene.text.Text;
import javafx.scene.text.TextFlow;

import java.net.URL;
import java.util.ResourceBundle;

public class GymManagerController implements Initializable {

    private ObservableList<String> locations = FXCollections.observableArrayList("Edison", "Bridgewater", "Franklin", "Piscataway", "Somerville");
    private String[] teachers = {"Davis", "Kim", "Denise", "Emma", "Jennifer"};
    private ClassSchedule classes = new ClassSchedule();
    private MemberDatabase memData = new MemberDatabase();
    private static final int ADULT = 18;
    private final int STANDARD_AND_FAMILY_EXPIRATION = 3;
    private final int PREMIUM_EXPIRATION = 12;
    private final int NOT_FOUND = -1;
    private final int EXPIRED = -2;
    private final int WRONG_LOCATION = -3;
    private final int DUPLICATE = -4;
    private final int CONFLICT = -5;
    private final int WRONG_GUEST_LOCATION = -6;
    private final int NO_MORE_GUEST = -7;
    private final int STANDARD = -8;
    private static final int NOT_CHECKED_IN = -9;
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
    ToggleGroup membershipOptions = new ToggleGroup();
    @FXML
    private RadioButton family = new RadioButton();
    @FXML
    private RadioButton premium = new RadioButton();
    @FXML
    private DatePicker addDob = new DatePicker();
    @FXML
    ToggleGroup classType = new ToggleGroup();
    @FXML
    private RadioButton pilates = new RadioButton();
    @FXML
    private RadioButton spinning = new RadioButton();
    @FXML
    private RadioButton cardio = new RadioButton();
    @FXML
    ToggleGroup timeOfClass = new ToggleGroup();
    @FXML
    private RadioButton morning = new RadioButton();
    @FXML
    private RadioButton afternoon = new RadioButton();
    @FXML
    private RadioButton evening = new RadioButton();
    @FXML
    Button memCheckIn;
    @FXML
    Button add;
    @FXML
    Button remove;
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
    @FXML
    Button loadMembers;
    @FXML
    Button loadSchedule;
    @FXML
    TextArea output1;


    @Override
    public void initialize(URL arg0, ResourceBundle arg1){
        chooseLocation.getItems().addAll(locations);
        chooseLocation1.getItems().addAll(locations);
        chooseTeacher.getItems().addAll(teachers);
        standard.setToggleGroup(membershipOptions);
        family.setToggleGroup(membershipOptions);
        premium.setToggleGroup(membershipOptions);
        pilates.setToggleGroup(classType);
        cardio.setToggleGroup(classType);
        spinning.setToggleGroup(classType);
        morning.setToggleGroup(timeOfClass);
        afternoon.setToggleGroup(timeOfClass);
        evening.setToggleGroup(timeOfClass);
    }

    @FXML
    public void checkInMem(){
        String dateOfBirth = checkInDob.getValue().getMonthValue() + "/" + checkInDob.getValue().getDayOfMonth() + "/" + checkInDob.getValue().getYear();
        Member memToCheckIn = memData.getFullDetails(new Member(checkInFirstName.getText(), checkInLastName.getText(), new Date(dateOfBirth)));
        RadioButton classTimeButton = (RadioButton) timeOfClass.getSelectedToggle();
        Time classtime = Time.valueOf(classTimeButton.getText().toUpperCase());
        RadioButton classTypeButton = (RadioButton) classType.getSelectedToggle();
        String className = classTypeButton.getText().toUpperCase();
        classType.getSelectedToggle();
        String instructor = chooseTeacher.getValue().toString();
        Location location = Location.valueOf(chooseLocation.getValue().toString().toUpperCase());
        int fitClassIndex = classes.getClassIndex(new FitnessClass(className, instructor, location));
        if (fitClassIndex < 0) return;
        FitnessClass classToCheckInto = classes.returnList()[fitClassIndex];
        if (guest) {
            checkGuest(memberToCheckIn, classToCheckInto);
            return;
        }
        int checkConditions = classToCheckInto.checkInMember(memToCheckIn, classSchedule);
        if (checkConditions == NOT_FOUND) {
            System.out.println(memberToCheckIn[4] + " " + memberToCheckIn[5] + " " + memberToCheckIn[6] + " is not in the database.");
        } else if (checkConditions == EXPIRED) {
            System.out.println(memToCheckIn.fullName() + " " + memberToCheckIn[6] + " membership expired.");
        } else if (checkConditions == WRONG_LOCATION) {
            System.out.println(memToCheckIn.fullName() + " checking in " +
                    classToCheckInto.getLocation().toString() + " - standard membership location restriction.");
        } else if (checkConditions == DUPLICATE) {
            System.out.println(memToCheckIn.fullName() + " already checked in.");
        } else if (checkConditions == CONFLICT) {
            System.out.println("Time conflict - " + classToCheckInto.getClassName().toUpperCase() + " - " + classToCheckInto.getInstructor().toUpperCase() + ", "
                    + classToCheckInto.getTimeOfClass().hourAndMinute() + ", " + classToCheckInto.getLocation().toString() + ".");
        } else {
            System.out.print(memToCheckIn.fullName() + " checked in ");
            classToCheckInto.printClass();
            System.out.println();
        }
    } else {
        System.out.println(memberToCheckIn[0] + " is an invalid command!");
    }
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
                output1.appendText(memToAdd.fullName() + " is already in the database.\n");
                return;
            }
        }
        if (!isOldEnough(memToAdd.dob())) return;
        if (memData.add(memToAdd)){
            output1.appendText(memToAdd.fullName() + " added.\n");
        }
    }

    /**
     * Prints statements depending on if a member was successfully removed
     * Calls "remove" method in MemberDatabase, which returns true if the member is in the database
     * and false otherwise
     */
    public void removeMember() {
        Member memToRemove = createMem();
        if (memData.remove(memToRemove))
            output1.appendText(memToRemove.fullName() + " removed.\n");
        else {
            output1.appendText(memToRemove.fullName() + " is not in the database.\n");
        }
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
    private boolean isOldEnough(Date checkDateOfBirth) {
        Date currentDate = new Date();
        String dob = checkDateOfBirth.toString();
        if (currentDate.compareTo(checkDateOfBirth) <= 0) {
            output1.appendText("DOB " + dob + ": cannot be today or a future date!\n");
            return false;
        }
        if (currentDate.getYear() - checkDateOfBirth.getYear() < ADULT) {
            output1.appendText("DOB " + dob + ": must be 18 or older to join!\n");
            return false;
        } else if (currentDate.getYear() - checkDateOfBirth.getYear() == ADULT) {
            if (currentDate.getMonth() < checkDateOfBirth.getMonth()) {
                output1.appendText("DOB " + dob + ": must be 18 or older to join!\n");
                return false;
            } else if (currentDate.getMonth() == checkDateOfBirth.getMonth()) {
                if (currentDate.getDay() < checkDateOfBirth.getDay()) {
                    output1.appendText("DOB " + dob + ": must be 18 or older to join!\n");
                    return false;
                }
            }
        }
        return true;
    }

    /**
     * Prints out the members in the database in the current order
     */
    public void print(){
        output1.appendText(memData.print());
    }

    /**
     * Prints out the members in the database sorted by county
     */
    public void printCounty(){
        output1.appendText(memData.printByCounty());
    }

    /**
     * Prints out the members in the database sorted by expiration date
     */
    public void printExpire(){
        output1.appendText(memData.printByExpirationDate());
    }
    /**
     * Prints out the members in the database sorted by name
     */
    public void printName(){
        output1.appendText(memData.printByName());
    }
    /**
     * Prints out the members in the database sorted by fee
     */
    public void printFee(){
        output1.appendText(memData.printWithFees());
    }

    public void loadMembers(){
        output1.appendText(memData.loadMembers());
    }
    public void loadSchedule() {
        output1.appendText(classes.loadSchedule());
    }

}