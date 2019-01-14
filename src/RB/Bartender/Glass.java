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
    final double volumeOfIce = 1;
    
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
    
    public double getVolumeOfIce() {
        return volumeOfIce;
    }
    
    public double getVolumeOfDillution(Drink drink) {
        if(drink.getShake() == true && drink.getIce() == true) {
            return 1;   // need to change
        }
        
        return 0;
    }
    
    public double getRemainingVolume(Drink drink) {
        double volumeOfIngred = 0;
        for(int i = 0; i < drink.getIngredient().length; i++) {
            volumeOfIngred = volumeOfIngred + (drink.getGlass().getVolume() * drink.getAmount()[i]);
        }
        return drink.getGlass().getVolume() - volumeOfIngred - getVolumeOfDillution(drink);
    }
    
    public static Glass getHighball() {
        return new Glass("Highball", 6);
    }
    
    public static Glass getMartini() {
        return new Glass("Martini", 3);
    }
    
    public static Glass getRock() {
        return new Glass("Rock", 4);
    }
    
    public static Glass getShot() {
        return new Glass("Shot", 1);
    }    
}
