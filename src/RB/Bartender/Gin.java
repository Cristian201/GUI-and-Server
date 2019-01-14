package RB.Bartender;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Collections;

/**
 *
 * @authors Anthony Spiteri
 *          Cristian Nuosci
 *          Shahezad Kassam
 */

public class Gin extends Ingredient implements Serializable, Cloneable {
    private static ArrayList<Gin> brands = new ArrayList<>();
    private static ArrayList<Drink> drinks = new ArrayList<>();
    
    public Gin(String name, double flowRate, int abv, String type) {
        super(name, flowRate, abv, type);
    }
    
    public static ArrayList<Gin> getBrands() throws CloneNotSupportedException {
        ArrayList<Gin> clone = new ArrayList<>();
        for(int i = 0; i < brands.size(); i++) {
            clone.add((Gin) brands.get(i).clone());
        }
        return clone;
    }
    
    public static ArrayList<Drink> getDrinks() throws CloneNotSupportedException {
        ArrayList<Drink> clone = new ArrayList<>();
        for(int i = 0; i < drinks.size(); i++) {
            clone.add((Drink)drinks.get(i).clone());
        }
        return clone;
    }

    public static void initBrands() {
        brands.add(new Gin("Tanqueray Dry Gin", 1, 40, "Gin"));
        brands.add(new Gin("Beefeater Dry Gin", 1, 40, "Gin"));
        brands.add(new Gin("Gordon's Dry Gin", 1, 40, "Gin"));
        brands.add(new Gin("Beefeater London Dry Gin", 1, 40, "Gin"));
        brands.add(new Gin("Bombay Sapphire London Dry Gin", 1, 40, "Gin"));
        brands.add(new Gin("Hendrick's Gin", 1, 40, "Gin"));
        Collections.sort(brands);
    }

    public static void initDrinks() {
        drinks.add(new Drink("Little Devil", new String[]{"Rum", "Gin", "Triple Sec", "Lemon Juice"}, new double[]{0.33, 0.33, 0.17, 0.17}, Glass.getMartini(), false, true, null));
        drinks.add(new Drink("Chelsea Sidecar", new String[]{"Gin", "Triple Sec", "Lemon Juice"}, new double[]{0.375, 0.375, 0.25}, Glass.getMartini(), false, true, null));
        drinks.add(new Drink("Flying Dutchman", new String[]{"Gin", "Triple Sec"}, new double[]{0.78, 0.22}, Glass.getRock(), true, false, null));
        drinks.add(new Drink("Gimlet", new String[]{"Gin", "Lemon Juice", "Simple Syrup"}, new double[]{0.56, 0.22, 0.22}, Glass.getMartini(), false, true, null));
        drinks.add(new Drink("Gin and Cranberry", new String[]{"Gin", "Cranberry Juice"}, new double[]{0.25, 0.75}, Glass.getHighball(), true, false, null));
        drinks.add(new Drink("Gin Neat", new String[]{"Gin"}, new double[]{0.375}, Glass.getRock(), false, false, null));
        drinks.add(new Drink("Gin On The Rocks", new String[]{"Gin"}, new double[]{0.375}, Glass.getRock(), true, false, null));
        drinks.add(new Drink("Gin Shot", new String[]{"Gin"}, new double[]{1.0}, Glass.getShot(), false, true, null));
        drinks.add(new Drink("Ginny Cosmo", new String[]{"Gin", "Triple Sec", "Cranberry Juice", "Lemon Juice"}, new double[]{0.17, 0.17, 0.49, 0.17}, Glass.getMartini(), false, true, null));
        drinks.add(new Drink("Singapore Sling", new String[]{"Gin", "Orange Juice", "Lemon Juice", "Soda Water"}, new double[]{0.2, 0.4, 0.1, 0.3}, Glass.getHighball(), true, false, null));
        drinks.add(new Drink("Tom Collins", new String[]{"Gin", "Lemon Juice", "Simple Syrup", "Soda Water"}, new double[]{0.25, 0.15, 0.1, 0.5}, Glass.getHighball(), true, true, null));
    }
}