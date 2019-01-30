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

public class SodaWater extends Ingredient implements Serializable, Cloneable {
    private static ArrayList<SodaWater> brands = new ArrayList<>();
    private static ArrayList<Drink> drinks = new ArrayList<>();
    
    public SodaWater(String name, double flowRate, int abv, String type) {
        super(name, flowRate, abv, type);
    }
    
    public static ArrayList<SodaWater> getBrands() throws CloneNotSupportedException {
        ArrayList<SodaWater> clone = new ArrayList<>();
        for(int i = 0; i < brands.size(); i++) {
            clone.add((SodaWater) brands.get(i).clone());
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
        brands.add(new SodaWater("Schweppes Club Soda", 1, 0, "Soda Water"));
        brands.add(new SodaWater("San Pellegrino Sparkling Mineral Water", 1, 0, "Soda Water"));
        brands.add(new SodaWater("Perroer Sparkling Natural Mineral Water", 1, 0, "Soda Water"));
        brands.add(new SodaWater("Canada Dry Club Soda", 1, 0, "Soda Water"));
        Collections.sort(brands);
    }

    public static void initDrinks() {
        drinks.add(new Drink("Cape Cod", new String[]{"Vodka", "Cranberry Juice", "Soda Water"}, new double[]{0.17, 0.7, 0.13}, Glass.getHighball(), true, false, null));
        drinks.add(new Drink("Hawaiian Sunset", new String[]{"Vodka", "Cranberry Juice", "Orange Juice", "Soda Water"}, new double[]{0.17, 0.33, 0.33, 0.17}, Glass.getHighball(), true, true, null));
        drinks.add(new Drink("Sparking Vodka Cranberry", new String[]{"Vodka", "Cranberry Juice", "Soda Water"}, new double[]{0.25, 0.55, 0.2}, Glass.getHighball(), true, false, null));
        drinks.add(new Drink("Vodka Collins", new String[]{"Vodka", "Lemon Juice", "Simple Syrup", "Soda Water"}, new double[]{0.25, 0.15, 0.1 , 0.5}, Glass.getHighball(), true, true, null));
        drinks.add(new Drink("Singapore Sling", new String[]{"Gin", "Orange Juice", "Lemon Juice", "Soda Water"}, new double[]{0.2, 0.4, 0.1, 0.3}, Glass.getHighball(), true, false, null));
        drinks.add(new Drink("Tom Collins", new String[]{"Gin", "Lemon Juice", "Simple Syrup", "Soda Water"}, new double[]{0.25, 0.15, 0.1, 0.5}, Glass.getHighball(), true, true, null));
        drinks.add(new Drink("Sunshine Fizz", new String[]{"Orange Juice", "Cranberry Juice", "Soda Water"}, new double[]{0.33, 0.33, 0.33}, Glass.getHighball(), true, false, null));
        drinks.add(new Drink("Cranberry Soda", new String[]{"Cranberry Juice", "Soda Water"}, new double[]{0.33, 0.33, 0.33}, Glass.getHighball(), true, false, null));
        drinks.add(new Drink("Orange Soda", new String[]{"Orange Juice", "Soda Water"}, new double[]{0.75, 0.25}, Glass.getHighball(), true, false, null));
        drinks.add(new Drink("Soda Water", new String[]{"Soda Water"}, new double[]{1.0}, Glass.getHighball(), true, false, null));
    }
        
    public static boolean isAlcohol() {
        return false;
    }
}