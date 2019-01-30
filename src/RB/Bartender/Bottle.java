package RB.Bartender;

import java.io.Serializable;

/**
 *
 * @authors Anthony Spiteri
 *          Cristian Nuosci
 *          Shahezad Kassam
 */

public class Bottle implements Serializable {
    private final Ingredient ingredient;
    private final int size;
    private final int slot;
    
    public Bottle(Ingredient ingredient, int size, int slot) {
        this.ingredient = ingredient;
        this.size = size;
        this.slot = slot;
    }
 
    public Ingredient getIngredient() {
        return ingredient;
    }
    
    public int getSize() {
        return size;
    }
    
    public int getSlot() {
        return slot;
    }
}
