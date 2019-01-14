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
    private static final String domainName = "DESKTOP-BHRA33J";
    private Socket socket;
    private static final int maxNumOfKiosks = 4;
    private ObjectOutputStream output;
    private ObjectInputStream input;
    public static ThreadGroup clients;
    private boolean loggedIn = false;
    
    private static ArrayList<Bottle> ingredients = new ArrayList<>();
    private static TreeSet<Drink> drinkMenu = new TreeSet<>();
    private static ArrayList<Customer> customers = new ArrayList<>();
    private static ArrayList<Manager> managers = new ArrayList<>();
    private static ArrayList<String> registeredTags = new ArrayList<>();
    private ArrayList<String> initIngredients = new ArrayList<>();
    private static ArrayList<Order> orders = new ArrayList<>();
    
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
            
            //System.out.println(getClients().activeCount());
            //getClients().interrupt();
            //System.out.println(Thread.currentThread().isInterrupted());
            
            String type = (String)receiveObject();  // receives the type of system connected
            if(type.equals("Kiosk")) {
                //getClients().add((Thread)receiveObject());
                
                
                if(getManagers().isEmpty()) {
                    sendObject(registerTags((String)receiveObject()));
                    sendObject(addManager((Object[])receiveObject()));
                }

                loginScreen((String)receiveObject());   // receieves the action the user wants to do within the login screen
            }                
            
            else if(type.equals("Bartender")) {
                //getBartenders().add(getSocket());
            }
    
        } catch (IOException ex) {
            Logger.getLogger(Server.class.getName()).log(Level.SEVERE, null, ex);
        } catch (ClassNotFoundException ex) {
            Logger.getLogger(Server.class.getName()).log(Level.SEVERE, null, ex);
        } catch (Exception ex) {
            Logger.getLogger(Server.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    public void loginScreen(String action) throws IOException, ClassNotFoundException, Exception {
        //sendObject(" 1");
        if(action.equals("Sign Up")) {
            sendObject(addCustomer((Object[])receiveObject()));
            loginScreen((String)receiveObject());
        }
                
        else if(action.equals("Login")) {
            Account account = validateTagNumber((String)receiveObject());     // validate tag number
            
            if(account instanceof Customer) {
                sendObject("Customer");         // tells the kiosk the account is a customer
                sendObject((Customer)account);  // gives the kiosk the customer account info
                setLoggedIn(true);
                while(getLoggedIn() == true) {
                    sendObject(customerMethods((String)receiveObject()));
                }
                loginScreen((String)receiveObject());
            }
            
            else if(account instanceof Manager) {
                sendObject("Manager");         // tells the kiosk the account is a manager
                sendObject((Manager)account);  // gives the kiosk the manager account info
                setLoggedIn(true);
                while(getLoggedIn() == true) {
                    sendObject(managerMethods((String)receiveObject()));
                }
                loginScreen((String)receiveObject());
            }
            else {
                sendObject(null);
            }
        }
    }
    
    public Object customerMethods(String method) throws IOException, ClassNotFoundException {
        switch(method) {
            case "getDrinkMenu":
                //sendObject("1");
                return getDrinkMenu();
                
            case "addCustomer":
                return addCustomer((Object[])receiveObject());
                
            case "filterDrinkMenu":
                //sendObject("1");
                return filterDrinkMenu((String[])receiveObject());
                
            case "placeOrder":
                return placeOrder((Object[])receiveObject());
                
            case "Logout":
                Customer account = (Customer)receiveObject();
                //System.out.println(account.getTagNumber());
                System.out.println(account.getCart().size());
                for(int i = 0; i < getCustomers().size(); i++) {
                    //System.out.println(getCustomers().get(i).getTagNumber());
                    if(getCustomers().get(i).getTagNumber().equals(account.getTagNumber())) {
                        getCustomers().set(i, account);
                        //System.out.println("found account");
                        //System.out.println(getCustomers().get(i).getCart().size());
                        
                        break;
                    }
                }
                deleteFile("Customers", account.getTagNumber());
                createFile("Customers", account.getTagNumber(), account);
                setLoggedIn(false);
                return "Successfully Logged Out";
                
                
            default:
                return "Invalid Function Selected";
        }
    }
    
    public Object managerMethods(String method) throws IOException, ClassNotFoundException, Exception {
        switch(method) {
            case "getDrinkMenu":
                //sendObject("1");
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
                //System.out.println(getInitIngredients().size());
                
                String ingred = (String)receiveObject();
                Class ingredientClass = Class.forName("RB.Bartender." + ingred.replace(" ", ""));
                Method getBrands = ingredientClass.getMethod("getBrands");
                sendObject(getBrands.invoke(null));
                return addBottle((Object[])receiveObject());
                
            case "removeBottle":
                return removeBottle((int)receiveObject());
                
            default:
                return "Invalid Function Selected";
        }
    }
    
    public Object bartenderMethods(String method) {
        switch(method) {
            case "getDrinkMenu":
                return getDrinkMenu();
        
                
            default:
                return "Invalid Function Selected";
        }
    }
    
    

    
    public Account validateTagNumber(String tagNumber) {
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
            
        } catch (IOException | ClassNotFoundException i) {
            Logger.getLogger(Kiosk.class.getName()).log(Level.SEVERE, null, i);
        }
        
        return null;
    }
    
    public void initServer() throws Exception {
        
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
        
        for(int i = 0; i < ingredClasses.size(); i++) {
            Class ingredientClass = Class.forName(ingredClasses.get(i));
            
            Method initBrands = ingredientClass.getMethod("initBrands");
            initBrands.invoke(null);

            getInitIngredients().add(ingredientClass.toString().substring(19).replaceAll("(\\p{Ll})(\\p{Lu})","$1 $2"));
        }

        File man = new File("Managers");
        File[] listOfManagers = man.listFiles();
        for(File listOfManager : listOfManagers) {
            if(listOfManager.isFile() && listOfManager.getName().endsWith(".txt")) {
                try (FileInputStream file = new FileInputStream(listOfManager); ObjectInputStream fileInput = new ObjectInputStream(file)) {
                    getManagers().add((Manager)fileInput.readObject());
                }
            }
        }
        
        File cust = new File("Customers");
        File[] listOfCustomers = cust.listFiles();
        for(File listOfCustomer : listOfCustomers) {
            if(listOfCustomer.isFile() && listOfCustomer.getName().endsWith(".txt")) {
                try (FileInputStream file = new FileInputStream(listOfCustomer); ObjectInputStream fileInput = new ObjectInputStream(file)) {
                    getCustomers().add((Customer)fileInput.readObject());
                }
            }
        }
        
        File ingred = new File("Ingredients");
        File[] listOfIngred = ingred.listFiles();
        for(File listOfIngred1 : listOfIngred) {
            if(listOfIngred1.isFile() && listOfIngred1.getName().endsWith(".txt")) {
                try (FileInputStream file = new FileInputStream(listOfIngred1); ObjectInputStream fileInput = new ObjectInputStream(file)) {
                    getIngredients().add((Bottle)fileInput.readObject());
                }
            }
        }
        
        for(int i = 0; i < getIngredients().size(); i++) {
            updateDrinks("add", getIngredients().get(i).getIngredient().getType());
        }
        
        File tags = new File("RegisteredTags");
        File[] listOfTags = tags.listFiles();
        for(File listOfTag : listOfTags) {
            if(listOfTag.isFile() && listOfTag.getName().endsWith(".txt")) {
                try (FileInputStream file = new FileInputStream(listOfTag); ObjectInputStream fileInput = new ObjectInputStream(file)) { 
                    getRegisteredTags().add((String)fileInput.readObject()); 
                }
            }
        }
        
        File order = new File("Orders");
        File[] listOfOrders = order.listFiles();
        for(File listOfOrder : listOfOrders) {
            if(listOfOrder.isFile() && listOfOrder.getName().endsWith(".txt")) {
                try (FileInputStream file = new FileInputStream(listOfOrder); ObjectInputStream fileInput = new ObjectInputStream(file)) { 
                    getOrders().add((Order)fileInput.readObject());
                }
            }
        }
    }
        
    public String addBottle(Object[] info) throws Exception {
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
    
    public String removeBottle(int slot) throws Exception {
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
    
    
    public void updateDrinks(String action, String ingred) throws Exception {
        Class ingredientClass = Class.forName("RB.Bartender." + ingred.replace(" ", ""));
        
        Method initDrinks = ingredientClass.getMethod("initDrinks");
        initDrinks.invoke(null);
            
        Method getDrinks = ingredientClass.getMethod("getDrinks");
        ArrayList<Drink> drinks = (ArrayList<Drink>)getDrinks.invoke(null);
        
        if(action.equals("add")) {
            ArrayList<String> ingredientNames = new ArrayList<>();
            
            for(int i = 0; i < getIngredients().size(); i++) {
                ingredientNames.add(getIngredients().get(i).getIngredient().getType());
            }

            for(int i = 0; i < drinks.size(); i++) {
                List<String> ingredient = Arrays.asList(drinks.get(i).getIngredName());
                
                if(ingredientNames.containsAll(ingredient)) {
                    Ingredient[] ingredientBrands = new Ingredient[ingredient.size()];
                    double spiritAmount = 0;
                    
                    for(int j = 0; j < ingredient.size(); j++) {
                        int index = ingredientNames.indexOf(ingredient.get(j));
                        ingredientBrands[j] = getIngredients().get(index).getIngredient();
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
                    if(drinkMenu.get(i).getName().equals(drinks.get(j).getName()) && drinkMenu.contains(drinkMenu.get(i))) {
                        getDrinkMenu().remove(drinkMenu.get(i));
                    }
                }
            }
        }
        
       // getClients().

        //ThreadGroup clients = Thread.currentThread().getThreadGroup();
        //clients.interrupt();
        //System.out.println(getClients().activeCount());
        
        //sendObject("Update");
        //sendObject(getDrinkMenu());
        //System.out.println("updated drink menu sent....");
        //sendObject("");
        
        drinks.clear();
    }
    
    public ArrayList<Drink> filterDrinkMenu(String[] info) {
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
        }
        return null;
    }

    public String placeOrder(Object[] info) throws IOException {
        int number = 1;
        ArrayList<Drink> drinks = (ArrayList<Drink>)info[0];
        Customer customer = (Customer)info[1];
        ArrayList<String> ingredientNames = new ArrayList<>();
        
        for(int i = 0; i < drinks.size(); i++) {
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
        
        getOrders().add(new Order(number, drinks, customer));
        createFile("Orders", Integer.toString(number), getOrders().get(getOrders().size() - 1));
        return "Your Order Has Been Placed\nYour Order Number is " + number; 
    }
    
    public String addManager(Object[] info) throws IOException {
        String tagNumber = (String)info[0];
        String firstName = (String)info[1];
        String lastName = (String)info[2];
        
        if((firstName.isEmpty() && lastName.isEmpty()) == false) {
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
                return "The Tag Scanned Is Not A Registered Tag";
            }
        }
        else {
            return "Please Enter A First Name And A Last Name";
        }
    }

    public String addCustomer(Object[] info) throws IOException {
        String tagNumber = (String)info[0];
        String firstName = (String)info[1];
        String lastName = (String)info[2];
        LocalDate birthday = (LocalDate)info[3];
        String gender = (String)info[4];
        
        if((firstName.isEmpty() && lastName.isEmpty()) == false) {
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
            return "Please Enter A First Name And A Last Name";
        }
    }
    
    public String registerTags(String tagNumber) {
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
    public boolean getLoggedIn() {
        return loggedIn;
    }
    
    public void setLoggedIn(boolean loggedIn) {
        this.loggedIn = loggedIn;
    }
    
    public Socket getSocket() {
        return socket;
    }
    
    public void sendObject(Object obj) throws IOException {
        output.writeObject(obj);
    }
    
    public Object receiveObject() throws IOException, ClassNotFoundException {
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
                    new Thread(clients, new Server(socket)).start();
                }
                
            case "k": 
                Socket socket = new Socket(getDomainName(), getPort()); 
                System.out.println("Connecting To " + getDomainName() + " On Port " + getPort());
                
                //getClients().add(new Thread(new Kiosk(socket)));
                //getClients().get(getClients().size() - 1).start();
                
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
