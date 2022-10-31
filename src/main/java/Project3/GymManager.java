package Project3;

import java.util.Scanner;

/**
 * Processes the inputs and calls other classes based on the input
 * Has methods for checking if the inputs are valid and printing out statements accordingly
 *
 * @author Chris Tai, Shreyank Yelagoila
 */
public class GymManager {
    private final MemberDatabase memData = new MemberDatabase();
    private final ClassSchedule classSchedule = new ClassSchedule();
    private final Scanner scanUserInput = new Scanner(System.in);
    private String input;
    private final int NOT_FOUND = -1;
    private final int EXPIRED = -2;
    private final int WRONG_LOCATION = -3;
    private final int DUPLICATE = -4;
    private final int CONFLICT = -5;
    private final int WRONG_GUEST_LOCATION = -6;
    private final int NO_MORE_GUEST = -7;
    private final int STANDARD = -8;
    private static final int NOT_CHECKED_IN = -9;
    private static final int ADULT = 18;
    private final int STANDARD_AND_FAMILY_EXPIRATION = 3;
    private final int PREMIUM_EXPIRATION = 12;

    /**
     * Calls methods based on user input
     * Reads user input and, depending on the command put in, runs a method associated with that command
     * and other inputted data
     */
    public void run() {
        System.out.println("Gym Manager running...");
        while (!(input = scanUserInput.nextLine()).equals("Q")) {
            String[] inputData = input.split(" ");
            if (inputData.length > 0 && inputData[0].length() > 0) {
                switch (inputData[0].charAt(0)) {
                    case 'A':
                        addMember(inputData);
                        break;
                    case 'R':
                        removeMember(inputData);
                        break;
                    case 'P':
                        printMembers(inputData[0]);
                        break;
                    case 'C':
                        checkIn(inputData);
                        break;
                    case 'D':
                        dropClass(inputData);
                        break;
                    default:
                        System.out.println(inputData[0] + " is an invalid command!");
                }
            }
        }
        System.out.println("Gym Manager terminated.");
    }

