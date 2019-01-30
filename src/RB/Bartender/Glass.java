package RB.Bartender;

import java.io.Serializable;

/**
 *
 * @authors Anthony Spiteri
 *          Cristian Nuosci
 *          Shahezad Kassam
 */

public class Glass implements Serializable, Cloneable {
    final String name;
    final double volume;
    
    public Glass(String name, double volume) {
        this.name = name;
        this.volume = volume;
    }
    
    public String getName() {
        return name;
    }
    
    public double getVolume() {
        return volume;
    }
    
    public final static double getVolumeOfIce() {
        return 1;   // need to change
    }
    
    public final static double getVolumeOfDillution() {
        return 1;   // need to change
    }
    
    /*
    public double getRemainingVolume(Drink drink) {
        double volumeOfIngred = 0;
        for(int i = 0; i < drink.getIngredient().length; i++) {
            volumeOfIngred = volumeOfIngred + (drink.getGlass().getVolume() * drink.getAmount()[i]);
        }
        return drink.getGlass().getVolume() - volumeOfIngred - getVolumeOfDillution(drink);
    }
    */
    
    public final static Glass getHighball() {
        return new Glass("Highball", 6);
    }
    
    public final static Glass getMartini() {
        return new Glass("Martini", 3);
    }
    
    public final static Glass getRock() {
        return new Glass("Rock", 4);
    }
    
    public final static Glass getShot() {
        return new Glass("Shot", 1);
    }    
}
