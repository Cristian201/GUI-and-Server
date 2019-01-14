package RB.Bartender;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.Serializable;
import static java.lang.System.out;
import java.net.Socket;
import java.time.LocalDate;
import java.time.Period;
import java.util.ArrayList;
import java.util.TreeSet;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @authors Anthony Spiteri
 *          Cristian Nuosci
 *          Shahezad Kassam
 */

public class Kiosk implements Runnable, Serializable {
    private Socket socket;
    private ObjectOutputStream output;
    private static ObjectInputStream input;
    private Customer customer;
    private Manager manager;
    private ArrayList<Drink> drinkMenu = new ArrayList<>();
    private final int kioskNumber = 1;
    private boolean receiveMessage = true;
    public Thread updateThread;
    
    public Kiosk(Socket socket) throws IOException {
        this.socket = socket;
        this.output = new ObjectOutputStream(getSocket().getOutputStream());
        this.input = new ObjectInputStream(getSocket().getInputStream());
    }

    @Override
    public void run() {
        try {
            System.out.println("Kiosk Connected");

            sendObject("Kiosk");    // notifies the server that a kiosk is connected
            //sendObject(Thread.currentThread());
            
            
            //createThread();
            
            /*
            sendObject("1");
            System.out.println(receiveObject());
            sendObject(new Object[] {"1", "John", "Smith"});
            System.out.println(receiveObject());
        */
            login("1");

        
        } catch (IOException ex) {
            Logger.getLogger(Kiosk.class.getName()).log(Level.SEVERE, null, ex);
        } catch (ClassNotFoundException ex) {
            Logger.getLogger(Kiosk.class.getName()).log(Level.SEVERE, null, ex);
        }

    }

    public void managerFunctions() throws IOException, ClassNotFoundException {
        
        sendObject("Sign Up");  // notifies the server that user wants to create a new account
            
        sendObject(new Object[] {"2", "Anthony", "Spiteri", LocalDate.of(1997, 9, 11), "Male"});
        System.out.println(receiveObject());
        
        login("2");
        
    }
    
    public void login(String tagNumber) throws IOException, ClassNotFoundException {
        
        //updateThread.interrupt();
        
        
        //receiveObject();
        sendObject("Login");
        sendObject(tagNumber);    // tag is scanned and the tag number is sent to verify
        String type = (String)receiveObject();
        //System.out.println("");
      
        switch (type) {
            case "Customer":
                setCustomer((Customer)receiveObject());
                
                sendObject("getDrinkMenu");
                //TreeSet<Drink> temp = (TreeSet<Drink>)receiveObject();
                //ArrayList<Drink> drinks = new ArrayList<>(temp);
                setDrinkMenu((TreeSet<Drink>)receiveObject());
                System.out.println(getDrinkMenu().size());

                sendObject("filterDrinkMenu");
                sendObject(new String[] {"Price", "High to Low"});
                setDrinkMenu((ArrayList<Drink>)receiveObject());
                for(int i = 0; i < getDrinkMenu().size(); i++)
                    System.out.println(getDrinkMenu().get(i).getName()+"  "+getDrinkMenu().get(i).getPrice());
                
                sendObject("filterDrinkMenu");
                sendObject(new String[] {"Price", "Low to High"});
                setDrinkMenu((ArrayList<Drink>)receiveObject());
                for(int i = 0; i < getDrinkMenu().size(); i++)
                    System.out.println(getDrinkMenu().get(i).getName()+"  "+getDrinkMenu().get(i).getPrice());
                
                //System.out.println(getDrinkMenu().size());
                System.out.println(addToCart(getDrinkMenu().get(0)));
                System.out.println(addToCart(getDrinkMenu().get(1)));
                
                placeOrder();
                
                logout();
                
                break;
            case "Manager":
                setManager((Manager)receiveObject());
                
                sendObject("registerTags");
                sendObject("2");
                System.out.println(receiveObject());
                
                sendObject("addBottle");
                ArrayList<String> ingred = (ArrayList<String>)receiveObject();
                for(int i = 0; i < ingred.size(); i++)
                    System.out.println(ingred.get(i));
                sendObject("Vodka");
                ArrayList<Ingredient> brands;
                brands = (ArrayList<Ingredient>)receiveObject();
                for(int i = 0; i < brands.size(); i++)
                    System.out.println(brands.get(i).getName());
                sendObject(new Object[] {brands.get(0), 20, 1});
                System.out.println(receiveObject());
                brands.clear();
                ingred.clear();
                
                sendObject("addBottle");
                ArrayList<String> ingred1 = (ArrayList<String>)receiveObject();
                for(int i = 0; i < ingred1.size(); i++)
                    System.out.println(ingred1.get(i));
                sendObject("Cranberry Juice");
                ArrayList<Ingredient> brands1 = (ArrayList<Ingredient>)receiveObject();
                for(int i = 0; i < brands1.size(); i++)
                    System.out.println(brands1.get(i).getName());
                sendObject(new Object[] {brands1.get(0), 20, 2});
                System.out.println(receiveObject());
                brands.clear();
                ingred.clear();
                
                logout();
                
                managerFunctions();
                
                break;
            
            default:
                System.out.println("Invalid Tag");
                break;
        
        }
    }
    