    /**
     * Performs checks to make sure that member data is valid
     * Checks location, if the member is already in database, and if the member's date of birth and
     * expiration date are valid
     *
     * @param memberToAdd contains member data as elements of an array
     */
    private void addMember(String[] memberToAdd) {
        if (!memberToAdd[0].equals("AF") && !memberToAdd[0].equals("AP") && !memberToAdd[0].equals("A")) {
            System.out.println(memberToAdd[0] + " is an invalid command!");
            return;
        }
        Member memToAdd = createMember(memberToAdd, false);
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
     * @param memberToAdd the information about the member held in an array of Strings
     * @param fromFile    determines whether the Member is added from file or command line
     * @return a Member with a first name, last name, date of birth, expiration date, and location
     */
    private Member createMember(String[] memberToAdd, boolean fromFile) {
        String firstName;
        String lastName;
        Date dob;
        Date expirationDate;
        Location location = Location.valueOf(memberToAdd[4].toUpperCase());
        firstName = memberToAdd[1];
        lastName = memberToAdd[2];
        dob = new Date(memberToAdd[3]);
        expirationDate = new Date();
        if (memberToAdd[0].equals("AF")) {
            expirationDate.setExpire(STANDARD_AND_FAMILY_EXPIRATION);
            return new Family(firstName, lastName, dob, expirationDate, location, 1);
        } else if (memberToAdd[0].equals("AP")) {
            expirationDate.setExpire(PREMIUM_EXPIRATION);
            return new Premium(firstName, lastName, dob, expirationDate, location, 3);
        } else {
            expirationDate.setExpire(STANDARD_AND_FAMILY_EXPIRATION);
        }
        return new Member(firstName, lastName, dob, expirationDate, location);
    }

    /**
     * Prints statements depending on if a member was successfully removed
     * Calls "remove" method in MemberDatabase, which returns true if the member is in the database
     * and false otherwise
     *
     * @param memberToRemove contains member data as elements of an array
     */
    private void removeMember(String[] memberToRemove) {
        if (!memberToRemove[0].equals("R")) {
            System.out.println(memberToRemove[0] + " is an invalid command!");
            return;
        }
        if (memData.remove(new Member(memberToRemove[1].toUpperCase(), memberToRemove[2].toUpperCase(),
                new Date(memberToRemove[3]))))
            System.out.println(memberToRemove[1] + " " + memberToRemove[2] + " removed.");
        else {
            System.out.println(memberToRemove[1] + " " + memberToRemove[2] + " is not in the database.");
        }
    }

    /**
     * Performs checks to make sure that the member is allowed to check in
     * Checks if member's date of birth is valid, if their membership expired, if member exists,
     * if class exists, and if member has already checked in to the class or to another class
     *
     * @param memberToCheckIn contains member data as elements of an array
     */

    //MOVE TO CONTROLLER and GET RID OF CASES NOT NEEDED
    private void checkIn(String[] memberToCheckIn) {
        if (memberToCheckIn[0].equals("C") || memberToCheckIn[0].equals("CG")) {
            int fitClassIndex = classSchedule.getClassIndex(new FitnessClass(memberToCheckIn[2], memberToCheckIn[3], Location.valueOf(memberToCheckIn[1])));
            if (fitClassIndex < 0) return;
            FitnessClass classToCheckInto = classSchedule.returnList()[fitClassIndex];
            if (memberToCheckIn[0].equals("CG")) {
                checkGuest(memberToCheckIn, classToCheckInto);
                return;
            }
            Member memToCheckIn = memData.getFullDetails(new Member(memberToCheckIn[4], memberToCheckIn[5],
                    new Date(memberToCheckIn[6])));
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
    }

    /**
     * Performs checks to ensure that the guest of a certain member is allowed to check in.
     * Checks if the member is eligible to check in a guest, has guest passes remaining, and
     * is checking in a guest at the original membership location.
     *
     * @param memberInfo contains the member data as elements of a string array
     * @param fitClass   the fitness class that the member is trying to check the guest into
     */
    //MOVE to CONTROLLER
    private void checkGuest(String[] memberInfo, FitnessClass fitClass) {
        Member mem = memData.getFullDetails(new Member(memberInfo[4], memberInfo[5], new Date(memberInfo[6])));
        int checkGuestCondition = fitClass.checkGuest(mem);
        if (checkGuestCondition == WRONG_GUEST_LOCATION) {
            System.out.println(mem.fullName() + " Guest checking in " + fitClass.getLocation() + " - guest location restriction.");
        } else if (checkGuestCondition == NO_MORE_GUEST) {
            System.out.println(mem.fullName() + " ran out of guest pass.");
        } else if (checkGuestCondition == STANDARD) {
            System.out.println("Standard membership - guest check-in is not allowed");
        } else {
            System.out.print(mem.fullName() + " (guest) checked in ");
            fitClass.printClass();
            System.out.println();
        }
    }

    /**
     * Performs checks to make sure that the member can be dropped
     * Checks if member's date of birth is valid, if the member is a participant in the class,
     * and if the class exists
     *
     * @param memberToDrop contains member data as elements of a String array
     */

    //MOVE TO CONTROLLER
    private void dropClass(String[] memberToDrop) {
        if (memberToDrop[0].equals("D") || memberToDrop[0].equals("DG")) {
            int fitClassIndex = classSchedule.getClassIndex(new FitnessClass(memberToDrop[2], memberToDrop[3], Location.valueOf(memberToDrop[1])));
            if (fitClassIndex < 0) {
                return;
            }
            FitnessClass classToDrop = classSchedule.returnList()[fitClassIndex];
            Member memToDrop = memData.getFullDetails(new Member(memberToDrop[4], memberToDrop[5],
                    new Date(memberToDrop[6])));
            if (memberToDrop[0].equals("DG")) {
                dropGuest(memToDrop, classToDrop);
                return;
            }
            int dropClassCondition = classToDrop.dropMem(memToDrop);
            if (dropClassCondition == NOT_FOUND) {
                System.out.println(memberToDrop[4] + " " + memberToDrop[5] + " " + memberToDrop[6] + " is not in the database.");
            } else if (dropClassCondition == NOT_CHECKED_IN) {
                System.out.println(memToDrop.fullName() + " did not check in.");
            } else {
                System.out.println(memToDrop.fullName() + " done with the class.");
            }
        } else {
            System.out.println(memberToDrop[0] + " is an invalid command!");
        }
    }

    /**
     * Performs checks to make sure that the members guest can drop or end their class session.
     * Checks if the member checked a guest into the class before ending the class session for the guest.
     *
     * @param memToDrop   the member whose guest is trying to drop the class
     * @param classToDrop the fitness class that the guest is trying to drop
     */

    //MOVE TO CONTROLLER
    public void dropGuest(Member memToDrop, FitnessClass classToDrop) {
        int dropGuestCondition = classToDrop.removeGuest(memToDrop);
        if (dropGuestCondition == NOT_CHECKED_IN) {
            System.out.println(memToDrop.fullName() + " did not check in.");
        } else {
            System.out.println(memToDrop.fullName() + " Guest done with the class.");
        }
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
        String dob = checkDateOfBirth.dateString();
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

    /**
     * Prints all of the attributes of a member depending on the command inputted as the argument
     * Calls outside methods to print the correct statement
     *
     * @param sortType string command used to print certain attributes of a member
     */
    private void printMembers(String sortType) {
        switch (sortType) {
            case "P":
                memData.print();
                break;
            case "PC":
                memData.printByCounty();
                break;
            case "PD":
                memData.printByExpirationDate();
                break;
            case "PN":
                memData.printByName();
                break;
            case "PF":
                memData.printWithFees();
                break;
        }
    }
}
