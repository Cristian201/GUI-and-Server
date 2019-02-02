package RB.GUI;

import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Label;
import javafx.scene.image.ImageView;
import javafx.stage.Stage;
import RB.Bartender.*;

/**
 *
 * @authors Anthony Spiteri
 *          Cristian Nuosci
 *          Shahezad Kassam
 */

public class ViewDrinkScreenController {
    
    private Drink selectedDrink;
    
    @FXML private Label nameLabel;
    @FXML private Label glassLabel;
    @FXML private Label iceLabel;
    @FXML private Label shakeLabel;
    @FXML private Label carbonatedLabel;
    @FXML private Label strengthLabel;
    @FXML private Label typeLabel;
    @FXML private Label priceLabel;
    @FXML private ImageView image;
    
    
    public void initData(Drink drink) {
        selectedDrink = drink;
        nameLabel.setText(selectedDrink.getName());
        glassLabel.setText(selectedDrink.getGlass().getName());
        iceLabel.setText(Drink.boolToString(selectedDrink.getIce()));
        shakeLabel.setText(Drink.boolToString(selectedDrink.getShake()));
        strengthLabel.setText(selectedDrink.getStrength());
        typeLabel.setText(selectedDrink.getType());
        priceLabel.setText(selectedDrink.getPrice().toString());
        image.setImage(selectedDrink.getImage());
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
    
    public void addToOrderButtonWasPushed(ActionEvent event) throws IOException
    {
        FXMLLoader loader = new FXMLLoader();
        loader.setLocation(getClass().getResource("/RB/GUI/ViewOrderScreen.fxml"));
        Parent tableViewParent = loader.load();
        Scene tableViewScene = new Scene(tableViewParent);
        
        ViewOrderScreenController controller = loader.getController();
        controller.initData(selectedDrink);
        
        //This line gets the Stage information
        Stage window = (Stage)((Node)event.getSource()).getScene().getWindow();
        window.setScene(tableViewScene);
        window.show();
    } 
}