    public String placeOrder() throws IOException, ClassNotFoundException {
        
        System.out.println(((System.currentTimeMillis() - getCustomer().getFirstDrinkTime())*(0.001/3600))%24);
        if(!getCustomer().getCart().isEmpty()) {
            // if the firstDrinkTime is zero or if it has been 6 hours since their last drink
            if(getCustomer().getFirstDrinkTime() == 0 || ((System.currentTimeMillis() - getCustomer().getFirstDrinkTime())*(0.001/3600)) % 24 > 6) { 
                getCustomer().setFirstDrinkTime(System.currentTimeMillis());
                getCustomer().setGramsOfAlcohol(0);
                getCustomer().setBAC(0);
                getCustomer().setNumOfWeakDrinks(0);
                getCustomer().setNumOfMediumDrinks(0);
                getCustomer().setNumOfStrongDrinks(0);
            }
            
            double bac = 0;
            double grams = 0;
            double balance = 0;
            int numOfWeakDrinks = 0;
            int numOfMediumDrinks = 0;
            int numOfStrongDrinks = 0;
            
            System.out.println(getCustomer().getCart().size());
            
            for(int i = 0; i < getCustomer().getCart().size(); i++) {
                double[] info = getCustomer().calculateBAC(getCustomer().getCart().get(i));
                bac = bac + info[0];
                grams = grams + info[1];
                balance = balance + getCustomer().getCart().get(i).getPrice();
 
                System.out.println(getCustomer().getCart().get(i).getPrice() + "    " + getCustomer().getCart().get(i).getName());
                
                switch (getCustomer().getCart().get(i).getStrength()) {
                    case "Weak":
                        numOfWeakDrinks++;
                        break;
                    case "Medium":
                        numOfMediumDrinks++;
                        break;
                    case "Strong":
                        numOfStrongDrinks++;
                        break;
                    default:
                        break;
                }
            }
            
            System.out.println("bac: " + bac);
            System.out.println("balance: " + balance);
            
            if(bac >= Server.getCutoffLimit()) {
                notifyManager(bac);
                getCustomer().setLockedOut(true);
                System.out.println("We Believe That You Are Over Intoxicated\nIf there is an Error, Please Notify a Manager");
            }
            else if(bac >= Server.getWarningLimit()) {
                notifyManager(bac);
                sendObject("placeOrder");
                sendObject(new Object[] {getCustomer().getCart(), getCustomer()});
                System.out.println(receiveObject());
                getCustomer().setGramsOfAlcohol(grams);
                getCustomer().setBAC(bac);
                getCustomer().setBalance(getCustomer().getBalance() + balance);
                getCustomer().setNumOfWeakDrinks(getCustomer().getNumOfWeakDrinks() + numOfWeakDrinks);
                getCustomer().setNumOfMediumDrinks(getCustomer().getNumOfMediumDrinks() + numOfMediumDrinks);
                getCustomer().setNumOfStrongDrinks(getCustomer().getNumOfStrongDrinks() + numOfStrongDrinks);
                getCustomer().getCart().clear();
                System.out.println(getCustomer().getCart().size());
            }
            else {
                System.out.println(getCustomer().getCart().size());
                sendObject("placeOrder");
                sendObject(new Object[] {getCustomer().getCart(), getCustomer()});
                System.out.println(receiveObject());
                getCustomer().setGramsOfAlcohol(grams);
                getCustomer().setBAC(bac);
                getCustomer().setBalance(getCustomer().getBalance() + balance);
                getCustomer().setNumOfWeakDrinks(getCustomer().getNumOfWeakDrinks() + numOfWeakDrinks);
                getCustomer().setNumOfMediumDrinks(getCustomer().getNumOfMediumDrinks() + numOfMediumDrinks);
                getCustomer().setNumOfStrongDrinks(getCustomer().getNumOfStrongDrinks() + numOfStrongDrinks);
                getCustomer().getCart().clear();
                System.out.println(getCustomer().getCart().size());
            }
        }
        else {
            return "Your Cart is Empty";
        }
        return "Error Placing Order";
    }
    
    
    public String addToCart(Drink drink) {
        if(drink.getSpiritAmount() != 0) {
            if(getCustomer().getAge() >= Server.getDrinkingAge()) {
                getCustomer().getCart().add(drink);
                return drink.getName() + " has Been Added to Your Cart";
            }
            else {
                return "Customer is not of the Legal Drinking Age to Order an Alcholic Drink";
            }
        }
        getCustomer().getCart().add(drink);
        return drink.getName() + " has Been Added to Your Cart";
    }
    
