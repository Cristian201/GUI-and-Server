package RB.Bartender;

import java.io.Serializable;
import java.time.LocalDate;
import java.time.LocalTime;
import java.time.Period;
import java.util.ArrayList;

/**
 *
 * @authors Anthony Spiteri
 *          Cristian Nuosci
 *          Shahezad Kassam
 */

public class Customer extends Account implements Serializable {
    private final LocalDate birthday;
    private final String gender;
    private long firstDrinkTime;
    private double gramsOfAlcohol;
    private double bac;
    private boolean lockedOut;
    private double balance;
    private int numOfWeakDrinks;
    private int numOfMediumDrinks;
    private int numOfStrongDrinks;
    private ArrayList<Drink> lastOrder = new ArrayList<>();
    private ArrayList<Drink> cart = new ArrayList<>();
    
    public Customer(String tagNumber, String firstName, String lastName, LocalDate birthday, String gender) {
        super(tagNumber, firstName, lastName);
        this.birthday = birthday;
        this.gender = gender;
        this.firstDrinkTime = 0;
        this.gramsOfAlcohol = 0;
        this.bac = 0;
        this.lockedOut = false;
        this.balance = 0;
        this.numOfWeakDrinks = 0;
        this.numOfMediumDrinks = 0;
        this.numOfStrongDrinks = 0;
    }
    
    public LocalDate getBirthday() {
        return birthday;
    }
    
    public String getGender() {
        return gender;
    }
    
    public boolean getLockedOut() {
        return lockedOut;
    }
    
    public long getFirstDrinkTime() {
        return firstDrinkTime;
    }
    
    public void setFirstDrinkTime(long firstDrinkTime) {
        this.firstDrinkTime = firstDrinkTime;
    }
    
    public void setLockedOut(boolean lockedOut) {
        this.lockedOut = lockedOut;
    }
    
    public double getGramsOfAlcohol() {
        return gramsOfAlcohol;
    }
    
    public void setGramsOfAlcohol(double gramsOfAlcohol) {
        this.gramsOfAlcohol = gramsOfAlcohol;
    }
    
    public double getBAC() {
        return bac;
    }
    
    public void setBAC(double bac) {
        this.bac = bac;
    }
    
    public double getBalance() {
        return balance;
    }
    
    public void setBalance(double balance) {
        this.balance = balance;
    }
    
    public int getNumOfWeakDrinks() {
        return numOfWeakDrinks;
    }
    
    public void setNumOfWeakDrinks(int numOfWeakDrinks) {
        this.numOfWeakDrinks = numOfWeakDrinks;
    }
    
    public int getNumOfMediumDrinks() {
        return numOfMediumDrinks;
    }
    
    public void setNumOfMediumDrinks(int numOfMediumDrinks) {
        this.numOfMediumDrinks = numOfMediumDrinks;
    }
    
    public int getNumOfStrongDrinks() {
        return numOfStrongDrinks;
    }
    
    public void setNumOfStrongDrinks(int numOfStrongDrinks) {
        this.numOfStrongDrinks = numOfStrongDrinks;
    }    
    
    public ArrayList<Drink> getLastOrder() {
        return lastOrder;
    }

    public ArrayList<Drink> getCart() {
        return cart;
    }
    
    public double[] calculateBAC(Drink drink) {
        double grams = 0;
        for(int i = 0; i < drink.getIngredient().length; i++) {
            // calcuates the grams of alcohol in the drink
            grams = grams + ((drink.getAmount()[i]*drink.getGlass().getVolume()*29.5735)*(drink.getIngredient()[i].getABV() / 100.0)*0.79);
        
            //System.out.println("grams: " + grams);
            //System.out.println(drink.getGlass().getVolume());
            //System.out.println(drink.getAmount()[i]);
            //System.out.println(drink.getIngredient()[i].getABV());
            
            //System.out.println((drink.getAmount()[i] * drink.getGlass().getVolume()));
            //System.out.println((drink.getIngredient()[i].getABV() / 100.0));
            //System.out.println(((drink.getAmount()[i] * drink.getGlass().getVolume()) * (drink.getIngredient()[i].getABV() / 100) * 0.79));
        }
        
        grams = grams + getGramsOfAlcohol();

        double bac = (grams*100.0)/(getWidmarkFactor()*getWeight()) - (0.015*((System.currentTimeMillis() - getFirstDrinkTime())*(0.001/3600.0)));
        //return (grams*100)/(getWidmarkFactor()*getWeight()) - (0.015*((System.currentTimeMillis()+3600000 - getFirstDrinkTime())*(0.001/3600)));  // after one hour from last drink
        return new double[]{bac, grams};
    }
 
    public int getAge() {
        return Period.between(getBirthday(), LocalDate.now()).getYears();
    }
        
    public double getWidmarkFactor() {
        double widmarkFactor = 0;
        if(getGender().equals("Male")) {
            if(18 <= getAge() && getAge() <= 39) {
                widmarkFactor = 1.0178 - 0.012127*21.7;
            }
            else if(40 <= getAge() && getAge() <= 59) {
                widmarkFactor = 1.0178 - 0.012127*27.45;
            }
            else if(60 <= getAge() && getAge() <= 79) {
                widmarkFactor = 1.0178 - 0.012127*27.45;
            }
        }
        else if(getGender().equals("Female")) {
            if(18 <= getAge() && getAge() <= 39) {
                widmarkFactor = 0.8736 - 0.0124*21.7;
            }
            else if(40 <= getAge() && getAge() <= 59) {
                widmarkFactor = 0.8736 - 0.0124*27.45;
            }
            else if(60 <= getAge() && getAge() <= 79) {
                widmarkFactor = 0.8736 - 0.0124*27.45;
            }
        }
        //System.out.println("widmark: " + widmarkFactor);
        return widmarkFactor;
    }

    public double getWeight() {
        double weight = 0;
        if(getGender().equals("Male")) {
            if(18 <= getAge() && getAge() <= 39) {
                weight = (1.751*1.751)*21.7;
            }
            else if(40 <= getAge() && getAge() <= 59) {
                weight = (1.751*1.751)*27.45;
            }
            else if(60 <= getAge() && getAge() <= 79) {
                weight = (1.751*1.751)*27.45;
            }
        }
        else if(getGender().equals("Female")) {
            if(18 <= getAge() && getAge() <= 39) {
                weight = (1.623*1.623)*21.7;
            }
            else if(40 <= getAge() && getAge() <= 59) {
                weight = (1.623*1.623)*27.45;
            }
            else if(60 <= getAge() && getAge() <= 79) {
                weight = (1.623*1.623)*27.45;
            }
        }
        //System.out.println("weight: " + weight*1000);
        return weight*1000; // returns weight in grams
    }
}
