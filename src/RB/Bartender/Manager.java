package RB.Bartender;

import java.io.Serializable;

/**
 *
 * @authors Anthony Spiteri
 *          Cristian Nuosci
 *          Shahezad Kassam
 */

public class Manager extends Account implements Serializable {
    Manager(String tagNumber, String firstName, String lastName) {
        super(tagNumber, firstName, lastName);
    }
}
