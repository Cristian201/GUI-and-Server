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

public class OrangeJuice extends Ingredient implements Serializable, Cloneable {
    private static ArrayList<OrangeJuice> brands = new ArrayList<>();
    private static ArrayList<Drink> drinks = new ArrayList<>();
    
    public OrangeJuice(String name, double flowRate, int abv, String type) {
        super(name, flowRate, abv, type);
    }
    
    public static ArrayList<OrangeJuice> getBrands() throws CloneNotSupportedException {
        ArrayList<OrangeJuice> clone = new ArrayList<>();
        for(int i = 0; i < brands.size(); i++) {
            clone.add((OrangeJuice) brands.get(i).clone());
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
        brands.add(new OrangeJuice("Minute Maid", 1, 0, "Orange Juice"));
        brands.add(new OrangeJuice("Simply Orange", 1, 0, "Orange Juice"));
        brands.add(new OrangeJuice("Tropicana", 1, 0, "Orange Juice"));
        brands.add(new OrangeJuice("Dole Orange Juice", 1, 0, "Orange Juice"));
        Collections.sort(brands);
    }

    public static void initDrinks() {
        drinks.add(new Drink("Brass Monkey", new String[]{"Vodka", "Rum", "Orange Juice"}, new double[]{0.1, 0.1, 0.8}, Glass.getHighball(), true, false, null));
        drinks.add(new Drink("Hawaiian Sunset", new String[]{"Vodka", "Cranberry Juice", "Orange Juice", "Soda Water"}, new double[]{0.17, 0.33, 0.33, 0.17}, Glass.getHighball(), true, true, null));
        drinks.add(new Drink("Madras", new String[]{"Vodka", "Cranberry Juice", "Orange Juice", "Lemon Juice"}, new double[]{0.17, 0.56, 0.17, 0.1}, Glass.getHighball(), true, false, null));
        drinks.add(new Drink("Orange Crush", new String[]{"Vodka", "Triple Sec", "Orange Juice"}, new double[]{0.25, 0.17, 0.58}, Glass.getHighball(), true, false, null));
        drinks.add(new Drink("Screwdriver", new String[]{"Vodka", "Orange Juice"}, new double[]{0.25, 0.75}, Glass.getHighball(), true, false, null));
        drinks.add(new Drink("Bermuda Triangle", new String[]{"Rum", "Cranberry Juice", "Orange Juice"}, new double[]{0.25, 0.375, 0.375}, Glass.getHighball(), true, false, null));
        drinks.add(new Drink("Singapore Sling", new String[]{"Gin", "Orange Juice", "Lemon Juice", "Soda Water"}, new double[]{0.2, 0.4, 0.1, 0.3}, Glass.getHighball(), true, false, null));
        drinks.add(new Drink("Orange Juice", new String[]{"Orange Juice"}, new double[]{1.0}, Glass.getHighball(), true, false, null));
        drinks.add(new Drink("Sunshine Fizz", new String[]{"Orange Juice", "Cranberry Juice", "Soda Water"}, new double[]{0.33, 0.33, 0.33}, Glass.getHighball(), true, false, null));
        drinks.add(new Drink("Orange Cranberry", new String[]{"Cranberry Juice", "Orange Juice"}, new double[]{0.5, 0.5}, Glass.getHighball(), true, false, null));
        drinks.add(new Drink("Orange Soda", new String[]{"Orange Juice", "Soda Water"}, new double[]{0.75, 0.25}, Glass.getHighball(), true, false, null));
    }
        
    public static boolean isAlcohol() {
        return false;
    }
}