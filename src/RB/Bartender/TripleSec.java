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

public class TripleSec extends Ingredient implements Serializable, Cloneable {
    private static ArrayList<TripleSec> brands = new ArrayList<>();
    private static ArrayList<Drink> drinks = new ArrayList<>();
    
    public TripleSec(String name, double flowRate, int abv, String type) {
        super(name, flowRate, abv, type);
    }
    
    public static ArrayList<TripleSec> getBrands() throws CloneNotSupportedException {
        ArrayList<TripleSec> clone = new ArrayList<>();
        for(int i = 0; i < brands.size(); i++) {
            clone.add((TripleSec) brands.get(i).clone());
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
        brands.add(new TripleSec("Meguinness Triple Sec", 1, 22, "Triple Sec"));
        brands.add(new TripleSec("Meaghers Triple Sec", 1, 35, "Triple Sec"));
        brands.add(new TripleSec("Coinreau", 1, 40, "Triple Sec"));
        brands.add(new TripleSec("Grand Marnier", 1, 40, "Triple Sec"));
        Collections.sort(brands);
    }

    public static void initDrinks() {
        drinks.add(new Drink("Cosmopolitan", new String[]{"Vodka", "Triple Sec", "Cranberry Juice", "Lemon Juice"}, new double[]{0.17, 0.17, 0.49, 0.17}, Glass.getMartini(), false, true, null));
        drinks.add(new Drink("Kamikaze", new String[]{"Vodka", "Triple Sec", "Lemon Juice"}, new double[]{0.7, 0.15, 0.15}, Glass.getShot(), false, true, null));
        drinks.add(new Drink("Lemon Drop Martini", new String[]{"Vodka", "Triple Sec", "Lemon Juice"}, new double[]{0.45, 0.45, 0.1}, Glass.getMartini(), false, true, null));
        drinks.add(new Drink("Orange Crush", new String[]{"Vodka", "Triple Sec", "Orange Juice"}, new double[]{0.25, 0.17, 0.58}, Glass.getHighball(), true, false, null));
        drinks.add(new Drink("Little Devil", new String[]{"Rum", "Gin", "Triple Sec" , "Lemon Juice"}, new double[]{0.33, 0.33, 0.17, 0.17}, Glass.getMartini(), false, true, null));
        drinks.add(new Drink("Chelsea Sidecar", new String[]{"Gin", "Triple Sec", "Lemon Juice"}, new double[]{0.375, 0.375, 0.25}, Glass.getMartini(), false, true, null));
        drinks.add(new Drink("Flying Dutchman", new String[]{"Gin", "Triple Sec"}, new double[]{0.78, 0.22}, Glass.getRock(), true, false, null));
        drinks.add(new Drink("Ginny Cosmo", new String[]{"Gin", "Triple Sec", "Cranberry Juice", "Lemon Juice"}, new double[]{0.17, 0.17, 0.49, 0.17}, Glass.getMartini(), false, true, null));
        drinks.add(new Drink("Triple Sec Neat", new String[]{"Triple Sec"}, new double[]{0.375}, Glass.getRock(), false, false, null));
        drinks.add(new Drink("Triple Sec On The Rocks", new String[]{"Triple Sec"}, new double[]{0.375}, Glass.getRock(), true, false, null));
        drinks.add(new Drink("Triple Sec Shot", new String[]{"Triple Sec"}, new double[]{1.0}, Glass.getShot(), false, true, null));
    }
        
    public static boolean isAlcohol() {
        return true;
    }
}