    public void notifyManager(double bac) {
        
    }
    
    public void logout() throws IOException, ClassNotFoundException {
        sendObject("Logout");
        
        if(getCustomer() != null && getManager() == null) {
            //System.out.println("hello");
            System.out.println(getCustomer().getCart().size());
            sendObject(getCustomer());
            //System.out.println("cust");
            System.out.println(receiveObject());
            
            setCustomer(null);
            System.out.println(getCustomer());
            getDrinkMenu().clear();
        }
        else if(getCustomer() == null && getManager() != null) {
            sendObject(getManager());
            //System.out.println("hello1");
            System.out.println(receiveObject());
            setManager(null);
            getDrinkMenu().clear();
        }
        else {
            System.out.println("Error Logging Out");
        }
    }
    
    public Socket getSocket() {
        return socket;
    }
    
    private Customer getCustomer() {
        return customer;
    }
    
    private void setCustomer(Customer customer) {
        this.customer = customer;
    }
    
    private Manager getManager() {
        return manager;
    }
    
    private void setManager(Manager manager) {
        this.manager = manager;
    }
    
    public ArrayList<Drink> getDrinkMenu() {
        return drinkMenu;
    }
    
    public void setDrinkMenu(TreeSet<Drink> drinks) {
        drinkMenu.clear();
        drinkMenu = new ArrayList<>(drinks);
    }
    
    public void setDrinkMenu(ArrayList<Drink> drinks) {
        drinkMenu.clear();
        drinkMenu = new ArrayList<>(drinks);
    }
    
    
    public void sendObject(Object obj) throws IOException {
        output.writeObject(obj);
    }
    
    public Object receiveObject() throws IOException, ClassNotFoundException {
        Object message = input.readObject();

        if(message.equals("Update")) {
            System.out.println("There is an Update");
            update();
            return receiveObject();
        }
        return message;
    }  
    
    public void update() throws IOException, ClassNotFoundException {
        setDrinkMenu((TreeSet<Drink>) receiveObject());
        System.out.println("drink menu updated....");
    }
    
    
    public void createThread() {
        updateThread = new Thread(() -> {
            try {
                while(!Thread.currentThread().isInterrupted()) {
                    receiveObject();
                
                }
            } catch (Exception ex) {
                System.exit(0);
            }
        });
        updateThread.start();
    }
}
