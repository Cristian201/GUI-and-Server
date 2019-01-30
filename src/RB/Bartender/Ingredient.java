package RB.Bartender;

import java.io.Serializable;

/**
 *
 * @authors Anthony Spiteri
 *          Cristian Nuosci
 *          Shahezad Kassam
 */

public abstract class Ingredient implements Comparable<Ingredient>, Serializable, Cloneable {
    private String name;
    private double flowRate;
    private int abv;
    private String type;

    public Ingredient(String name, double flowRate, int abv, String type) {
        this.name = name;
        this.flowRate = flowRate;
        this.abv = abv;
        this.type = type;
    }

    @Override
    public int compareTo(Ingredient ingred) {
        return this.name.compareTo(ingred.name);
    }
    
    public String getName() {
        return name;
    }
    
    public double getFlowRate() {
        return flowRate;
    }
        
    public int getABV() {
        return abv;
    }
    
    public String getType() {
        return type;
    }
}
