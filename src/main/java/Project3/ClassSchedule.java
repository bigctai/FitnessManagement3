package Project3;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.Scanner;

/**
 * A ClassSchedule class that initializes a list of classes as an array.
 * Provides methods that can add a class to the schedule, return the number
 * of classes, and return the list of classes.
 *
 * @author Chris Tai, Shreyank Yelagoila
 */
public class ClassSchedule {

    private final FitnessClass[] classes;
    private int numOfClasses;
    private static final int NOT_FOUND = -1;

    /**
     * Initializes an array as a list of fitness classes and the
     * number of classes in the list as 0.
     */
    public ClassSchedule() {
        classes = new FitnessClass[15];
        numOfClasses = 0;
    }

    /**
     * Loads the list of classes from the file specified in the path
     * Will print all of the classes in the list from beginning to end
     */
    public String loadSchedule() {
        String filePath = new File("").getAbsolutePath();
        filePath += "/classSchedule";
        File scheduleList = new File(filePath);
        try {
            Scanner classScanner = new Scanner(scheduleList);
            String output = ("-Fitness Classes loaded-\n");
            while (classScanner.hasNextLine()) {
                String[] classInputData = classScanner.nextLine().split(" ");
                FitnessClass fitClass = new FitnessClass(Time.valueOf(classInputData[2].toUpperCase()),
                        classInputData[1], classInputData[0], Location.valueOf(classInputData[3].toUpperCase()), new Member[0]);
                output += fitClass.printClass();
                addClass(fitClass);
            }
            output += "-end of class list-\n";
            return output;
        } catch (FileNotFoundException exception) {
            return exception.toString();
        }
    }

    /**
     * Gets the list of classes in the class schedule
     *
     * @return the list of classes as an array
     */
    public FitnessClass[] returnList() {
        return classes;
    }

    /**
     * Gets the index of the inputted class in the array of classes
     * Also checks if the class inputted is not in the array of classes and checks if the member has already checked in
     * if they are adding and if the member has not checked in if they are dropping
     *
     * @param fitClass the class to be searched for
     * @return the index of the class if it is found, and they haven't checked in, else ALREADY_CHECKED_IN if
     * they already checked in, else NOT_FOUND
     */
    public int getClassIndex(FitnessClass fitClass) {
        int classExists = NOT_FOUND;
        for (int i = 0; i < numOfClasses; i++) {
            FitnessClass classPtr = classes[i];
            if (classPtr.getClassName().equalsIgnoreCase(fitClass.getClassName())) {
                if (classPtr.getInstructor().equalsIgnoreCase(fitClass.getInstructor())) {
                    if (classPtr.getLocation().name().equalsIgnoreCase(fitClass.getLocation().name())) {
                        classExists = i;
                        break;
                    }
                }
            }
        }
        return classExists;
    }

    /**
     * Gets the number of classes in the class schedule
     *
     * @return the number of classes as an integer
     */
    public int getNumOfClasses() {
        return numOfClasses;
    }

    /**
     * Adds a class to the list of classes in the schedule and updates
     * the number of classes in the schedule
     *
     * @param fitClass the fitness class to be added to the list of classes
     */
    public void addClass(FitnessClass fitClass) {
        classes[numOfClasses] = fitClass;
        numOfClasses++;
    }

    /**
     * Prints out each fitness class in the fitness chain
     * Calls the printClass method in FitnessClass
     *
     * @param input Checks to make sure the command is valid
     */
    private void printClasses(String input) {
        if (input.equals("S")) {
            if (numOfClasses == 0) {
                System.out.println("Fitness class schedule is empty");
            } else {
                System.out.println("-Fitness classes-");
                for (int i = 0; i < numOfClasses; i++) {
                    classes[i].printClass();
                }
                System.out.println("-end of class list.\n");
            }
        } else {
            System.out.println(input + " is an invalid command!");
        }
    }

}
