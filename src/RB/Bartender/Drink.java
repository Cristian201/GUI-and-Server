package RB.Bartender;

import java.io.File;
import java.io.Serializable;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;

/**
 *
 * @authors Anthony Spiteri
 *          Cristian Nuosci
 *          Shahezad Kassam
 */

public class Drink implements Comparable<Drink>, Serializable, Cloneable {
    private final String name;
    private String[] ingredName;
    private Ingredient[] ingredient;
    private double[] amount;
    private Glass glass;
    private boolean ice;
    private boolean shake;
    private Double price;
    private double spiritAmount;
    private int count;
    
    // for when initalizing the drinks
    public Drink(String name, String[] ingredName, double[] amount, Glass glass, boolean ice, boolean shake, Double price) {
        this.name = name;
        this.ingredName = ingredName;
        this.amount = amount;
        this.glass = glass;
        this.ice = ice;
        this.shake = shake;
        this.price = price;
    }
    
    // for when adding drinks to drinkMenu (with ingredients)
    public Drink(String name, Ingredient[] ingredient, double[] amount, Glass glass, boolean ice, boolean shake, Double price, double spiritAmount) {
        this.name = name;
        this.ingredient = ingredient;
        this.amount = amount;
        this.glass = glass;
        this.ice = ice;
        this.shake = shake;
        this.price = price;
        this.spiritAmount = spiritAmount;
    }
    
    // to determine popularity of the drinks
    public Drink(String name) {
        this.name = name;
        this.count = 0;
    }
    
    @Override
    public Object clone() throws CloneNotSupportedException{
        return super.clone();
    }
    
    @Override
    public int compareTo(Drink drink) {
        return this.name.compareTo(drink.name);
    }
    
    public String getName() {
        return name;
    }
    
    public String[] getIngredName() {
        return ingredName;
    }
    
    public Ingredient[] getIngredient() {
        return ingredient;
    }
    
    public double[] getAmount() {
        return amount;
    }

    public double getSpiritAmount() {
        return spiritAmount;
    }
    
    public Double getPrice() {
        return price;
    }
        
    public void setPrice(double price) {
        this.price = price;
    }
        
    public Glass getGlass() {
        return glass;
    }
    
    public boolean getIce() {
        return ice;
    }

    public void setIce(boolean ice) {
        this.ice = ice;
    }
        
    public boolean getShake() {
        return shake;
    }

    public void setShake(boolean shake) {
        this.shake = shake;
    }
    
    public int getCount() {
        return count;
    }
    
    public void setCount(int count) {
        this.count = count;
    }
    
    public String getType() {
        if(getShake() == true && getGlass().getName().equals("Highball")) {
            return "Cocktail";
        }
        
        else if(getShake() == false && getGlass().getName().equals("Highball")) {
            return "Mixed Drink";
        }
        
        else if(getIngredient().length == 1 && getGlass().getName().equals("Rock") && getIce() == true) {
            return "On The Rocks";
        }
        
        else if(getIngredient().length == 1 && getGlass().getName().equals("Rock") && getIce() == false) {
            return "Neat";
        }

        else if(getGlass().getName().equals("Martini")) {
            return "Martini";
        }
        
        else if(getGlass().getName().equals("Shot")) {
            return "Shot";
        }
        
        else if(getGlass().getName().equals("Rock") && getShake() == false) {
            return "Cocktail";
        }
        
        else {
            return "Mixed Drink";
        }
    }

    public String getStrength() {
        if(getGlass().getName().equals("Highball")) {
            if(0 <= getSpiritAmount() && getSpiritAmount() <= 1.5) {
                return "Weak";
            }
            
            else if(1.5 < getSpiritAmount() && getSpiritAmount() <= 2.5) {
                return "Medium";
            }
            
            else if(2.5 < getSpiritAmount()) {
                return "Strong";
            }
        }
        
        else if(getGlass().getName().equals("Martini")) {
            if(0 <= getSpiritAmount() && getSpiritAmount() <= 1) {
                return "Weak";
            }
            
            else if(1 < getSpiritAmount() && getSpiritAmount() <= 1.8) {
                return "Medium";
            }
            
            else if(1.8 < getSpiritAmount()) {
                return "Strong";
            }
        }
        return "Strong";
    }
    
    public Image getImage() {
        File file = new File("DrinkImages/" + getName().replace(" ", "").toLowerCase() + ".jpeg");
        return new Image(file.toURI().toString());
    }
    
    public static String boolToString(boolean result) {
        if(result == true) {
            return "Yes";
        }
        else {
            return "No";
        }
    }
}