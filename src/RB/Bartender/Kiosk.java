package RB.Bartender;

import java.awt.Toolkit;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.Serializable;
import static java.lang.System.out;
import java.net.Socket;
import java.net.URL;
import java.time.LocalDate;
import java.time.Period;
import java.util.ArrayList;
import java.util.List;
import java.util.ResourceBundle;
import java.util.TreeSet;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.SynchronousQueue;
import java.util.logging.Level;
import java.util.logging.Logger;
import javafx.application.Application;
import static javafx.application.Application.launch;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Label;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.image.ImageView;
import javafx.stage.Screen;
import javafx.stage.Stage;

/**
 *
 * @authors Anthony Spiteri
 *          Cristian Nuosci
 *          Shahezad Kassam
 */

public class Kiosk extends Application implements Runnable, Serializable {
    private Socket socket;
    private static ObjectOutputStream output;
    private static ObjectInputStream input;
    private final int kioskNumber = 1;
    private static Customer customer;
    private static Manager manager;
    private static ArrayList<Drink> drinkMenu = new ArrayList<>();
    private static SynchronousQueue queue = new SynchronousQueue();  
    private static ArrayList<String> orderOfWindows = new ArrayList<>();
    private final double[] screenSize = {Toolkit.getDefaultToolkit().getScreenSize().getWidth(), Toolkit.getDefaultToolkit().getScreenSize().getHeight()};
    
    @FXML private TextField tagNumber;


    public Kiosk() {
        
    }

    public Kiosk(Socket socket) throws IOException {
        this.socket = socket;
        this.output = new ObjectOutputStream(getSocket().getOutputStream());
        this.input = new ObjectInputStream(getSocket().getInputStream());
    }
    
    @Override
    public void start(Stage primaryStage) throws Exception {
        Parent windowParent = FXMLLoader.load(getClass().getResource("/RB/GUI/IdleScreen.fxml"));
        Scene scene = new Scene(windowParent);
        getOrderOfWindows().add("/RB/GUI/IdleScreen.fxml");
        
        primaryStage.setScene(scene);
        primaryStage.setTitle("Robotic Bartender");
        
        Screen screen = Screen.getPrimary();
        javafx.geometry.Rectangle2D bounds = screen.getVisualBounds();
        primaryStage.setWidth(bounds.getWidth());
        primaryStage.setHeight(bounds.getHeight());
        
        primaryStage.show();
        primaryStage.setMaximized(true);
    }

    @Override
    public void run() {
        try {
            System.out.println("Kiosk Connected");
            sendObject("Kiosk");    // notifies the server that a kiosk is connected
            createUpdateThread();
            launch("");
             
            /*
            sendObject("1");
            System.out.println(receiveObject());
            sendObject(new Object[] {"1", "John", "Smith"});
            System.out.println(receiveObject());
            */
            
            //login("1");

            //managerFunctions();
            
            //logout();
            //socket.close();
            
        } catch (IOException ex) {
            Logger.getLogger(Kiosk.class.getName()).log(Level.SEVERE, null, ex);
        }
    }


    public void managerFunctions() throws IOException, ClassNotFoundException, InterruptedException {
  
        sendObject("Sign Up");  // notifies the server that user wants to create a new account
            
        sendObject(new Object[] {"2", "Anthony", "Spiteri", LocalDate.of(1997, 9, 11), "Male"});
        System.out.println(receiveObject());
        login("2");
        
    }
    
