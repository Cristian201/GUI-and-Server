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
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.SynchronousQueue;
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
    private final int kioskNumber = 1;
    private Customer customer;
    private Manager manager;
    private ArrayList<Drink> drinkMenu = new ArrayList<>();
    public Thread updateThread;
    private SynchronousQueue queue = new SynchronousQueue();
    
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
            createThread();
            
            /*
            sendObject("1");
            System.out.println(receiveObject());
            sendObject(new Object[] {"1", "John", "Smith"});
            System.out.println(receiveObject());
            */
            
            //login("1");

            managerFunctions();
            
            //logout();
            //socket.close();
            
        } catch (IOException | ClassNotFoundException | InterruptedException ex) {
            Logger.getLogger(Kiosk.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    public void managerFunctions() throws IOException, ClassNotFoundException, InterruptedException {
  
        sendObject("Sign Up");  // notifies the server that user wants to create a new account
            
        sendObject(new Object[] {"2", "Anthony", "Spiteri", LocalDate.of(1997, 9, 11), "Male"});
        System.out.println(receiveObject());
        login("2");
        
    }
    
    public void login(String tagNumber) throws IOException, ClassNotFoundException, InterruptedException {
        sendObject("Login");
        sendObject(tagNumber);    // tag is scanned and the tag number is sent to verify
        String type = (String)receiveObject();
      
        switch (type) {
            case "Customer":
                setCustomer((Customer)receiveObject());
                
                sendObject("getDrinkMenu");
                sendObject(getCustomer());
                setDrinkMenu((TreeSet<Drink>)receiveObject());
                System.out.println(getDrinkMenu().size());

                /*
                sendObject("filterDrinkMenu");
                ArrayList<String> ingred = (ArrayList<String>)receiveObject();
                for(int i = 0; i < ingred.size(); i++)
                    System.out.println(ingred.get(i));
                sendObject(new String[] {"Price", "High to Low"});
                setDrinkMenu((ArrayList<Drink>)receiveObject());
                for(int i = 0; i < getDrinkMenu().size(); i++)
                    System.out.println(getDrinkMenu().get(i).getName()+"  "+getDrinkMenu().get(i).getPrice());
                
                */
                System.out.println("about to filter menu");
                sendObject("filterDrinkMenu");
                ArrayList<String> ingredients = new ArrayList<>((ArrayList<String>)receiveObject());    // gonna receive all the ingredients in the Robotic Bartender
                for(int i = 0; i < ingredients.size(); i++)
                    System.out.println(ingredients.get(i));
                
                sendObject(new String[] {"Most Popular", null});
                setDrinkMenu((ArrayList<Drink>)receiveObject());
                System.out.println(getDrinkMenu().size());
                for(int i = 0; i < getDrinkMenu().size(); i++)
                    System.out.println(getDrinkMenu().get(i).getName()+"  "+getDrinkMenu().get(i).getPrice());
                
                System.out.println(getDrinkMenu().size());
                System.out.println(addToCart(getDrinkMenu().get(0)));
                System.out.println(addToCart(getDrinkMenu().get(2)));
                
                placeOrder();
                
                System.out.println("about to log out..");
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
    
    public String placeOrder() throws IOException, ClassNotFoundException, InterruptedException {
        System.out.println(getCustomer().getFirstDrinkTime());
        System.out.println("time diff from now to last drink in hours");
        System.out.println((System.currentTimeMillis() - getCustomer().getFirstDrinkTime()) / (60*60*1000));
        if(!getCustomer().getCart().isEmpty()) {
            // if the firstDrinkTime is zero or if it has been 6 hours since their last drink
            if(getCustomer().getFirstDrinkTime() == 0 || ((System.currentTimeMillis() - getCustomer().getFirstDrinkTime()) / (60*60*1000)) > 6) { 
                getCustomer().setFirstDrinkTime(System.currentTimeMillis());
                getCustomer().setGramsOfAlcohol(0);
                getCustomer().setBAC(0);
                getCustomer().setNumOfWeakDrinks(0);
                getCustomer().setNumOfMediumDrinks(0);
                getCustomer().setNumOfStrongDrinks(0);
                System.out.println("cust data reset... new diff of drink time:"); 
            }
            
            double bac = 0;
            double grams = 0;
            double balance = 0;
            int numOfWeakDrinks = 0;
            int numOfMediumDrinks = 0;
            int numOfStrongDrinks = 0;
            
            for(int i = 0; i < getCustomer().getCart().size(); i++) {
                double[] info = getCustomer().calculateBAC(getCustomer().getCart().get(i));
                bac = bac + info[0];
                grams = grams + info[1];
                balance = balance + getCustomer().getCart().get(i).getPrice();
 
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
                getCustomer().setGramsOfAlcohol(grams);
                getCustomer().setBAC(bac);
                getCustomer().setBalance(getCustomer().getBalance() + balance);
                getCustomer().setNumOfWeakDrinks(getCustomer().getNumOfWeakDrinks() + numOfWeakDrinks);
                getCustomer().setNumOfMediumDrinks(getCustomer().getNumOfMediumDrinks() + numOfMediumDrinks);
                getCustomer().setNumOfStrongDrinks(getCustomer().getNumOfStrongDrinks() + numOfStrongDrinks);
                getCustomer().getLastOrder().clear();
                getCustomer().getLastOrder().addAll(getCustomer().getCart());
                getCustomer().getCart().clear();
                sendObject("placeOrder");
                sendObject(new Object[] {getCustomer().getLastOrder(), getCustomer()});
                System.out.println(receiveObject());
                notifyManager(bac);
                output.reset();
            }
            else {
                getCustomer().setGramsOfAlcohol(grams);
                getCustomer().setBAC(bac);
                getCustomer().setBalance(getCustomer().getBalance() + balance);
                getCustomer().setNumOfWeakDrinks(getCustomer().getNumOfWeakDrinks() + numOfWeakDrinks);
                getCustomer().setNumOfMediumDrinks(getCustomer().getNumOfMediumDrinks() + numOfMediumDrinks);
                getCustomer().setNumOfStrongDrinks(getCustomer().getNumOfStrongDrinks() + numOfStrongDrinks);
                getCustomer().getLastOrder().clear();
                getCustomer().getLastOrder().addAll(getCustomer().getCart());
                getCustomer().getCart().clear();
                sendObject("placeOrder");
                sendObject(new Object[] {getCustomer().getLastOrder(), getCustomer()});
                System.out.println(receiveObject());
                output.reset();
            }
        }
        else {
            return "Your Cart is Empty";
        }
        return "Error Placing Order";
    }
    
    public void notifyManager(double bac) throws IOException, InterruptedException, ClassNotFoundException {
        sendObject("Notify Manager");
        sendObject(new Object[] {kioskNumber, getCustomer(), bac});
        receiveObject();
    }
    
    public void logout() throws IOException, ClassNotFoundException, InterruptedException {
        sendObject("Logout");
        
        if(getCustomer() != null && getManager() == null) {
            sendObject(getCustomer());
            System.out.println(receiveObject());      
            setCustomer(null);
            getDrinkMenu().clear();
        }
        else if(getCustomer() == null && getManager() != null) {
            sendObject(getManager());
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
    
    public void sendObject(Object obj) throws IOException {
        output.writeObject(obj);
    }
    
    public Object receiveObject() throws IOException, ClassNotFoundException, InterruptedException {
        return queue.take();
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
    
    public void createThread() {
        updateThread = new Thread(() -> {
            try {
                while(true) {
                    Object message = input.readObject();

                    if(message.equals("Update")) {
                        System.out.println("there is an new drinkMenu...");
                        setDrinkMenu((TreeSet<Drink>)input.readObject());
                    }
                    else if(message.equals("Notify Manager")) {
                        if(getManager() != null) {
                          
                            Object[] info = (Object[])input.readObject();
                            int kioskNumber = (int)info[0];
                            Customer customer = (Customer)info[1];
                            double bac = (double)info[2];

                            System.out.println("At Kiosk " + kioskNumber + ", " + customer.getFirstName() + " " + customer.getLastName() + " has a BAC of " + bac); 
                        }
                        else {
                            receiveObject();
                        }
                    }                        
                    else {
                        //System.out.println("in thread else.....");
                        queue.put(message); 
                    }
                }
            } catch (IOException | ClassNotFoundException | InterruptedException ex) {
                Logger.getLogger(Kiosk.class.getName()).log(Level.SEVERE, null, ex);
            }   
        });
        updateThread.start();
    }
}
