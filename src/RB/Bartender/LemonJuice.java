package RB.Bartender;

import java.io.Serializable;
import java.util.ArrayList;

/**
 *
 * @authors Anthony Spiteri
 *          Cristian Nuosci
 *          Shahezad Kassam
 */

public class LemonJuice extends Ingredient implements Serializable, Cloneable {
    private static ArrayList<LemonJuice> brands = new ArrayList<>();
    private static ArrayList<Drink> drinks = new ArrayList<>();
    
    public LemonJuice(String name, double flowRate, int abv, String type) {
        super(name, flowRate, abv, type);
    }
    
    public static ArrayList<LemonJuice> getBrands() throws CloneNotSupportedException {
        ArrayList<LemonJuice> clone = new ArrayList<>();
        for(int i = 0; i < brands.size(); i++) {
            clone.add((LemonJuice) brands.get(i).clone());
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
        brands.add(new LemonJuice("Lemon Juice", 1, 0, "Lemon Juice"));
    }

    public static void initDrinks() {
        drinks.add(new Drink("Bloody Nightmare", new String[]{"Vodka", "Lemon Juice", "Cranberry Juice"}, new double[]{0.17, 0.33, 0.5}, Glass.getHighball(), true, false, null));
        drinks.add(new Drink("Cosmopolitan", new String[]{"Vodka", "Triple Sec", "Cranberry Juice", "Lemon Juice"}, new double[]{0.17, 0.17, 0.49, 0.17}, Glass.getMartini(), false, true, null));
        drinks.add(new Drink("Huntsman", new String[]{"Vodka", "Rum", "Lemon Juice", "Simple Syrup"}, new double[]{0.5, 0.17, 0.23, 0.1}, Glass.getMartini(), false, true, null));
        drinks.add(new Drink("Kamikaze", new String[]{"Vodka", "Triple Sec", "Lemon Juice"}, new double[]{0.7, 0.15, 0.15}, Glass.getShot(), false, true, null));
        drinks.add(new Drink("Lemon Drop Martini", new String[]{"Vodka", "Triple Sec", "Lemon Juice"}, new double[]{0.45, 0.45, 0.1}, Glass.getMartini(), false, true, null));
        drinks.add(new Drink("Lemon Drop Shot", new String[]{"Vodka", "Lemon Juice", "Simple Syrup"}, new double[]{0.8, 0.1, 0.1}, Glass.getShot(), false, true, null));
        drinks.add(new Drink("Vodka Collins", new String[]{"Vodka", "Lemon Juice", "Simple Syrup", "Soda Water"}, new double[]{0.25, 0.15, 0.1 , 0.5}, Glass.getHighball(), true, true, null));
        drinks.add(new Drink("Daiquiri", new String[]{"Rum", "Simple Syrup", "Lemon Juice"}, new double[]{0.5, 0.17, 0.33}, Glass.getMartini(), false, true, null));
        drinks.add(new Drink("Little Devil", new String[]{"Rum", "Gin", "Triple Sec", "Lemon Juice"}, new double[]{0.33, 0.33, 0.17, 0.17}, Glass.getMartini(), false, true, null));
        drinks.add(new Drink("Ti-Punch", new String[]{"Rum", "Simple Syrup", "Lemon Juice"}, new double[]{0.55, 0.3, 0.15}, Glass.getRock(), true, false, null));
        drinks.add(new Drink("Chelsea Sidecar", new String[]{"Gin", "Triple Sec", "Lemon Juice"}, new double[]{0.375, 0.375, 0.25}, Glass.getMartini(), false, true, null));
        drinks.add(new Drink("Gimlet", new String[]{"Gin", "Lemon Juice", "Simple Syrup"}, new double[]{0.56, 0.22, 0.22}, Glass.getMartini(), false, true, null));
        drinks.add(new Drink("Ginny Cosmo", new String[]{"Gin", "Triple Sec", "Cranberry Juice", "Lemon Juice"}, new double[]{0.17, 0.17, 0.49, 0.17}, Glass.getMartini(), false, true, null));
        drinks.add(new Drink("Singapore Sling", new String[]{"Gin", "Orange Juice", "Lemon Juice", "Soda Water"}, new double[]{0.2, 0.4, 0.1, 0.3}, Glass.getHighball(), true, false, null));
        drinks.add(new Drink("Tom Collins", new String[]{"Gin", "Lemon Juice", "Simple Syrup", "Soda Water"}, new double[]{0.25, 0.15, 0.1, 0.5}, Glass.getHighball(), true, true, null));
        drinks.add(new Drink("Madras", new String[]{"Vodka", "Cranberry Juice", "Orange Juice", "Lemon Juice"}, new double[]{0.17, 0.56, 0.17, 0.1}, Glass.getHighball(), true, false, null));
    }
        
    public static boolean isAlcohol() {
        return false;
    }
}
