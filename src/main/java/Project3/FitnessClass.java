package Project3;

import java.util.ArrayList;

/**
 * Defines a class for members to check in to, with a time, instructor and an array of participants
 * Has methods for finding members checked into each class, checking members in, and dropping members
 * @author Chris Tai, Shreyank Yelagoila
 */
public class FitnessClass {
    private final Time timeOfClass;
    private final String instructor;
    private final String className;
    private Member[] participants;
    private final Location gymLocation;
    private final ArrayList<Member> guests;
    private int size;
    private final int NOT_FOUND = -1;
    private final int EXPIRED = -2;
    private final int WRONG_LOCATION = -3;
    private final int DUPLICATE = -4;
    private final int CONFLICT = -5;
    private final int WRONG_GUEST_LOCATION = -6;
    private final int NO_MORE_GUEST = -7;
    private final int STANDARD = -8;
    private final int NOT_CHECKED_IN = -9;
    private final int INVALID_DATE = -10;

    /**
     * Initializes a projectone.FitnessClass that has a time, an instructor, a name, and
     * an array of participants
     *
     * @param timeOfClass the time the class takes place, passed in as a Time constant
     * @param instructor the name of the instructor, passed in as a String
     * @param className the name of the class, passed in as a String
     * @param gymLocation the location of the gym
     * @param participants the array of participants in the class
     */
    public FitnessClass(Time timeOfClass, String instructor, String className, Location gymLocation, Member[] participants) {
        this.timeOfClass = timeOfClass;
        this.instructor = instructor;
        this.className = className;
        this.participants = participants;
        this.gymLocation = gymLocation;
        this.size = this.participants.length;
        guests = new ArrayList<>();
    }

    public FitnessClass(String className, String instructor, Location gymLocation) {
        this.instructor = instructor;
        this.className = className;
        this.gymLocation = gymLocation;
        timeOfClass = null;
        this.participants = null;
        guests = new ArrayList<>();
        this.size = 0;
    }

    /**
     * Gets the name of the class
     *
     * @return className as a String
     */
    public String getClassName() {
        return this.className;
    }

    /**
     * Gets the location of the fitness class
     *
     * @return the location as a location variable
     */
    public Location getLocation(){ return gymLocation;}

    /**
     * Gets the instructor of the fitness class
     *
     * @return the instructor as a String
     */
    public String getInstructor(){
        return instructor;
    }

    /**
     * Gets the list of participants in the fitness class
     *
     * @return the list of participants as a Member array
     */
    public Member[] getParticipants(){return participants;}

    /**
     * Gets the size of the fitness class
     *
     * @return the size of the class as an integer
     */
    public int getSize(){return size;}

    /**
     * Gets the list of guests in the fitness class
     *
     * @return the list of guests as a Member arraylist
     */
    public ArrayList<Member> getGuests(){return guests;}

    /**
     * Gets the time of the class
     *
     * @return timeOfClass as a Time object
     */
    public Time getTimeOfClass() {
        return this.timeOfClass;
    }

    /**
     * Finds the member in the array of participants
     *
     * @param mem the member that the method searches for
     * @return the index of the member in the array if they exist, else NOT_FOUND
     */
    public int findParticipant(Member mem) {
        for (int i = 0; i < size; i++) {
            if (mem.equals(participants[i])) {
                return i;
            }
        }
        return NOT_FOUND;
    }

    /**
     * Doubles the size of the participants array
     * Creates a new array with double the size, and copies the elements over
     */
    private void grow() {
        Member[] tempList = new Member[size == 0 ? 1 : size * 2];
        if (size >= 0) System.arraycopy(participants, 0, tempList, 0, size);
        participants = tempList;
    }

    /**
     * Performs a series of checks before adding member to participants array
     *
     * @param memToCheckIn the member trying to check into the class
     * @param classes Schedule of classes needed to check if there is a scheduling conflict
     * @return differing negative integers based on the conditions they fail, 0 if they pass all
     */
    public int checkInMember(Member memToCheckIn, ClassSchedule classes) {
        if (size == participants.length) {
            grow();
        }
        if(!memToCheckIn.dob().isValid()){
            return INVALID_DATE;
        }
        if (memToCheckIn.getLocation() == null) {
            return NOT_FOUND;
        }
        if (memToCheckIn.expirationDate().compareTo(new Date()) < 0) {
            return EXPIRED;
        }
        if(checkLocationRestriction(memToCheckIn)){
            return WRONG_LOCATION;
        }
        if (findParticipant(memToCheckIn) >= 0) {
            return DUPLICATE;
        }
        if(checkSchedulingConflict(classes, memToCheckIn)){
            return CONFLICT;
        }
        participants[size] = memToCheckIn;
        size++;
        return 0;
    }

