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

}
