package RB.Bartender;

import java.io.Serializable;

/**
 *
 * @authors Anthony Spiteri
 *          Cristian Nuosci
 *          Shahezad Kassam
 */

public class Bottle implements Serializable {
    private Ingredient ingredient;
    private int size;
    private int slot;
    
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
