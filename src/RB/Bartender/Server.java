package RB.Bartender;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.Serializable;
import static java.lang.System.out;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.net.InetAddress;
import java.net.ServerSocket;
import java.net.Socket;
import java.net.URISyntaxException;
import java.net.URL;
import java.time.LocalDate;
import java.time.Period;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.Collections;
import java.util.Comparator;
import java.util.Enumeration;
import java.util.Formatter;
import java.util.List;
import java.util.Scanner;
import java.util.Set;
import java.util.TreeSet;
import java.util.jar.JarEntry;
import java.util.jar.JarFile;
import java.util.logging.Level;
import java.util.logging.Logger;
import java.util.zip.ZipEntry;
import java.util.zip.ZipInputStream;
import sun.misc.Launcher;

/**
 *
 * @authors Anthony Spiteri
 *          Cristian Nuosci
 *          Shahezad Kassam
 */

public class Server implements Runnable, Serializable {
    
    private static final int port = 49797;
    private static final String domainName = "localhost";    //"DESKTOP-BHRA33J";
    private Socket socket;
    private static final int maxNumOfKiosks = 4;
    private ObjectOutputStream output;
    private ObjectInputStream input;
    private static ArrayList<ObjectOutputStream> clients = new ArrayList<>();
    private boolean loggedIn = false;
    
    private static ArrayList<Bottle> ingredients = new ArrayList<>();       // all ingredients currently available to the Robotic Bartender
    private static TreeSet<Drink> drinkMenu = new TreeSet<>();              // all drinks currently available to be made
    private static ArrayList<Customer> customers = new ArrayList<>();       // all activated customer accounts
    private static ArrayList<Manager> managers = new ArrayList<>();         // all activated manager accounts
    private static ArrayList<String> registeredTags = new ArrayList<>();    // all regsitered tags
    private ArrayList<String> initIngredients = new ArrayList<>();          // all ingredients initalized to the Robotic Bartender
    private static ArrayList<Order> orders = new ArrayList<>();             // all orders in the queue
    private static ArrayList<Drink> drinks = new ArrayList<>();             // list of all possible drinks to determine drink popularity
    
    private static final int drinkingAge = 19;
    private static final double warningLimit = 0.05;
    private static final double cutoffLimit = 0.08;
    
    public boolean inIDE = true;
    
    public Server(Socket socket) throws IOException {
        this.socket = socket;
        output = new ObjectOutputStream(getSocket().getOutputStream());
        input = new ObjectInputStream(getSocket().getInputStream());
    }
    
