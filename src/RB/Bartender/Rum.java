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

public class Rum extends Ingredient implements Serializable, Cloneable {
    private static ArrayList<Rum> brands = new ArrayList<>();
    private static ArrayList<Drink> drinks = new ArrayList<>();
    
    public Rum(String name, double flowRate, int abv, String type) {
        super(name, flowRate, abv, type);
    }
    
    public static ArrayList<Rum> getBrands() throws CloneNotSupportedException {
        ArrayList<Rum> clone = new ArrayList<>();
        for(int i = 0; i < brands.size(); i++) {
            clone.add((Rum) brands.get(i).clone());
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
        brands.add(new Rum("Bacardi Superior Rum", 1, 40, "Rum"));
        brands.add(new Rum("Lamb's White Rum", 1, 40, "Rum"));
        brands.add(new Rum("Havana Club 3 Anos", 1, 40, "Rum"));
        brands.add(new Rum("Captain Morgan White Rum", 1, 40, "Rum"));
        brands.add(new Rum("Bacardi Gold Rum", 1, 40, "Rum"));
        brands.add(new Rum("Appleton Estate Reserve Blend", 1, 40, "Rum"));
        brands.add(new Rum("Captain Morgan Gold Rum", 1, 40, "Rum"));
        brands.add(new Rum("Captian Morgan Dark Rum", 1, 40, "Rum"));
        brands.add(new Rum("The Kraken Black Spiced Rum", 1, 40, "Rum"));  
        Collections.sort(brands);
    }

    public static void initDrinks() {
        drinks.add(new Drink("Brass Monkey", new String[]{"Vodka", "Rum", "Orange Juice"}, new double[]{0.1, 0.1, 0.8}, Glass.getHighball(), true, false, null));
        drinks.add(new Drink("Huntsman", new String[]{"Vodka", "Rum", "Lemon Juice", "Simple Syrup"}, new double[]{0.5, 0.17, 0.23, 0.1}, Glass.getMartini(), false, true, null));
        drinks.add(new Drink("Bermuda Triangle", new String[]{"Rum", "Cranberry Juice", "Orange Juice"}, new double[]{0.25, 0.375, 0.375}, Glass.getHighball(), true, false, null));
        drinks.add(new Drink("Daiquiri", new String[]{"Rum", "Simple Syrup", "Lemon Juice"}, new double[]{0.5, 0.17, 0.33}, Glass.getMartini(), false, true, null));
        drinks.add(new Drink("Little Devil", new String[]{"Rum", "Gin", "Triple Sec" , "Lemon Juice"}, new double[]{0.33, 0.33, 0.17, 0.17}, Glass.getMartini(), false, true, null));
        drinks.add(new Drink("Rum Neat", new String[]{"Rum"}, new double[]{0.375}, Glass.getRock(), false, false, null));
        drinks.add(new Drink("Rum On The Rocks", new String[]{"Rum"}, new double[]{0.375}, Glass.getRock(), true, false, null));
        drinks.add(new Drink("Rum Shot", new String[]{"Rum"}, new double[]{1.0}, Glass.getShot(), false, true, null));
        drinks.add(new Drink("Ti-Punch", new String[]{"Rum", "Simple Syrup", "Lemon Juice"}, new double[]{0.55, 0.3, 0.15}, Glass.getRock(), true, false, null));
    }
        
    public static boolean isAlcohol() {
        return true;
    }
}

    
