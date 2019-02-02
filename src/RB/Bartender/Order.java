package RB.Bartender;

import java.io.Serializable;
import java.util.ArrayList;

/**
 *
 * @authors Anthony Spiteri
 *          Cristian Nuosci
 *          Shahezad Kassam
 */

public class Order implements Serializable {
    private int number;
    private ArrayList<Drink> drinks;
    private Customer customer;
    
    public Order(int number, ArrayList<Drink> drinks, Customer customer) {
        this.number = number;
        this.drinks = drinks;
        this.customer = customer;
    }
    
    public int getNumber() {
        return number;
    }
    
    public ArrayList<Drink> getDrinks() {
        return drinks;
    }
    
    public Customer getCustomer() {
        return customer;
    }
}