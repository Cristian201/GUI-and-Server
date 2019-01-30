package RB.Bartender;

import java.io.Serializable;

/**
 *
 * @authors Anthony Spiteri
 *          Cristian Nuosci
 *          Shahezad Kassam
 */

public class Account implements Serializable{
    private final String tagNumber;
    private final String firstName;
    private final String lastName;
    
    public Account(String tagNumber, String firstName, String lastName) {
        this.tagNumber = tagNumber;
        this.firstName = firstName;
        this.lastName = lastName;
    }
    
    public String getTagNumber() {
        return tagNumber;
    }
    
    public String getFirstName() {
        return firstName;
    }
    
    public String getLastName() {
        return lastName;
    }
}