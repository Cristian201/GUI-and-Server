package RB.Bartender;

import java.io.Serializable;
import java.util.ArrayList;

/**
 *
 * @authors Anthony Spiteri
 *          Cristian Nuosci
 *          Shahezad Kassam
 */

public class SimpleSyrup extends Ingredient implements Serializable, Cloneable {
    private static ArrayList<SimpleSyrup> brands = new ArrayList<>();
    private static ArrayList<Drink> drinks = new ArrayList<>();
    
    public SimpleSyrup(String name, double flowRate, int abv, String type) {
        super(name, flowRate, abv, type);
    }
    
    public static ArrayList<SimpleSyrup> getBrands() throws CloneNotSupportedException {
        ArrayList<SimpleSyrup> clone = new ArrayList<>();
        for(int i = 0; i < brands.size(); i++) {
            clone.add((SimpleSyrup) brands.get(i).clone());
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
        brands.add(new SimpleSyrup("Simple Syrup", 1, 0, "Simple Syrup"));
    }
    
    public static void initDrinks() {
        drinks.add(new Drink("Huntsman", new String[]{"Vodka", "Rum", "Lemon Juice", "Simple Syrup"}, new double[]{0.5, 0.17, 0.23, 0.1}, Glass.getMartini(), false, true, null));
        drinks.add(new Drink("Lemon Drop Shot", new String[]{"Vodka", "Lemon Juice", "Simple Syrup"}, new double[]{0.8, 0.1, 0.1}, Glass.getShot(), false, true, null));
        drinks.add(new Drink("Vodka Collins", new String[]{"Vodka", "Lemon Juice", "Simple Syrup", "Soda Water"}, new double[]{0.25, 0.15, 0.1 , 0.5}, Glass.getHighball(), true, true, null));
        drinks.add(new Drink("Daiquiri", new String[]{"Rum", "Simple Syrup", "Lemon Juice"}, new double[]{0.5, 0.17, 0.33}, Glass.getMartini(), false, true, null));
        drinks.add(new Drink("Ti-Punch", new String[]{"Rum", "Simple Syrup", "Lemon Juice"}, new double[]{0.55, 0.3, 0.15}, Glass.getRock(), true, false, null));
        drinks.add(new Drink("Gimlet", new String[]{"Gin", "Lemon Juice", "Simple Syrup"}, new double[]{0.56, 0.22, 0.22}, Glass.getMartini(), false, true, null));
        drinks.add(new Drink("Tom Collins", new String[]{"Gin", "Lemon Juice", "Simple Syrup", "Soda Water"}, new double[]{0.25, 0.15, 0.1, 0.5}, Glass.getHighball(), true, true, null));
    }
        
    public static boolean isAlcohol() {
        return false;
    }
}
