package RB.GUI;

import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;
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
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.stage.Stage;
import RB.Bartender.*;
import java.util.ArrayList;
import java.util.TreeSet;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @authors Anthony Spiteri
 *          Cristian Nuosci
 *          Shahezad Kassam
 */

public class DrinksMenuController implements Initializable {

    @FXML private ComboBox ingredientBox;
    @FXML private ComboBox strengthBox;
    @FXML private ComboBox typeBox;
    
    @FXML private TableView<Drink> drinksTable;
    @FXML private Button viewDrinkButton;
    @FXML private ImageView images;
    @FXML private TableColumn<Drink, String> nameColumn;
    @FXML private TableColumn<Drink, String> priceColumn;
    
    public void backButtonWasPushed(ActionEvent event) throws IOException {
        Kiosk.getOrderOfWindows().remove(Kiosk.getOrderOfWindows().size() - 1);
        Parent windowParent = FXMLLoader.load(getClass().getResource(Kiosk.getOrderOfWindows().get(Kiosk.getOrderOfWindows().size() - 1)));
        Scene screen = new Scene(windowParent);
        
        Stage window = (Stage)((Node)event.getSource()).getScene().getWindow();
        window.setScene(screen);
        window.setMaximized(true);
        window.show();
    }
    
    public void viewOrderButtonWasPushed(ActionEvent event) throws IOException {
        Parent windowParent = FXMLLoader.load(getClass().getResource("/RB/GUI/ViewOrderScreen.fxml"));
        Scene screen = new Scene(windowParent);
        Kiosk.getOrderOfWindows().add("/RB/GUI/ViewOrderScreen.fxml");

        Stage window = (Stage)((Node)event.getSource()).getScene().getWindow();
        window.setScene(screen);
        window.setMaximized(true);
        window.show();
    }
    
    public void logOutButtonWasPushed(ActionEvent event) throws Exception {
        Kiosk.logout();
        Kiosk.getOrderOfWindows().clear();
        
        Parent windowParent = FXMLLoader.load(getClass().getResource("/RB/GUI/IdleScreen.fxml"));
        Scene screen = new Scene(windowParent);
        Kiosk.getOrderOfWindows().add("/RB/GUI/IdleScreen.fxml");

        Stage window = (Stage)((Node)event.getSource()).getScene().getWindow();
        window.setScene(screen);
        window.setMaximized(true);
        window.show();
    }
    
    public void viewDrinkButtonWasPushed(ActionEvent event) throws IOException {
        FXMLLoader loader = new FXMLLoader();
        loader.setLocation(getClass().getResource("/RB/GUI/ViewDrinkScreen.fxml"));
        Parent windowParent = loader.load();
        Scene screen = new Scene(windowParent);
        Kiosk.getOrderOfWindows().add("/RB/GUI/ViewDrinkScreen.fxml");
        
        ViewDrinkScreenController controller = loader.getController();
        controller.initData(drinksTable.getSelectionModel().getSelectedItem());
        
        Stage window = (Stage)((Node)event.getSource()).getScene().getWindow();
        window.setScene(screen);
        window.setMaximized(true);
        window.show();
    }
   
    public void ingredientBoxWasUpdated() throws Exception {    
        strengthBox.valueProperty().set(" ");
        typeBox.valueProperty().set(" ");        
        
        if(null != ingredientBox.getValue().toString()) {
            Kiosk.sendObject("filterDrinkMenu");
            Kiosk.sendObject(new String[] {"Ingredient", ingredientBox.getValue().toString()});
            Kiosk.setDrinkMenu((ArrayList<Drink>)Kiosk.receiveObject());            
            drinksTable.setItems(Kiosk.getObservableDrinkMenu());  
        }
    }
    
    public void ingredientBoxWasClicked() {
        images.setImage(null);
        this.viewDrinkButton.setDisable(true); 
    }
    
    public void strengthBoxWasUpdated() throws Exception {      
        ingredientBox.valueProperty().set(" ");
        typeBox.valueProperty().set(" ");        
        
        if(null != strengthBox.getValue().toString()) {
            Kiosk.sendObject("filterDrinkMenu");
            Kiosk.sendObject(new String[] {"Strength", strengthBox.getValue().toString()});
            Kiosk.setDrinkMenu((ArrayList<Drink>)Kiosk.receiveObject());            
            drinksTable.setItems(Kiosk.getObservableDrinkMenu()); 
        }
    }
    
    public void strengthBoxWasClicked() {
        images.setImage(null);
        this.viewDrinkButton.setDisable(true); 
    }
    
    public void typeBoxWasUpdated() throws Exception {            
        strengthBox.valueProperty().set(" ");
        ingredientBox.valueProperty().set(" "); 
        
        if(null != typeBox.getValue().toString()) {
            Kiosk.sendObject("filterDrinkMenu");
            Kiosk.sendObject(new String[] {"Type", typeBox.getValue().toString()});
            Kiosk.setDrinkMenu((ArrayList<Drink>)Kiosk.receiveObject());            
            drinksTable.setItems(Kiosk.getObservableDrinkMenu()); 
        }
    }
    
    public void typeBoxWasClicked() {
        images.setImage(null);
        this.viewDrinkButton.setDisable(true);
    }
    
    public void userClickedOnTable() {
        this.viewDrinkButton.setDisable(false);
        images.setImage(drinksTable.getSelectionModel().getSelectedItem().getImage());  
    }
    
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        try {
            nameColumn.setCellValueFactory(new PropertyValueFactory<>("Name"));
            priceColumn.setCellValueFactory(new PropertyValueFactory<>("Price"));
            viewDrinkButton.setDisable(true);

            Kiosk.sendObject("getDrinkMenu");
            Kiosk.sendObject(Kiosk.getCustomer());
            
            ArrayList<String> ingredients = new ArrayList<>((ArrayList<String>)Kiosk.receiveObject());
            ingredientBox.getItems().addAll(ingredients);
            ArrayList<String> strength = new ArrayList<>((TreeSet<String>)Kiosk.receiveObject());
            strengthBox.getItems().addAll(strength);      
            ArrayList<String> type = new ArrayList<>((TreeSet<String>)Kiosk.receiveObject());
            typeBox.getItems().addAll(type);

            Kiosk.setDrinkMenu((ArrayList<Drink>)Kiosk.receiveObject());
            drinksTable.setItems(Kiosk.getObservableDrinkMenu());           
        } catch (IOException | ClassNotFoundException | InterruptedException ex) {
            Logger.getLogger(DrinksMenuController.class.getName()).log(Level.SEVERE, null, ex);
        }
    }    
}