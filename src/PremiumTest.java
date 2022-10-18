package projecttwo;

import org.junit.Test;

import static org.junit.Assert.*;

/**
 * A Premium test class that provides the JUnit testing for the Premium class.
 * Provides methods for every test case applicable to the membershipFee method.
 *
 * @author Chris Tai, Shreyank Yelagoila
 */
public class PremiumTest {

    private static final double MEMBER_FEE = 149.96;
    private static final double FAMILY_FEE = 209.96;
    private static final double PREMIUM_FEE = 659.89;
    private static final double VARIANCE = .01;

    /**
     * Tests the membershipFee method for a member with Standard membership
     * Will return MEMBER_FEE if method works
     */
    @Test
    public void testMembershipFeeForStandard() {
        Member mem = new Member("John", "Doe", new Date("01/01/2002"), new Date("01/01/2023"), Location.BRIDGEWATER);
        assertEquals(MEMBER_FEE, mem.membershipFee(), VARIANCE);
        assertNotEquals(PREMIUM_FEE, mem.membershipFee(), VARIANCE);
        assertNotEquals(FAMILY_FEE, mem.membershipFee(), VARIANCE);
    }

    /**
     * Tests the membershipFee method for a member with Family membership
     * Will return FAMILY_FEE if method works
     */
    @Test
    public void testMembershipFeeForFamily() {
        Member fam = new Family("John", "Doe", new Date("01/01/2002"), new Date("01/01/2023"), Location.BRIDGEWATER, 1);
        assertEquals(FAMILY_FEE, fam.membershipFee(), VARIANCE);
        assertNotEquals(MEMBER_FEE, fam.membershipFee(), VARIANCE);
        assertNotEquals(PREMIUM_FEE, fam.membershipFee(), VARIANCE);
    }

    /**
     * Tests the membershipFee method for a member with Premium membership
     * Will return PREMIUM_FEE if method works
     */
    @Test
    public void testMembershipFeeForPremium() {
        Member prem = new Premium("John", "Doe", new Date("01/01/2002"), new Date("01/01/2023"), Location.BRIDGEWATER, 3);
        assertEquals(PREMIUM_FEE, prem.membershipFee(), VARIANCE);
        assertNotEquals(MEMBER_FEE, prem.membershipFee(), VARIANCE);
        assertNotEquals(FAMILY_FEE, prem.membershipFee(), VARIANCE);
    }
}