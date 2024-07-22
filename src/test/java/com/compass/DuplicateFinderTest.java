package com.compass;
import org.junit.After;
import org.junit.Assert;
import org.junit.Test;
import org.junit.Before;

import java.util.ArrayList;
import java.util.List;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

public class DuplicateFinderTest {

    private List<Contact> contacts;

    @Before
    public void setUp() {
        System.out.println("Before setUp");
        contacts = new ArrayList<>();
    }

    @After
    public void tearDown() {
        contacts = null;
        System.out.println("Contact list is empty");
    }

    //first and second test are the ones requested by the requirements, others are necessary since
    // the algorithm checks for potentials duplicates, it could be the case are different, or are the same.
    @Test
    public void testSameInitialEmailAndAddressShouldReturnHigh() {
        contacts.add(new Contact(1001, "C", "F", "mollis.lectus.pede@outlook.net", "449-6990 Tellus. Rd", "449-6990 Tellus. Rd."));
        contacts.add(new Contact(1002, "C", "French", " mollis.lectus.pede@outlook.net", "39746", "449-6990 Tellus. Rd."));
        List<Match> matches = DuplicateFinder.findDuplicates(contacts);
        assertMatch(matches.get(0),1001,1002, "High");

    }

    //the same name or initials does not imply there are the same contact,
    // since there are lots of persons who share the same full name
    @Test
    public void testSameOnlyInitialNamShouldReturnLow() {
        contacts.add(new Contact(1001, "C", "F", "mollis.lectus.pede@outlook.net", "39723. Rd", "449-6990 Tellus. Rd."));
        contacts.add(new Contact(1003, "Ciara", "F", "non.lacinia.at@zoho.ca", "39746", ""));

        List<Match> matches = DuplicateFinder.findDuplicates(contacts);
        assertMatch(matches.get(0),1001,1003, "Low");
    }

    @Test
    public void testSameDiferentInitialsAreNotMatch() {
        contacts.add(new Contact(1003, "Ciara", "F", "non.lacinia.at@zoho.ca", "39746", ""));
        contacts.add(new Contact(1004, "Florencia", "G", "florencia.g@example.com", "12345", "123 Main St"));
        List<Match> matches = DuplicateFinder.findDuplicates(contacts);
        assertTrue(matches.isEmpty());
    }

    //as per the requirements, one of them has a white space at the beginning
    @Test
    public void testTheSameContactTwiceAreMatchShouldReturnEquals() {
        contacts.add(new Contact(1006, "Florencia", "Garcia", "florencia.g@example.com", "12345", "123 Main St"));
        contacts.add(new Contact(1007, "Florencia", "Garcia", " florencia.g@example.com", "12345", "123 Main St"));
        List<Match> matches = DuplicateFinder.findDuplicates(contacts);
        assertMatch(matches.get(0),1006,1007,"Equals");
    }

    @Test
    public void testDifferentContactShouldNotReturnMatches() {
        contacts.add(new Contact(1010, "Fernanda", "Lowenr", "mollis.lectus.pede@outlook.net", "39723. Rd", "449-6990 Tellus. Rd."));
        contacts.add(new Contact(1011, "Ciara", "F", "non.lacinia.at@zoho.ca", "39746", "344 Lake street Tellus. Rd."));

        List<Match> matches = DuplicateFinder.findDuplicates(contacts);
        Assert.assertTrue(matches.isEmpty());
    }

    private void assertMatch(Match match, int expectedSourceID, int expectedMatchID, String expectedAccuracy) {
        assertEquals(expectedSourceID, match.sourceID);
        assertEquals(expectedMatchID, match.matchID);
        assertEquals(expectedAccuracy, match.accuracy);
    }
}