    public String login(String tagNumber) throws IOException, ClassNotFoundException, InterruptedException {
        sendObject("Login");
        sendObject(tagNumber);    // tag is scanned and the tag number is sent to verify
        return (String)receiveObject();
      
        
        /*
        switch (type) {
            case "Customer":
                setCustomer((Customer)receiveObject());
                
                sendObject("getDrinkMenu");
                sendObject(getCustomer());
                setDrinkMenu((TreeSet<Drink>)receiveObject());
                System.out.println(getDrinkMenu().size());

                
                sendObject("filterDrinkMenu");
                ArrayList<String> ingred = (ArrayList<String>)receiveObject();
                for(int i = 0; i < ingred.size(); i++)
                    System.out.println(ingred.get(i));
                sendObject(new String[] {"Price", "High to Low"});
                setDrinkMenu((ArrayList<Drink>)receiveObject());
                for(int i = 0; i < getDrinkMenu().size(); i++)
                    System.out.println(getDrinkMenu().get(i).getName()+"  "+getDrinkMenu().get(i).getPrice());
                
                
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
    */
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
    
    public static void logout() throws IOException, ClassNotFoundException, InterruptedException {
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
    
    public static void sendObject(Object obj) throws IOException {
        output.writeObject(obj);
    }
    
    public static Object receiveObject() throws IOException, ClassNotFoundException, InterruptedException {
        return queue.take();
    }  
    
    public static Customer getCustomer() {
        return customer;
    }
    
    public static void setCustomer(Customer customer) {
        Kiosk.customer = customer;
    }
    
    public static Manager getManager() {
        return manager;
    }
    
    public static void setManager(Manager manager) {
        Kiosk.manager = manager;
    }
    
    public static ArrayList<Drink> getDrinkMenu() {
        return drinkMenu;
    }
    
    public static ObservableList<Drink> getObservableDrinkMenu() {
        return FXCollections.observableArrayList(drinkMenu);
    }
    
    public static void setDrinkMenu(TreeSet<Drink> drinks) {
        drinkMenu.clear();
        drinkMenu = new ArrayList<>(drinks);
    }
    
    public static void setDrinkMenu(ArrayList<Drink> drinks) {
        drinkMenu.clear();
        drinkMenu = new ArrayList<>(drinks);
    }
    
    public static ArrayList<String> getOrderOfWindows() {
        return orderOfWindows;
    }
    
    private double[] getScreenSize() {
        return screenSize;
    }
 
    public void createUpdateThread() {
        new Thread(() -> {
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
        }).start();
    }
    
    // GUI SHIT ------------------------------------------------------
    /*
    public void backButtonWasPushed(ActionEvent event) throws IOException {
        //getOrderOfWindows().remove(getOrderOfWindows().size() - 1);
        //setScreen(getOrderOfWindows().get(getOrderOfWindows().size() - 1), event);
    
        Kiosk.getOrderOfWindows().remove(Kiosk.getOrderOfWindows().size() - 1);
        Parent windowParent = FXMLLoader.load(getClass().getResource(Kiosk.getOrderOfWindows().get(Kiosk.getOrderOfWindows().size() - 1)));
        Scene screen = new Scene(windowParent);
        
        Stage window = (Stage)((Node)event.getSource()).getScene().getWindow();
        window.setScene(screen);
        window.setMaximized(true);
        window.show();
        
    }
    
    // called from IdleScreen
    public void startupButtonWasPushed(ActionEvent event) throws IOException {
        Parent windowParent = FXMLLoader.load(getClass().getResource("/RB/GUI/LoginScreen.fxml"));
        Scene screen = new Scene(windowParent);
        getOrderOfWindows().add("/RB/GUI/LoginScreen.fxml");

        Stage window = (Stage)((Node)event.getSource()).getScene().getWindow();
        window.setScene(screen);
        window.setMaximized(true);
        window.show();
    }
    
    // called from LoginScreen
    public void loginButtonWasPushed(ActionEvent event) throws IOException, InterruptedException, ClassNotFoundException {
  
        System.out.println("tagNumber: " + tagNumber.getText());

        sendObject("Login");
        sendObject(tagNumber.getText());    // tag is scanned and the tag number is sent to verify
        String accountType = (String)receiveObject();
        
        if(accountType.equals("Customer")) {
            setCustomer((Customer)receiveObject());
            setScreen("/RB/GUI/CustomerMenu.fxml", event);
        }
        else if(accountType.equals("Manager")) {
            setManager((Manager)receiveObject());
            setScreen("/RB/GUI/ManagerMenu.fxml", event);
        }
    } 

    public void logOutButtonWasPushed(ActionEvent event) throws IOException, ClassNotFoundException, InterruptedException {
        Kiosk.logout();
        Kiosk.getOrderOfWindows().clear();
        setScreen("/RB/GUI/IdleScreen.fxml", event);
        
    }
    
    public void viewOrderButtonWasPushed(ActionEvent event) throws IOException {        
        setScreen("/RB/GUI/ViewOrderScreen.fxml", event);
    }
    
    
    @FXML private ComboBox ingredientBox;
    @FXML private ComboBox strengthBox;
    @FXML private ComboBox typeBox;
    
    
    @FXML private TableView<Drink> drinksTable;
    @FXML private Button viewDrinkButton;
    @FXML private ImageView images;
    @FXML private TableColumn<Drink, String> nameColumn;
    @FXML private TableColumn<Drink, String> priceColumn;
    
    
    // called in CustomerMenu
    public void drinksMenuButtonWasPushed(ActionEvent event) throws Exception {
        //sendObject("getDrinkMenu");
        //sendObject(getCustomer());
        

        //ingredientBox.getItems().addAll((ArrayList<String>)receiveObject());
        //ingredientBox.getItems().addAll("Weak", "Medium", "Strong");
        //strengthBox.getItems().addAll("Weak", "Medium", "Strong");
        //typeBox.getItems().addAll("Cocktail", "Martini", "Mixed Drinks", "On The Rocks", "Neat", "Shot");
        
        List<String> list = new ArrayList<String>();
        list.add("Item A");
        list.add("Item B");
        list.add("Item C");
        ObservableList obList = FXCollections.observableList(list);
        
        
        Parent windowParent = FXMLLoader.load(getClass().getResource("/RB/GUI/DrinksMenu.fxml"));
        Scene screen = new Scene(windowParent);
        
        //ingredientBox.setItems(obList);
        //strengthBox.setItems(obList);
        //typeBox.setItems(obList);
        
        ingredientBox.getItems().addAll("Weak", "Medium", "Strong");
        strengthBox.getItems().addAll("Weak", "Medium", "Strong");
        typeBox.getItems().addAll("Cocktail", "Martini", "Mixed Drinks", "On The Rocks", "Neat", "Shot");
        
        getOrderOfWindows().add("/RB/GUI/DrinksMenu.fxml");

        Stage window = (Stage)((Node)event.getSource()).getScene().getWindow();
        window.setScene(screen);
        window.setWidth(getScreenSize()[0]);
        window.setHeight(getScreenSize()[1]);
        
        
        
        
        
        
        
        //nameColumn.setCellValueFactory(new PropertyValueFactory<>("Name"));
        //priceColumn.setCellValueFactory(new PropertyValueFactory<>("Price"));
        viewDrinkButton.setDisable(true);  

        
        //setDrinkMenu((TreeSet<Drink>)receiveObject());
        window.show();        
        //setScreen("/RB/GUI/DrinksMenu.fxml", event);
        
        
    }
    
    
    
    
    
    // called in DrinksMenu
    public void viewDrinkButtonWasPushed(ActionEvent event) throws IOException {
        /*FXMLLoader loader = new FXMLLoader();
        loader.setLocation(getClass().getResource("ViewDrinkScreen.fxml"));
        windowParent = loader.load();
        screen = new Scene(windowParent);
        getOrderOfWindows().add("ViewDrinkScreen.fxml");
        ViewDrinkScreenController controller = loader.getController();
        controller.initData(drinksTable.getSelectionModel().getSelectedItem());
        window = (Stage)((Node)event.getSource()).getScene().getWindow();
        window.setScene(screen); 
        window.setWidth(getScreenSize()[0]);
        window.setHeight(getScreenSize()[1]);
        window.show();  
    }
    
    // called in DrinksMenu
    public void ingredientBoxWasClicked() {
        images.setImage(null);
        this.viewDrinkButton.setDisable(true); 
    }
        
    // called in DrinksMenu
    public void ingredientBoxWasUpdated() throws Exception {    
        strengthBox.valueProperty().set(" ");
        typeBox.valueProperty().set(" ");        
        
        if(null != ingredientBox.getValue().toString()) {
            sendObject("filterDrinkMenu");
            sendObject(new String[] {"Ingredient", ingredientBox.getValue().toString()});
            setDrinkMenu((ArrayList<Drink>)receiveObject());            
            drinksTable.setItems(FXCollections.observableArrayList(getDrinkMenu()));  
        }
    }
    
    // called in DrinksMenu
    public void strengthBoxWasClicked() {
        images.setImage(null);
        this.viewDrinkButton.setDisable(true); 
    }
    
    // called in DrinksMenu
    public void strengthBoxWasUpdated() throws Exception {      
        ingredientBox.valueProperty().set(" ");
        typeBox.valueProperty().set(" ");    
        
        if(null != strengthBox.getValue().toString()) {
            sendObject("filterDrinkMenu");
            sendObject(new String[] {"Strength", strengthBox.getValue().toString()});
            setDrinkMenu((ArrayList<Drink>)receiveObject());            
            drinksTable.setItems(FXCollections.observableArrayList(getDrinkMenu())); 
        }
    }
    
    // called in DrinksMenu
    public void typeBoxWasClicked() {
        images.setImage(null);
        this.viewDrinkButton.setDisable(true);
    }
    
    // called in DrinksMenu
    public void typeBoxWasUpdated() throws Exception {            
        strengthBox.valueProperty().set(" ");
        ingredientBox.valueProperty().set(" "); 
        
        if(null != typeBox.getValue().toString()) {
            sendObject("filterDrinkMenu");
            sendObject(new String[] {"Type", typeBox.getValue().toString()});
            setDrinkMenu((ArrayList<Drink>)receiveObject());            
            drinksTable.setItems(FXCollections.observableArrayList(getDrinkMenu())); 
        }
    }
    
    //NEED TO ADD MOST POPUPAR FILTER OPTION AND PRICE METHODS
    
    // called in DrinksMenu
    public void userClickedOnTable() {
        this.viewDrinkButton.setDisable(false);  
    }
    
    
    
    
    public void setScreen(String screenName, ActionEvent event) throws IOException {
        Parent windowParent = FXMLLoader.load(getClass().getResource(screenName));
        Scene screen = new Scene(windowParent);
        Kiosk.getOrderOfWindows().add(screenName);

        Stage window = (Stage)((Node)event.getSource()).getScene().getWindow();
        window.setScene(screen);
        window.setMaximized(true);
        window.show();
    }
    
   */
    
    
    
    
    
    
    
}
