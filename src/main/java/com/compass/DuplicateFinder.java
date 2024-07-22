package com.compass;

import java.util.*;

/*
For the sake of brevity the code is made in a single file,
in a productive code each one should be in its own class (without inner classes) and even be able to assemble objects that collaborate to be able to delegate more and not have such a coupled code
In this case they are solved by separating into different methods, in a larger code,consider the composition of objects.
the initials of the name and lastname are the clustering signature in the algorithm
*
* */
class Contact {
    int contactID;
    String firstName;
    String lastName;
    String email;
    String zipCode;
    String address;

    public Contact(int contactID, String firstName, String lastName, String email, String zipCode, String address) {
        this.contactID = contactID;
        this.firstName = firstName.trim();
        this.lastName = lastName.trim();
        this.email = email.trim();
        this.zipCode = zipCode.trim();
        this.address = address.trim();
    }
}

class Match {
    int sourceID;
    int matchID;
    String accuracy;

    public Match(int sourceID, int matchID, String accuracy) {
        this.sourceID = sourceID;
        this.matchID = matchID;
        this.accuracy = accuracy;
    }

    @Override
    public String toString() {
        return "Source ID: " + sourceID + ", Match ID: " + matchID + ", Accuracy: " + accuracy;
    }
}

public class DuplicateFinder {

    /*
    * The scoring is given by the sum of their weights. When it reaches 1,
    *  it means that the contacts are exactly the same, in terms of their attributes,
    *  there is nothing that can indicate that they are potentially different.
    * */
    private static final double NAME_WEIGHT = 0.1;
    private static final double LAST_NAME_WEIGHT = 0.1;
    private static final double EMAIL_WEIGHT = 0.4;
    private static final double ZIPCODE_WEIGHT = 0.1;
    private static final double ADDRESS_WEIGHT = 0.3;

    public static List<Match> findDuplicates(List<Contact> contacts) {
        List<Match> matches = new ArrayList<>();
        Map<String, List<Contact>> signatureMap = new HashMap<>();

        // Populate signature map
        for (Contact contact : contacts) {
            String signature = createSignature(contact);
            signatureMap.computeIfAbsent(signature, k -> new ArrayList<>()).add(contact);
        }

        // Compare contacts within the same signature group
        for (List<Contact> group : signatureMap.values()) {
            if (group.size() > 1) {
                findMatchesWithinGroup(group, matches);
            }
        }

        return matches;
    }

    private static String createSignature(Contact contact) {
        String firstNameKey = !contact.firstName.isEmpty() ? contact.firstName.substring(0, 1).toLowerCase() : "";
        String lastNameKey = !contact.lastName.isEmpty() ? contact.lastName.substring(0, 1).toLowerCase() : "";
        return firstNameKey + "|" + lastNameKey;
    }

    private static void findMatchesWithinGroup(List<Contact> group, List<Match> matches) {
        for (int i = 0; i < group.size(); i++) {
            for (int j = i + 1; j < group.size(); j++) {
                Contact c1 = group.get(i);
                Contact c2 = group.get(j);

                double score = 0;
                if (initialMatch(c1.firstName, c2.firstName)) score += NAME_WEIGHT;
                if (initialMatch(c1.lastName, c2.lastName)) score += LAST_NAME_WEIGHT;
                if (c1.email.equalsIgnoreCase(c2.email)) score += EMAIL_WEIGHT;
                if (c1.zipCode.equals(c2.zipCode)) score += ZIPCODE_WEIGHT;
                if (c1.address.equalsIgnoreCase(c2.address)) score += ADDRESS_WEIGHT;

                String accuracy;
                if (score == 1) {
                    accuracy = "Equals";
                }
                 else  if (score >= 0.7) {
                    accuracy = "High";
                } else if (score >= 0.5) {
                    accuracy = "Medium";
                } else if (score >= 0.2) {
                    accuracy = "Low";
                } else {
                    accuracy = "Different";
                }

                if (score >= 0.2) { // it a match if the score is at least 0.2 which is name and lastname
                    // according to the example in the definition
                    matches.add(new Match(c1.contactID, c2.contactID, accuracy));
                }
            }
        }
    }

    private static boolean initialMatch(String name1, String name2) {
        if (name1.isEmpty() || name2.isEmpty()) {
            return false;
        }
        return name1.equalsIgnoreCase(name2) ||
                (!name1.isEmpty() && !name2.isEmpty() && name1.charAt(0) == name2.charAt(0));
    }

    public static void main(String[] args) {

        List<Contact> contacts = new ArrayList<>();
        contacts.add(new Contact(1001, "C", "F", "mollis.lectus.pede@outlook.net", "449-6990 Tellus. Rd", "449-6990 Tellus. Rd."));
        contacts.add(new Contact(1002, "C", "French", " mollis.lectus.pede@outlook.net", "39746", "449-6990 Tellus. Rd."));
        contacts.add(new Contact(1003, "Ciara", "F", "non.lacinia.at@zoho.ca", "39746", ""));
        contacts.add(new Contact(1004, "Florencia", "G", "florencia.g@example.com", "12345", "123 Main St"));
        contacts.add(new Contact(1005, "F", "Garcia", "f.garcia@example.com", "12345", "123 Main St"));


        List<Match> matches = findDuplicates(contacts);

        for (Match match : matches) {
            System.out.println(match);
        }
    }
}