    @Override
    public void run() {
        try {
            /*
            Vodka.initBrands();
            Rum.initBrands();
            Gin.initBrands();
            TripleSec.initBrands();
            OrangeJuice.initBrands();
            CranberryJuice.initBrands();
            SodaWater.initBrands();
            LemonJuice.initBrands();
            SimpleSyrup.initBrands();
            
            addBottle(new Object[] {Vodka.getBrands().get(0), 20, 1});
            addBottle(new Object[] {CranberryJuice.getBrands().get(0), 25, 2});
            addBottle(new Object[] {LemonJuice.getBrands().get(0), 25, 3});
            addBottle(new Object[] {Gin.getBrands().get(0), 20, 4});
            addBottle(new Object[] {OrangeJuice.getBrands().get(0), 20, 5});
            addBottle(new Object[] {Rum.getBrands().get(0), 20, 6});
            addBottle(new Object[] {SimpleSyrup.getBrands().get(0), 20, 7});
            addBottle(new Object[] {SodaWater.getBrands().get(0), 20, 8});
            addBottle(new Object[] {TripleSec.getBrands().get(0), 20, 9});
            */
            
            /*
            ArrayList<Drink> drinkMenu = new ArrayList<>(getDrinkMenu());
            int count = 0;
            System.out.println(drinkMenu.size());
            for(int i = 0; i < getDrinkMenu().size(); i++) {
                
                System.out.println(drinkMenu.get(i).getSpiritAmount() + "     " +drinkMenu.get(i).getName());
                if(drinkMenu.get(i).getStrength().equals("Weak")) {
                    count++;
                }
            }
            System.out.println("Weak " + count);
            
            count = 0;
            for(int i = 0; i < getDrinkMenu().size(); i++) {
                if(drinkMenu.get(i).getStrength().equals("Medium")) {
                    count++;
                }
            }
            System.out.println("Medium " + count);
            
            count = 0;
            for(int i = 0; i < getDrinkMenu().size(); i++) {
                if(drinkMenu.get(i).getStrength().equals("Strong")) {
                    count++;
                }
            }
            System.out.println("Strong " + count);
            */
            initServer();
            getClients().add(output);

            /*
            addCustomer(new Object[] {"2", "Anthony", "Spiteri", LocalDate.of(1997, 9, 11), "Male"});
            Customer cust = getCustomers().get(getCustomers().size() - 1);
            cust.setBAC(0.05);
            cust.setGramsOfAlcohol(9.562);
            System.out.println("bac: " + cust.getBAC());
            System.out.println("grams: " + cust.getGramsOfAlcohol());
            System.out.println("befre");
            sendObject(cust);
            System.out.println("after");
            Customer cust1 = (Customer)receiveObject();
            System.out.println("new bac: " + cust1.getBAC());
            System.out.println("new grams: " + cust1.getGramsOfAlcohol());
            */
            
            String type = (String)receiveObject();  // receives the type of system connected
            if(type.equals("Kiosk")) {
                if(getManagers().isEmpty()) {
                    sendObject(registerTags((String)receiveObject()));
                    sendObject(addManager((Object[])receiveObject()));
                }

                loginScreen((String)receiveObject());   // receieves the action the user wants to do within the login screen
            }                
            
            else if(type.equals("Bartender")) {
                
            } 
        } catch (IOException ex) {
            Logger.getLogger(Server.class.getName()).log(Level.SEVERE, null, ex);
        } catch (ClassNotFoundException ex) {
            Logger.getLogger(Server.class.getName()).log(Level.SEVERE, null, ex);
        } catch (Exception ex) {
            Logger.getLogger(Server.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    private void initServer() throws Exception {
        // gets all the ingredient classes
        ArrayList<String> ingredClasses = new ArrayList<>();
        
        if(inIDE == false) {
        JarFile jar = new JarFile(System.getProperty("java.class.path"));
        
        Enumeration<JarEntry> entries = jar.entries(); //gives ALL entries in jar
        while(entries.hasMoreElements()) {
            JarEntry entry = entries.nextElement();
            String file = entry.getName();
            if (file.endsWith(".class")) {
                String className = file.replace('/', '.').substring(0, file.length() - 6);
                if(Ingredient.class.isAssignableFrom(Class.forName(className))) {
                        ingredClasses.add(className);
                }
            }
        }
        jar.close();
       
        }
        else if(inIDE == true) {
        final URL url = Launcher.class.getResource("/RB/Bartender");
            if (url != null) {
                try {
                    final File apps = new File(url.toURI());
                    for (File app : apps.listFiles()) {
                        if(Ingredient.class.isAssignableFrom(Class.forName("RB.Bartender." + app.getName().replace(".class", "")))) {
                            ingredClasses.add("RB.Bartender." + app.getName().replace(".class", ""));
                        }
                    }
                } catch (URISyntaxException ex) {
                    Logger.getLogger(Kiosk.class.getName()).log(Level.SEVERE, null, ex);
                }
            }
        }
        
        ingredClasses.remove("RB.Bartender.Ingredient");
        
        // initializes all ingredient brands
        TreeSet<Drink> temp = new TreeSet<>();
        
        for(int i = 0; i < ingredClasses.size(); i++) {
            Class ingredientClass = Class.forName(ingredClasses.get(i));
            
            Method initBrands = ingredientClass.getMethod("initBrands");
            initBrands.invoke(null);
            
            Method initDrinks = ingredientClass.getMethod("initDrinks");
            initDrinks.invoke(null);
            
            Method getDrinks = ingredientClass.getMethod("getDrinks");
            ArrayList<Drink> drinks = (ArrayList<Drink>)getDrinks.invoke(null);
        
            temp.addAll(drinks);

            getInitIngredients().add(ingredientClass.toString().substring(19).replaceAll("(\\p{Ll})(\\p{Lu})","$1 $2"));
        }
        
        // initializes all drinks     
        ArrayList<Drink> allDrinks = new ArrayList<>(temp);
        ArrayList<String> drinkNames = new ArrayList<>();
        File drinks = new File("Drinks");
        File[] listOfDrinks = drinks.listFiles();
        
        for(int i = 0; i < listOfDrinks.length; i++) {
            drinkNames.add(listOfDrinks[i].getName().substring(0, listOfDrinks[i].getName().length() - 4));
        }
        
        // initializes the data to determine popularity of the drink
        for(int i = 0; i < allDrinks.size(); i++) {
            int index = nameSearch(drinkNames, allDrinks.get(i).getName());
            if(index != -1) {
                if(listOfDrinks[index].isFile() && listOfDrinks[index].getName().endsWith(".txt")) {
                    try (FileInputStream file = new FileInputStream(listOfDrinks[i]); ObjectInputStream fileInput = new ObjectInputStream(file)) {
                        Drink drink = (Drink)fileInput.readObject();
                        getDrinks().add(new Drink(drink.getName()));
                        getDrinks().get(getDrinks().size() - 1).setCount(drink.getCount());
                    }
                }
            }
            else {
                getDrinks().add(new Drink(allDrinks.get(i).getName()));
                createFile("Drinks", allDrinks.get(i).getName(), getDrinks().get(getDrinks().size() - 1));
            }
        }

        // initializes all managers
        File man = new File("Managers");
        File[] listOfManagers = man.listFiles();
        for(int i = 0; i < listOfManagers.length; i++) {
            if(listOfManagers[i].isFile() && listOfManagers[i].getName().endsWith(".txt")) {
                try (FileInputStream file = new FileInputStream(listOfManagers[i]); ObjectInputStream fileInput = new ObjectInputStream(file)) {
                    getManagers().add((Manager)fileInput.readObject());
                }
            }
        }

        // initializes all customers
        File cust = new File("Customers");
        File[] listOfCustomers = cust.listFiles();
        for(int i = 0; i < listOfCustomers.length; i++) {
            if(listOfCustomers[i].isFile() && listOfCustomers[i].getName().endsWith(".txt")) {
                try (FileInputStream file = new FileInputStream(listOfCustomers[i]); ObjectInputStream fileInput = new ObjectInputStream(file)) {
                    getCustomers().add((Customer)fileInput.readObject());
                }
            }
        }
        
        // initializes all available ingredients and updates the drinkMenu
        File ingred = new File("Ingredients");
        File[] listOfIngreds = ingred.listFiles();
        for(int i = 0; i < listOfIngreds.length; i++) {
            if(listOfIngreds[i].isFile() && listOfIngreds[i].getName().endsWith(".txt")) {
                try (FileInputStream file = new FileInputStream(listOfIngreds[i]); ObjectInputStream fileInput = new ObjectInputStream(file)) {
                    getIngredients().add((Bottle)fileInput.readObject());
                }
            }
        }
        
        for(int i = 0; i < getIngredients().size(); i++) {
            updateDrinks("add", getIngredients().get(i).getIngredient().getType());
        }
        
        // initializes all the registered tags
        File tags = new File("RegisteredTags");
        File[] listOfTags = tags.listFiles();
        for(int i = 0; i < listOfTags.length; i++) {
            if(listOfTags[i].isFile() && listOfTags[i].getName().endsWith(".txt")) {
                try (FileInputStream file = new FileInputStream(listOfTags[i]); ObjectInputStream fileInput = new ObjectInputStream(file)) { 
                    getRegisteredTags().add((String)fileInput.readObject()); 
                }
            }
        }
        
        // initializes all orders
        File order = new File("Orders");
        File[] listOfOrders = order.listFiles();
        for(int i = 0; i < listOfOrders.length; i++) {
            if(listOfOrders[i].isFile() && listOfOrders[i].getName().endsWith(".txt")) {
                try (FileInputStream file = new FileInputStream(listOfOrders[i]); ObjectInputStream fileInput = new ObjectInputStream(file)) { 
                    getOrders().add((Order)fileInput.readObject());
                }
            }
        }
        System.out.println("Server Initialized");
    }
    
    private void loginScreen(String action) throws IOException, ClassNotFoundException, Exception {
        if(action.equals("Sign Up")) {
            sendObject(addCustomer((Object[])receiveObject()));
            loginScreen((String)receiveObject());
        }
                
        else if(action.equals("Login")) {
            Account account = validateTagNumber((String)receiveObject());   // validate tag number
            
            if(account instanceof Customer) {
                sendObject("Customer");                                     // tells the kiosk the account is a customer
                sendObject((Customer)account);                              // gives the kiosk the customer account info
                setLoggedIn(true);
                while(getLoggedIn() == true) {
                    sendObject(customerMethods((String)receiveObject()));
                }
                loginScreen((String)receiveObject());
            }
            
            else if(account instanceof Manager) {
                sendObject("Manager");                                      // tells the kiosk the account is a manager
                sendObject((Manager)account);                               // gives the kiosk the manager account info
                setLoggedIn(true);
                while(getLoggedIn() == true) {
                    sendObject(managerMethods((String)receiveObject()));
                }
                loginScreen((String)receiveObject());
            }
            
            else {
                sendObject("Error Finding Account");
            }
        }
    }
    
    private Object customerMethods(String method) throws Exception {
        switch(method) {
            case "getDrinkMenu":
                TreeSet<Drink> drinks = new TreeSet<>(getDrinkMenu());  
                Customer customer = (Customer)receiveObject();
                ArrayList<String> ingredientNames = new ArrayList<>();                          // list of all ingredient names in Robotic Bartender
            
                for(int i = 0; i < getIngredients().size(); i++) {
                    ingredientNames.add(getIngredients().get(i).getIngredient().getType());
                }

                for(int i = 0; i < customer.getCustomizedDrinks().size(); i++) {
                    Drink drink = customer.getCustomizedDrinks().get(i);
                    List<String> ingredient = Arrays.asList(drink.getIngredName());             // list of all ingredients in the selected drink

                    if(ingredientNames.containsAll(ingredient)) {                               // if Robotic Bartender has all the ingredients needed to make the drink
                        Ingredient[] ingredientBrands = new Ingredient[ingredient.size()];
                        double spiritAmount = 0;

                        for(int j = 0; j < ingredient.size(); j++) {
                            int index = ingredientNames.indexOf(ingredient.get(j));
                            ingredientBrands[j] = getIngredients().get(index).getIngredient();  // adds the ingredient bottle to the ingredientBrands list
                            if(ingredientBrands[j].getABV() != 0) {
                                spiritAmount = spiritAmount + drink.getAmount()[j];
                            }
                        }
                        spiritAmount = spiritAmount*drink.getGlass().getVolume();

                        if(drink.getPrice() == null) {
                            if(0 <= spiritAmount && spiritAmount < 1.5) {
                                drink.setPrice(5);
                            }
                            else if(1.5 <= spiritAmount && spiritAmount < 2.5) {
                                drink.setPrice(6.5);
                            }
                            else if(2.5 <= spiritAmount) {
                                drink.setPrice(8);
                            }
                        }
                        drinks.add(new Drink(drink.getName(), ingredientBrands, drink.getAmount(), drink.getGlass(), drink.getIce(), drink.getShake(), drink.getPrice(), spiritAmount));
                    }
                }
                return drinks;
                
            case "addCustomer":
                return addCustomer((Object[])receiveObject());
                
            case "filterDrinkMenu":
                ArrayList<String> ingred = new ArrayList<>();
                for(int i = 0; i < getIngredients().size(); i++) {
                    ingred.add(getIngredients().get(i).getIngredient().getType());
                }
                Collections.sort(ingred);
                sendObject(ingred); // sends all ingredients in the Robotic Bartender
                return filterDrinkMenu((String[])receiveObject());
                
            case "placeOrder":
                return placeOrder((Object[])receiveObject());
                
            case "createDrink":
                return createDrink((Object[])receiveObject());
                
            case "Logout":
                Customer account = (Customer)receiveObject();
                for(int i = 0; i < getCustomers().size(); i++) {
                    if(getCustomers().get(i).getTagNumber().equals(account.getTagNumber())) {
                        getCustomers().set(i, account);
                        break;
                    }
                }
                deleteFile("Customers", account.getTagNumber());
                createFile("Customers", account.getTagNumber(), account);
                setLoggedIn(false);
                return "Successfully Logged Out";
                
            case "Notify Manager":
                Object[] info = (Object[])receiveObject();
                for(int i = 0; i < getClients().size(); i++) {
                    getClients().get(i).writeObject("Notify Manager");
                    getClients().get(i).writeObject(info);
                }
                return "";
                
            default:
                System.out.println("method requested: " + method);  // for debugging
                return "Invalid Function Selected";
        }
    }
    
    private Object managerMethods(String method) throws IOException, ClassNotFoundException, Exception {
        switch(method) {
            case "getDrinkMenu":
                return getDrinkMenu();
        
            case "registerTags":
                return registerTags((String)receiveObject());
                
            case "Logout":
                Manager account = (Manager)receiveObject();
                for(int i = 0; i < getManagers().size(); i++) {
                    if(getManagers().get(i).getTagNumber().equals(account.getTagNumber())) {
                        getManagers().set(i, account);
                        break;
                    }
                }
                deleteFile("Managers", account.getTagNumber());
                createFile("Managers", account.getTagNumber(), account);
                setLoggedIn(false);
                return "Successfully Logged Out";
                
            case "addBottle":
                sendObject(getInitIngredients());
                String ingred = (String)receiveObject();
                Class ingredientClass = Class.forName("RB.Bartender." + ingred.replace(" ", ""));
                Method getBrands = ingredientClass.getMethod("getBrands");
                sendObject(getBrands.invoke(null));
                return addBottle((Object[])receiveObject());
                
            case "removeBottle":
                return removeBottle((int)receiveObject());
                
            default:
                System.out.println("method requested: " + method);  // for debugging
                return "Invalid Function Selected";
        }
    }
    
    private Object bartenderMethods(String method) {
        switch(method) {
            case "getDrinkMenu":
                return getDrinkMenu();
        
                
            default:
                System.out.println("method requested: " + method);  // for debugging
                return "Invalid Function Selected";
        }
    }
    
    private Account validateTagNumber(String tagNumber) {
        try {
            if(new File("Managers//" + tagNumber + ".txt").exists()) {
                FileInputStream file = new FileInputStream("Managers//" + tagNumber + ".txt");
                ObjectInputStream fileInput = new ObjectInputStream(file);
               
                Manager manager = (Manager)fileInput.readObject();
                
                fileInput.close();
                file.close();

                return manager;
            }
        
            else if(new File("Customers//" + tagNumber + ".txt").exists()) {
                FileInputStream file = new FileInputStream("Customers//" + tagNumber + ".txt");
                ObjectInputStream fileInput = new ObjectInputStream(file);
               
                Customer customer = (Customer)fileInput.readObject();
                
                fileInput.close();
                file.close();
                
                return customer;
            }
            
        } catch (IOException | ClassNotFoundException ex) {
            Logger.getLogger(Kiosk.class.getName()).log(Level.SEVERE, null, ex);
        }
        return null;
    }
    
    private String addBottle(Object[] info) throws Exception {
        Ingredient ingredient = (Ingredient)info[0];
        int size = (int)info[1];
        int slot = (int)info[2];
        
        for(int i = 0; i < getIngredients().size(); i++) {
            if(getIngredients().get(i).getSlot() == slot) {
                return "Slot " + slot + " is Already Occupied";
            }
        }
        
        getIngredients().add(new Bottle(ingredient, size, slot));
        createFile("Ingredients", slot + " - " + ingredient.getName(), (Bottle)getIngredients().get(getIngredients().size() - 1));
        updateDrinks("add", ingredient.getType());
        return ingredient.getName() + " is Added to Slot " + slot;
    }
    
    private String removeBottle(int slot) throws Exception {
        for(int i = 0; i < getIngredients().size(); i++) {
            if(getIngredients().get(i).getSlot() == slot) {  
                updateDrinks("remove", getIngredients().get(i).getIngredient().getType());
                deleteFile("Ingredients", getIngredients().get(i).getIngredient().getName());
                String ingredient = getIngredients().get(i).getIngredient().getName();
                getIngredients().remove(i); 
                return ingredient + " is Removed from Slot " + slot;
            }
        } 
        return "Error Removing Bottle from Slot " + slot;
    }
    
    private void updateDrinks(String action, String ingred) throws Exception {
        Class ingredientClass = Class.forName("RB.Bartender." + ingred.replace(" ", ""));
            
        Method getDrinks = ingredientClass.getMethod("getDrinks");
        ArrayList<Drink> drinks = (ArrayList<Drink>)getDrinks.invoke(null);
        
        if(action.equals("add")) {
            ArrayList<String> ingredientNames = new ArrayList<>();                          // list of all ingredient names in Robotic Bartender
            
            for(int i = 0; i < getIngredients().size(); i++) {
                ingredientNames.add(getIngredients().get(i).getIngredient().getType());
            }

            for(int i = 0; i < drinks.size(); i++) {
                List<String> ingredient = Arrays.asList(drinks.get(i).getIngredName());     // list of all ingredients in the selected drink
                
                if(ingredientNames.containsAll(ingredient)) {                               // if Robotic Bartender has all the ingredients needed to make the drink
                    Ingredient[] ingredientBrands = new Ingredient[ingredient.size()];
                    double spiritAmount = 0;
                    
                    for(int j = 0; j < ingredient.size(); j++) {
                        int index = ingredientNames.indexOf(ingredient.get(j));
                        ingredientBrands[j] = getIngredients().get(index).getIngredient();  // adds the ingredient bottle to the ingredientBrands list
                        if(ingredientBrands[j].getABV() != 0) {
                            spiritAmount = spiritAmount + drinks.get(i).getAmount()[j];
                        }
                    }
                    spiritAmount = spiritAmount*drinks.get(i).getGlass().getVolume();
                    
                    if(drinks.get(i).getPrice() == null) {
                        if(0 <= spiritAmount && spiritAmount < 1.5) {
                            drinks.get(i).setPrice(5);
                        }
                        else if(1.5 <= spiritAmount && spiritAmount < 2.5) {
                            drinks.get(i).setPrice(6.5);
                        }
                        else if(2.5 <= spiritAmount) {
                            drinks.get(i).setPrice(8);
                        }
                    }
                    getDrinkMenu().add(new Drink(drinks.get(i).getName(), ingredientBrands, drinks.get(i).getAmount(), drinks.get(i).getGlass(), drinks.get(i).getIce(), drinks.get(i).getShake(), drinks.get(i).getPrice(), spiritAmount));
                }
            }
        }
        
        else if(action.equals("remove")) {
            ArrayList<Drink> drinkMenu = new ArrayList<>(getDrinkMenu());

            for(int i = 0; i < drinkMenu.size(); i++) {            
                for(int j = 0; j < drinks.size(); j++) {
                    // if the drinkMenu contains a drink will the ingredient getting removed
                    if(drinkMenu.get(i).getName().equals(drinks.get(j).getName()) && drinkMenu.contains(drinkMenu.get(i))) {
                        getDrinkMenu().remove(drinkMenu.get(i));
                    }
                }
            }
        }
        
        for(int i = 0; i < getClients().size(); i++) {
            getClients().get(i).writeObject("Update");
            getClients().get(i).writeObject(getDrinkMenu());
        }

        drinks.clear();
    }
    
    private ArrayList<Drink> filterDrinkMenu(String[] info) {
        String category = info[0];
        String type = info[1];
        ArrayList<Drink> drinks = new ArrayList<>(getDrinkMenu());
        ArrayList<Drink> filtered = new ArrayList<>();
        
        switch(category) {
            case "Type":
                for(int i = 0; i < drinks.size(); i++) {
                    if(drinks.get(i).getType().equals(type)) {
                        filtered.add(drinks.get(i));
                    }
                }
                return filtered;
                
            case "Ingredient":
                for(int i = 0; i < drinks.size(); i++) {
                    for(int j = 0; j < drinks.get(i).getIngredient().length; j++) {
                        if(drinks.get(i).getIngredient()[j].getType().equals(type)) {
                            filtered.add(drinks.get(i));
                        }
                    }
                }
                return filtered;
                
            case "Strength":
                for(int i = 0; i < drinks.size(); i++) {
                    if(drinks.get(i).getStrength().equals(type)) {
                        filtered.add(drinks.get(i));
                    }
                }
                return filtered;
                
            case "Price":
                if(type.equals("Low to High")) {
                    Collections.sort(drinks, (Drink d1, Drink d2) -> Double.compare(d1.getPrice(), d2.getPrice()));
                    return drinks;
                }
                    
                else if(type.equals("High to Low")) {
                    Collections.sort(drinks, (Drink d1, Drink d2) -> Double.compare(d2.getPrice(), d1.getPrice()));
                    return drinks;
                }
                
            case "Most Popular":
                Collections.sort(getDrinks(), (Drink d1, Drink d2) -> Double.compare(d2.getCount(), d1.getCount()));
                
                for(int i = 0; i < getDrinks().size(); i++) {
                    int index = search(drinks, getDrinks().get(i).getName());
                    if(index != -1) {
                        filtered.add(drinks.get(index));
                    }
                }
                return filtered;
                
            default:
                return null;
        }
    }

    private String createDrink(Object[] info) throws Exception {
        String name = (String)info[0];
        String[] ingredName = (String[])info[1];
        double[] amount = (double[])info[2];
        Glass glass = (Glass)info[3];
        boolean ice = (boolean)info[4];
        boolean shake = (boolean)info[5];
        Customer customer = (Customer)info[6];
        
        if(!name.isEmpty()) {
            if(name.length() <= 20) {
                for(int i = 0; i < name.length(); i++) {
                    int c = Character.getNumericValue(name.charAt(i));
                    if(((0 <= c) && (c <= 35)) == false) {
                        return "The Name of the Drink Cannot Contain Special Characters";
                    }
                }  

                double totalAmount = 0;
                if(ice == true) {
                    totalAmount = totalAmount - Glass.getVolumeOfIce();
                }

                if(shake == true) {
                    totalAmount = totalAmount - Glass.getVolumeOfDillution();
                }

                for(int i = 0; i < amount.length; i++) {
                    totalAmount = totalAmount + amount[i];
                }

                if(totalAmount <= (glass.getVolume())) {
                    double spiritAmount = 0;
                    
                    for(int i = 0; i < ingredName.length; i++) {
                        Class ingredientClass = Class.forName("RB.Bartender." + ingredName[i].replace(" ", ""));
                        Method isAlcohol = ingredientClass.getMethod("isAlcohol");
                        
                        if((boolean)isAlcohol.invoke(null) == true) {
                            spiritAmount = spiritAmount + amount[i];
                        }
                    }
                    spiritAmount = spiritAmount*glass.getVolume();
                    
                    if(spiritAmount <= 3) {
                        customer.getCustomizedDrinks().add(new Drink(name, ingredName, amount, glass, ice, shake, null));                        
                        return "Your Customized Drink has Successfully been Added to the Drink Menu";
                    }
                    else {
                        return "The Drink Cannot Exceed a Total of 3oz of Alcohol";
                    }
                }
                else {
                    return "The Amount of the Drink Exceeds the Amount the Glass can Hold";
                }
            }
            else {
                return "The Name of the Drink Cannot Exceed 20 Characters";
            }
        }
        else {
            return "The Drink Must Have a Name";
        }
    }
    
    private String placeOrder(Object[] info) throws IOException {
        int number = 1;
        ArrayList<Drink> drinks = (ArrayList<Drink>)info[0];
        Customer customer = (Customer)info[1];
        ArrayList<String> ingredientNames = new ArrayList<>();
        
        for(int i = 0; i < getIngredients().size(); i++) {
            ingredientNames.add(getIngredients().get(i).getIngredient().getType());
        }
        
        for(int i = 0; i < drinks.size(); i++) {
            List<String> ingredient = new ArrayList<>();
            for(int j = 0; j < drinks.get(i).getIngredient().length; j++) {
                ingredient.add(drinks.get(i).getIngredient()[j].getType());
            }
            
            if(!ingredientNames.containsAll(ingredient)) {
                return "Error, Cannot make a " + drinks.get(i).getName() + " not all Ingredients are Available to Make the Drink";
            }
        }
        
        if(!getOrders().isEmpty()) {
            number = getOrders().get(getOrders().size() - 1).getNumber() + 1;
        }
        
        for(int i = 0; i < drinks.size(); i++) {
            int index = search(getDrinks(), drinks.get(i).getName());
            getDrinks().get(index).setCount(getDrinks().get(index).getCount() + 1);
            deleteFile("Drinks", getDrinks().get(index).getName());
            createFile("Drinks", getDrinks().get(index).getName(), getDrinks().get(index));
        }
        
        getOrders().add(new Order(number, drinks, customer));
        createFile("Orders", Integer.toString(number), getOrders().get(getOrders().size() - 1));
        return "Your Order Has Been Placed\nYour Order Number is " + number; 
    }
        
    private String addManager(Object[] info) throws IOException {
        String tagNumber = (String)info[0];
        String firstName = (String)info[1];
        String lastName = (String)info[2];
        
        if((firstName.isEmpty() && lastName.isEmpty()) == false) {
            if(firstName.length() <= 20 && lastName.length() <= 20) {
                for(int i = 0; i < firstName.length(); i++) {
                    int c = Character.getNumericValue(firstName.charAt(i));
                    if(((10 <= c) && (c <= 35)) == false) {
                        return "First Name Cannot Contain Special Characters";
                    }
                }

                for(int i = 0; i < lastName.length(); i++) {
                    int c = Character.getNumericValue(lastName.charAt(i));
                    if(((10 <= c) && (c <= 35)) == false) {
                        return "Last Name Cannot Contain Special Characters";
                    }
                }

                if(new File("RegisteredTags//" + tagNumber + ".txt").exists()) {
                    for(int i = 0; i < getManagers().size(); i++) {
                        if(getManagers().get(i).getTagNumber().equals(tagNumber)) {
                            return "The Tag Scanned Is Already Associated To An Account";
                        }
                    }

                    getManagers().add(new Manager(tagNumber, firstName, lastName));
                    createFile("Managers", tagNumber, (Manager)getManagers().get(getManagers().size() - 1));
                    return firstName + " " + lastName + "'s Account Is Successfully Created";
                }
                else {
                    return "The Tag Scanned is Not a Registered Tag";
                }
            }
            else {
                return "The First Name or Last Name Cannot Exceed 20 Characters";
            }
        }
        else {
            return "Please Enter a First Name and a Last Name";
        }
    }

    private String addCustomer(Object[] info) throws IOException {
        String tagNumber = (String)info[0];
        String firstName = (String)info[1];
        String lastName = (String)info[2];
        LocalDate birthday = (LocalDate)info[3];
        String gender = (String)info[4];
        
        if((firstName.isEmpty() && lastName.isEmpty()) == false) {
            if(firstName.length() <= 20 && lastName.length() <= 20) {
                for(int i = 0; i < firstName.length(); i++) {
                    int c = Character.getNumericValue(firstName.charAt(i));
                    if(((10 <= c) && (c <= 35)) == false) {
                        return "First Name Cannot Contain Special Characters";
                    }
                }

                for(int i = 0; i < lastName.length(); i++) {
                    int c = Character.getNumericValue(lastName.charAt(i));
                    if(((10 <= c) && (c <= 35)) == false) {
                        return "Last Name Cannot Contain Special Characters";
                    }
                }
                if(birthday.isBefore(LocalDate.now())) {
                    if(gender.equals("Male") || gender.equals("Female")) {
                        if(new File("RegisteredTags//" + tagNumber + ".txt").exists()) {
                            for(int i = 0; i < getCustomers().size(); i++) {
                                if(getCustomers().get(i).getTagNumber().equals(tagNumber)) {
                                    return "The Tag Scanned Is Already Associated To An Account";
                                }
                            }
                            getCustomers().add(new Customer(tagNumber, firstName, lastName, birthday, gender));
                            createFile("Customers", tagNumber, (Customer)getCustomers().get(getCustomers().size() - 1));
                            return firstName + " " + lastName + "'s Account Is Successfully Created";
                        }
                        else {
                            return "The Tag Scanned Is Not A Registered Tag";
                        }
                    }
                    else {
                        return "Gender Must Be Either Male Or Female";
                    }
                }
                else {
                    return "Please Enter A Valid Birthday";
                }
            }
            else {
                return "The First Name or Last Name Cannot Exceed 20 Characters";
            }
        }
        else {
            return "Please Enter A First Name And A Last Name";
        }
    }
    
    private String registerTags(String tagNumber) {
        try {
            File tag = new File("RegisteredTags//" + tagNumber + ".txt");
            if(!tag.exists()) {
                createFile("RegisteredTags", tagNumber, tagNumber);
                return "Tag Number " + tagNumber + " Is Registered";
            }
            else {
                return "Tag Number " + tagNumber + " Is Already Registered";
            }
            
        } catch (IOException ex) {
            return "Error Registering Tag";
        }
    }
    
    private void createFile(String folder, String file, Object contents) throws IOException {
        try {
            new Formatter(folder + "//" + file + ".txt");
            FileOutputStream fileOut = new FileOutputStream(folder + "//" + file + ".txt");
            ObjectOutputStream out = new ObjectOutputStream(fileOut);
            
            switch (folder) {
                case "RegisteredTags":
                    out.writeObject((String)contents);
                    break;
                    
                case "Customers":
                    out.writeObject((Customer)contents);
                    break;
                    
                case "Managers":
                    out.writeObject((Manager)contents);
                    break;
                    
                case "Ingredients":
                    out.writeObject((Bottle)contents);
                    break;
                    
                case "Orders":
                    out.writeObject((Order)contents);
                    break;
                    
                case "Drinks":
                    out.writeObject((Drink)contents);
                    break;
                    
                default:
                    break;
            }
            
            out.close();
            fileOut.close();
        }
        catch(FileNotFoundException e) {
            Logger.getLogger(Server.class.getName()).log(Level.SEVERE, null, e);
        }
    }
    
    private boolean deleteFile(String folder, String file) {
        boolean answer = false;
        File user = new File(folder + "//" + file + ".txt");
        if (user.exists()) {
            if(user.delete()) {
                return answer = true;
            }
        }
        return answer;
    }
    
    private int search(ArrayList<Drink> drinks, String name) {
        int low = 0;
        int high = drinks.size() - 1;
        int middle;

        while(low <= high) {
            middle = (low + high) / 2;

            if(drinks.get(middle).getName().compareTo(name) < 0) {
                low = middle + 1;
            }
            else if (drinks.get(middle).getName().compareTo(name) > 0) {
                high = middle - 1;
            }
            else {
                return middle;
            }
        }
        return -1;
    }
    
    private int nameSearch(ArrayList<String> drinks, String name) {
        int low = 0;
        int high = drinks.size() - 1;
        int middle;

        while(low <= high) {
            middle = (low + high) / 2;

            if(drinks.get(middle).compareTo(name) < 0) {
                low = middle + 1;
            }
            else if (drinks.get(middle).compareTo(name) > 0) {
                high = middle - 1;
            }
            else {
                return middle;
            }
        }
        return -1;
    }
    
    public static int getPort() {
        return port;
    }   
    
    public static String getDomainName() {
        return domainName;
    }
    
    public static int getDrinkingAge() {
        return drinkingAge;
    }
    
    public static double getWarningLimit() {
        return warningLimit;
    }
    
    public static double getCutoffLimit() {
        return cutoffLimit;
    }
    
    private static ArrayList<Bottle> getIngredients() {
        return ingredients;
    }
    
    private static TreeSet<Drink> getDrinkMenu() {
        return drinkMenu;
    }
    
    private static ArrayList<Customer> getCustomers() {
        return customers;
    }
    
    private static ArrayList<Manager> getManagers() {
        return managers;
    }
    private static ArrayList<String> getRegisteredTags() {
        return registeredTags;
    }
    
    private ArrayList<String> getInitIngredients() {
        return initIngredients;
    }
    
    private ArrayList<Order> getOrders() {
        return orders;
    }
    
    private ArrayList<Drink> getDrinks() {
        return drinks;
    }
    
    private boolean getLoggedIn() {
        return loggedIn;
    }
    
    private void setLoggedIn(boolean loggedIn) {
        this.loggedIn = loggedIn;
    }
    
    private Socket getSocket() {
        return socket;
    }
    
    private ArrayList<ObjectOutputStream> getClients() {
        return clients;
    }
    
    private void sendObject(Object obj) throws IOException {
        output.writeObject(obj);
    }
    
    private Object receiveObject() throws IOException, ClassNotFoundException {
        return input.readObject();
    }  
    
    public static void main(String[] args) throws Exception {

        /*
        // To get the domain name of the server
        System.out.println("Domain Name: " + InetAddress.getLocalHost().getHostName());
        
        // To get an available port number
        ServerSocket s = new ServerSocket(0);
        System.out.println("Available Port: " + s.getLocalPort());
        */
        
        // in future make it read from a file that has all the info
        Scanner scanner = new Scanner(System.in);
        System.out.println("Server, Kiosk or Bartender (S, K, B)");
        
        switch(scanner.next().toLowerCase()) {
            case "s":
                ServerSocket server = new ServerSocket(getPort(), maxNumOfKiosks);
                
                System.out.println("Server Online");
                
                while(true) {
                    Socket socket = server.accept();                   
                    new Thread(new Server(socket)).start();
                }
                
            case "k": 
                Socket socket = new Socket(getDomainName(), getPort()); 
                System.out.println("Connecting To " + getDomainName() + " On Port " + getPort());
                new Thread(new Kiosk(socket)).start();
                break;
            
            case "b":
                Socket socket1 = new Socket(getDomainName(), getPort()); 
                System.out.println("Connecting To " + getDomainName() + " On Port " + getPort());
                new Thread(new Bartender(socket1)).start();
                break;
        }
    }
}
