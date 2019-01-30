package RB.Bartender;

import java.io.Serializable;
import java.util.ArrayList;

/**
 *
 * @authors Anthony Spiteri
 *          Cristian Nuosci
 *          Shahezad Kassam
 */

public class CranberryJuice extends Ingredient implements Serializable, Cloneable {
    private static ArrayList<CranberryJuice> brands = new ArrayList<>();
    private static ArrayList<Drink> drinks = new ArrayList<>();
    
    public CranberryJuice(String name, double flowRate, int abv, String type) {
        super(name, flowRate, abv, type);
    }
    
    public static ArrayList<CranberryJuice> getBrands() throws CloneNotSupportedException {
        ArrayList<CranberryJuice> clone = new ArrayList<>();
        for(int i = 0; i < brands.size(); i++) {
            clone.add((CranberryJuice) brands.get(i).clone());
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
        brands.add(new CranberryJuice("Ocean Spray Cranberry Juice", 1, 0, "Cranberry Juice"));
    }
    
    public static void initDrinks() {
        drinks.add(new Drink("Bloody Nightmare", new String[]{"Vodka", "Lemon Juice", "Cranberry Juice"}, new double[]{0.17, 0.33, 0.5}, Glass.getHighball(), true, false, null));
        drinks.add(new Drink("Cape Cod", new String[]{"Vodka", "Cranberry Juice", "Soda Water"}, new double[]{0.17, 0.7, 0.13}, Glass.getHighball(), true, false, null));
        drinks.add(new Drink("Cosmopolitan", new String[]{"Vodka", "Triple Sec", "Cranberry Juice", "Lemon Juice"}, new double[]{0.17, 0.17, 0.49, 0.17}, Glass.getMartini(), false, true, null));
        drinks.add(new Drink("Hawaiian Sunset", new String[]{"Vodka", "Cranberry Juice", "Orange Juice", "Soda Water"}, new double[]{0.17, 0.33, 0.33, 0.17}, Glass.getHighball(), true, true, null));
        drinks.add(new Drink("Madras", new String[]{"Vodka", "Cranberry Juice", "Orange Juice", "Lemon Juice"}, new double[]{0.17, 0.56, 0.17, 0.1}, Glass.getHighball(), true, false, null));
        drinks.add(new Drink("Sparking Vodka Cranberry", new String[]{"Vodka", "Cranberry Juice", "Soda Water"}, new double[]{0.25, 0.55, 0.2}, Glass.getHighball(), true, false, null));
        drinks.add(new Drink("Vodka Cranberry", new String[]{"Vodka", "Cranberry Juice"}, new double[]{0.25, 0.75}, Glass.getHighball(), true, false, null));
        drinks.add(new Drink("Bermuda Triangle", new String[]{"Rum", "Cranberry Juice", "Orange Juice"}, new double[]{0.25, 0.375, 0.375}, Glass.getHighball(), true, false, null));
        drinks.add(new Drink("Gin and Cranberry", new String[]{"Gin", "Cranberry Juice"}, new double[]{0.25, 0.75}, Glass.getHighball(), true, false, null));
        drinks.add(new Drink("Ginny Cosmo", new String[]{"Gin", "Triple Sec", "Cranberry Juice", "Lemon Juice"}, new double[]{0.17, 0.17, 0.49, 0.17}, Glass.getMartini(), false, true, null));
        drinks.add(new Drink("Sunshine Fizz", new String[]{"Orange Juice", "Cranberry Juice", "Soda Water"}, new double[]{0.33, 0.33, 0.33}, Glass.getHighball(), true, false, null));
        drinks.add(new Drink("Cranberry Juice", new String[]{"Cranberry Juice"}, new double[]{1.0}, Glass.getHighball(), true, false, null));
        drinks.add(new Drink("Cranberry Soda", new String[]{"Cranberry Juice", "Soda Water"}, new double[]{0.33, 0.33, 0.33}, Glass.getHighball(), true, false, null));
        drinks.add(new Drink("Orange Cranberry", new String[]{"Cranberry Juice", "Orange Juice"}, new double[]{0.5, 0.5}, Glass.getHighball(), true, false, null));
    }
    
    public static boolean isAlcohol() {
        return false;
    }
}