    /**
     * Checks a guest into the fitness class and adds the member to guests arraylist
     * Performs a series of checks to see if the guest is allowed to be checked in
     *
     * @param mem member who is checking in a guest
     * @return differing negative integers based on the conditions they fail, 0 if they pass all
     */
    public int checkGuest(Member mem){
        if(mem instanceof Family || mem instanceof Premium){
            if(!(mem.getLocation().toString().equalsIgnoreCase(gymLocation.toString()))){
                return WRONG_GUEST_LOCATION;
            } else if(((Family) mem).getGuestPass() == 0){
                return NO_MORE_GUEST;
            } else {
                ((Family) mem).guestIn();
                guests.add(mem);
                return 0;
            }
        } else {
            return STANDARD;
        }
    }

    /**
     * Checks if the member is allowed to check into the fitness class at this location
     *
     * @param memToCheckIn member that is checking into this location
     * @return true if the member is restricted from checking into this location and false otherwise
     */
    private boolean checkLocationRestriction(Member memToCheckIn){
        if(memToCheckIn instanceof Family || memToCheckIn instanceof Premium){
            return false;
        }
        else return !memToCheckIn.getLocation().equals(gymLocation);
    }

    /**
     * Checks if the member has a scheduling conflict
     * If the member has checked into another class that is at the same time as the class they are trying to
     * check into, then they have a scheduling conflict
     *
     * @param classes     the database of classes to compare against
     * @param memToCheckIn the member who is trying to check in
     * @return true if the member has a scheduling conflict, else false
     */
    private boolean checkSchedulingConflict(ClassSchedule classes, Member memToCheckIn) {
        for (int i = 0; i < classes.getNumOfClasses(); i++) {
            if (classes.returnList()[i].getTimeOfClass().equals(timeOfClass)
                    && classes.returnList()[i].findParticipant(memToCheckIn) >= 0) {
                    return true;
                }
            }
        return false;
    }

    /**
     * Removes a member from a class's participants
     * Performs a series of checks to see if the member can be removed
     *
     * @param memToDrop the member trying to drop the class
     * @return differing negative integers based on the conditions they fail, 0 if they pass all
     */
    public int dropMem(Member memToDrop) {
        if(!memToDrop.dob().isValid()){
            return INVALID_DATE;
        }
        if (memToDrop.getLocation() == null) {
            return NOT_FOUND;
        }
        else if(findParticipant(memToDrop) < 0){
            return NOT_CHECKED_IN;
        }
        else{
            int index = findParticipant(memToDrop);
            for (int i = index; i < size; i++) {
                participants[i] = participants[i++];
            }
            participants[findParticipant(memToDrop)] = participants[size - 1];
            participants[size - 1] = null;
            decrementSize();
            return 0;
        }
    }

    /**
     * Reduces the size variable by 1
     */
    public void decrementSize(){
        size--;
    }

    /**
     * Removes a guest from this fitness class
     * Checks to see if the guest can be removed
     *
     * @param guest the member who is trying to remove their guest from this class
     * @return -9 if the guest is not checked in, otherwise returns 0
     */
    public int removeGuest(Member guest){
        if(!guests.contains(guest)){
            return NOT_CHECKED_IN;
        }
        else {
            ((Family) guest).guestOut();
            guests.remove(guest);
            return 0;
        }
    }

    /**
     * Prints out the class along with the participants in it
     * Prints out the name of the class, instructor, and the time of the class, followed by each participant
     */
    public String printClass() {
        String output = className.toUpperCase() + " - " + instructor.toUpperCase() + ", " +
                timeOfClass.hourAndMinute() + ", " + gymLocation.name() + "\n";
        if (size > 0) {
            output += "- Participants -\n";
            for (int i = 0; i < size; i++) {
                output += "\t" + participants[i].toString() + "\n";
            }
        }
        if (guests.size() > 0) {
            output += "- Guests -\n";
            for (Member guest : guests) {
                output += "\t" + guest.toString() +"\n";
            }
        }
        return output;
    }
}


