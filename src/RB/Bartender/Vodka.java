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

public class Vodka extends Ingredient implements Serializable, Cloneable {
    private static ArrayList<Vodka> brands = new ArrayList<>();
    private static ArrayList<Drink> drinks = new ArrayList<>();
    
    public Vodka(String name, double flowRate, int abv, String type) {
        super(name, flowRate, abv, type);
    }
    
    public static ArrayList<Vodka> getBrands() throws CloneNotSupportedException {
        ArrayList<Vodka> clone = new ArrayList<>();
        for(int i = 0; i < brands.size(); i++) {
            clone.add((Vodka) brands.get(i).clone());
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
        brands.add(new Vodka("Smirnoff Vodka", 1, 40, "Vodka"));
        brands.add(new Vodka("Alberta Pure Vodka", 1, 40, "Vodka"));
        brands.add(new Vodka("Russian Prince Vodka", 1, 40, "Vodka"));
        brands.add(new Vodka("Iceberg Vodka", 1, 40, "Vodka"));
        brands.add(new Vodka("Polar Ice Vodka", 1, 40, "Vodka"));
        brands.add(new Vodka("Ciroc", 1, 40, "Vodka"));
        brands.add(new Vodka("Grey Goose Vodka", 1, 40, "Vodka"));
        brands.add(new Vodka("Belvedere Vodka", 1, 40, "Vodka"));
        brands.add(new Vodka("Ketel One Vodka", 1, 40, "Vodka"));
        Collections.sort(brands);
    }

    public static void initDrinks() {
        drinks.add(new Drink("Bloody Nightmare", new String[]{"Vodka", "Lemon Juice", "Cranberry Juice"}, new double[]{0.17, 0.33, 0.5}, Glass.getHighball(), true, false, null));
        drinks.add(new Drink("Brass Monkey", new String[]{"Vodka", "Rum", "Orange Juice"}, new double[]{0.1, 0.1, 0.8}, Glass.getHighball(), true, false, null));
        drinks.add(new Drink("Cape Cod", new String[]{"Vodka", "Cranberry Juice", "Soda Water"}, new double[]{0.17, 0.7, 0.13}, Glass.getHighball(), true, false, null));
        drinks.add(new Drink("Cosmopolitan", new String[]{"Vodka", "Triple Sec", "Cranberry Juice", "Lemon Juice"}, new double[]{0.17, 0.17, 0.49, 0.17}, Glass.getMartini(), false, true, null));
        drinks.add(new Drink("Hawaiian Sunset", new String[]{"Vodka", "Cranberry Juice", "Orange Juice", "Soda Water"}, new double[]{0.17, 0.33, 0.33, 0.17}, Glass.getHighball(), true, true, null));
        drinks.add(new Drink("Huntsman", new String[]{"Vodka", "Rum", "Lemon Juice", "Simple Syrup"}, new double[]{0.5, 0.17, 0.23, 0.1}, Glass.getMartini(), false, true, null));
        drinks.add(new Drink("Kamikaze", new String[]{"Vodka", "Triple Sec", "Lemon Juice"}, new double[]{0.7, 0.15, 0.15}, Glass.getShot(), false, true, null));
        drinks.add(new Drink("Lemon Drop Martini", new String[]{"Vodka", "Triple Sec", "Lemon Juice"}, new double[]{0.45, 0.45, 0.1}, Glass.getMartini(), false, true, null));
        drinks.add(new Drink("Lemon Drop Shot", new String[]{"Vodka", "Lemon Juice", "Simple Syrup"}, new double[]{0.8, 0.1, 0.1}, Glass.getShot(), false, true, null));
        drinks.add(new Drink("Madras", new String[]{"Vodka", "Cranberry Juice", "Orange Juice", "Lemon Juice"}, new double[]{0.17, 0.56, 0.17, 0.1}, Glass.getHighball(), true, false, null));
        drinks.add(new Drink("Orange Crush", new String[]{"Vodka", "Triple Sec", "Orange Juice"}, new double[]{0.25, 0.17, 0.58}, Glass.getHighball(), true, false, null));
        drinks.add(new Drink("Screwdriver", new String[]{"Vodka", "Orange Juice"}, new double[]{0.25, 0.75}, Glass.getHighball(), true, false, null));
        drinks.add(new Drink("Sparking Vodka Cranberry", new String[]{"Vodka", "Cranberry Juice", "Soda Water"}, new double[]{0.25, 0.55, 0.2}, Glass.getHighball(), true, false, null));
        drinks.add(new Drink("Vodka Collins", new String[]{"Vodka", "Lemon Juice", "Simple Syrup", "Soda Water"}, new double[]{0.25, 0.15, 0.1 , 0.5}, Glass.getHighball(), true, true, null));
        drinks.add(new Drink("Vodka Cranberry", new String[]{"Vodka", "Cranberry Juice"}, new double[]{0.25, 0.75}, Glass.getHighball(), true, false, null));
        drinks.add(new Drink("Vodka Neat", new String[]{"Vodka"}, new double[]{0.375}, Glass.getRock(), false, false, null));
        drinks.add(new Drink("Vodka On The Rocks", new String[]{"Vodka"}, new double[]{0.375}, Glass.getRock(), true, false, null));
        drinks.add(new Drink("Vodka Shot", new String[]{"Vodka"}, new double[]{1.0}, Glass.getShot(), false, true, null));
    }
}