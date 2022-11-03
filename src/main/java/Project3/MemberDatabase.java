package Project3;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.Scanner;

/**
 * Keeps track of all members of the fitness chain
 * Contains members in an array, and has methods to find a member in the array, increase the array length,
 * add members, remove members, and print members based on different sorts
 *
 * @author Christopher Tai, Shreyank Yelagoila
 */
public class MemberDatabase {
    private Member[] mlist;
    private int size;
    private final int NOT_FOUND = -1;

    /**
     * Initializes an array of members with a length of 4
     */
    public MemberDatabase() {
        this.mlist = new Member[4];
        size = 0;
    }

    /**
     * Loads the list of members from the file specified in the path
     * Will print all of the members in the list from beginning to end
     */
    public String loadMembers() {
        String filePath = new File("").getAbsolutePath();
        filePath += "/memberList";
        File memberList = new File(filePath);
        try {
            String output = "-list of members loaded-";
            Scanner memberScanner = new Scanner(memberList);
            while (memberScanner.hasNextLine()) {
                String[] memberInputData = memberScanner.nextLine().replaceAll("  ", " ").split(" ");
                Member member = new Member(memberInputData[0], memberInputData[1], new Date(memberInputData[2]),
                        new Date(memberInputData[3]), Location.valueOf(memberInputData[4].toUpperCase()));
                add(member);
                output += member + "\n";
            }
            output += "-end of list-\n";
            return output;
        } catch (FileNotFoundException exception) {
            return exception.toString();
        }
    }

    /**
     * Gets the Member database
     *
     * @return the array of Members
     */
    public Member[] returnList() {
        return mlist;
    }

    /**
     * Gets the size of the member database
     *
     * @return the size of the array, in integer value
     */
    public int size() {
        return size;
    }

    /**
     * Locates a member in the database
     * Traverses through the array and, if a value equals the member passed in, then it has
     * successfully located that member
     *
     * @param member the member to be found in the database
     * @return the index of the member if the member exists, otherwise -1
     */
    private int find(Member member) {

        for (int i = 0; i < size; i++) {
            if (member.equals(mlist[i])) {
                return i;
            }
        }
        return NOT_FOUND;
    }

    /**
     * Doubles the size of the array
     * If the array is full, copies all previous values of the array onto a new array and
     * sets mlist to the new array
     */
    private void grow() {
        Member[] tempList = new Member[size + 4];
        if (size >= 0) System.arraycopy(mlist, 0, tempList, 0, size);
        mlist = tempList;
    }

    /**
     * Adds a member to the database
     * If the date of birth or expiration date are invalid, doesn't add. If the array is full, call grow
     *
     * @param member the member to be added to the database
     * @return true if the member does not exist and the data is valid, else return false
     */
    public boolean add(Member member) {
        if (size == mlist.length) {
            grow();
        }
        mlist[size] = member;
        size++;
        return true;
    }

    /**
     * Removes member from database
     * If the member exists, shifts every member afterwards
     *
     * @param member the member to be removed from the database
     * @return true if the member is in the database, otherwise false
     */
    public boolean remove(Member member) {
        int index = find(member);
        if (index >= 0) {
            for (int i = index; i < size - 1; i++) {
                int nextElement = i + 1;
                mlist[i] = mlist[nextElement];
            }
            size--;
            return true;
        } else {
            return false;
        }
    }

    /**
     * Prints the unsorted array content
     */
    public String print() {
        if (size == 0) {
            return "Member database is empty!\n";
        }
        String output = "\n-list of members-\n";
        for (int i = 0; i < size; i++) {
            output += mlist[i].toString() + "\n";
        }
        output += "-end of list-\n";
        return output;
    }

    /**
     * Gets the full details of a member by finding the member in the member list array
     *
     * @param mem the member whose details are to be returned
     * @return the member from the member list array
     */
    public Member getFullDetails(Member mem) {
        if (find(mem) >= 0) return mlist[find(mem)];
        else {
            return mem;
        }
    }

    /**
     * Prints the array content sorted by county name, then by zipcode
     */
    public String printByCounty() {
        if (size == 0) {
            return "Member database is empty!\n";
        }
        for (int i = 0; i < size; i++) {
            for (int j = i + 1; j < size; j++) {
                Member tempSmallest;
                if (mlist[i].getLocation().county().compareTo(mlist[j].getLocation().county()) > 0) {
                    tempSmallest = mlist[i];
                    mlist[i] = mlist[j];
                    mlist[j] = tempSmallest;
                } else if (mlist[i].getLocation().county().compareTo(mlist[j].getLocation().county()) == 0 &&
                        Integer.parseInt(mlist[j].zipCode()) <= Integer.parseInt(mlist[i].zipCode())) {
                    tempSmallest = mlist[i];
                    mlist[i] = mlist[j];
                    mlist[j] = tempSmallest;
                }
            }
        }
        String output = "\n-list of members sorted by country and zip code-\n";
        for (int i = 0; i < size; i++) {
            output += mlist[i].toString() + "\n";
        }
        output += "-end of list-\n";
        return output;
    }

    /**
     * Prints the member database sorted by their expiration dates
     * Sorted so that the earlier expiration dates come first
     */
    public String printByExpirationDate() {
        if (size == 0) {
            return "\nMember database is empty!\n";
        }
        for (int i = 0; i < size; i++) {
            for (int j = i + 1; j < size; j++) {
                Member tempSmallest;
                if (mlist[i].expirationDate().compareTo(mlist[j].expirationDate()) >= 0) {
                    tempSmallest = mlist[i];
                    mlist[i] = mlist[j];
                    mlist[j] = tempSmallest;
                }
            }
        }
        String output = "\n-list of members sorted by expiration date-\n";
        for (int i = 0; i < size; i++) {
            output += mlist[i].toString() + "\n";
        }
        output += "-end of list-\n";
        return output;
    } //sort by the expiration date

    /**
     * Prints the member database sorted by their names
     * Sorted by last name first in alphabetical order, then their first name
     */
    public String printByName() {
        if (size == 0) {
            return ("Member database is empty!\n");
        }
        for (int i = 0; i < size; i++) {
            for (int j = i + 1; j < size; j++) {
                Member tempSmallest = null;
                if (mlist[i].compareTo(mlist[j]) > 0) {
                    tempSmallest = mlist[i];
                    mlist[i] = mlist[j];
                    mlist[j] = tempSmallest;
                }
            }
        }
        String output = "\n-list of members sorted by last name, and first name-\n";
        for (int i = 0; i < size; i++) {
            output += mlist[i].toString() + "\n";
        }
        output += "-end of list-\n";
        return output;
    } //sort by last name and then first name

    /**
     * Prints the list of members in the member list array with their
     * corresponding membership fee from beginning to end
     */
    public String printWithFees() {
        String output = "\n-list of members with membership fees-\n";
        if (size == 0) {
            return ("Member database is empty!\n");
        }
        for (int i = 0; i < size; i++) {
            output += mlist[i].toString() + ", Membership fee: $" + mlist[i].membershipFee() + "\n";
        }
        output += "-end of list-\n";
        return output;
    }
}