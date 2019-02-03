package RB.GUI;

import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;
import javafx.event.ActionEvent;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;
import RB.Bartender.*;
import javafx.fxml.FXML;
import javafx.scene.control.TextField;

/**
 *
 * @authors Anthony Spiteri
 *          Cristian Nuosci
 *          Shahezad Kassam
 */

public class LoginScreenController {
    @FXML private TextField tagNumber;
    
    public void backButtonWasPushed(ActionEvent event) throws IOException {
        Kiosk.getOrderOfWindows().remove(Kiosk.getOrderOfWindows().size() - 1);
        Parent windowParent = FXMLLoader.load(getClass().getResource(Kiosk.getOrderOfWindows().get(Kiosk.getOrderOfWindows().size() - 1)));
        Scene screen = new Scene(windowParent);
        
        Stage window = (Stage)((Node)event.getSource()).getScene().getWindow();
        window.setScene(screen);
        window.setMaximized(true);
        window.show();
    }
    
    public void signUpButtonWasPushed(ActionEvent event) throws Exception
    {
        Parent tableViewParent = FXMLLoader.load(getClass().getResource("/RB/GUI/SignUpPage.fxml"));
        Scene tableViewScene = new Scene(tableViewParent);
        Kiosk.getOrderOfWindows().add("/RB/GUI/SignUpPage.fxml");
        
        //This line gets the Stage information
        Stage window = (Stage)((Node)event.getSource()).getScene().getWindow();
        window.setScene(tableViewScene);
        window.show();
    }
    
    public void loginButtonWasPushed(ActionEvent event) throws Exception {
        Kiosk.sendObject("Login");
        Kiosk.sendObject(tagNumber.getText());    // tag is scanned and the tag number is sent to verify
        String accountType = (String)Kiosk.receiveObject();
        
        if(accountType.equals("Customer")) {
            Kiosk.setCustomer((Customer)Kiosk.receiveObject());
            Parent windowParent = FXMLLoader.load(getClass().getResource("/RB/GUI/CustomerMenu.fxml"));
            Scene screen = new Scene(windowParent);
            Kiosk.getOrderOfWindows().add("/RB/GUI/CustomerMenu.fxml");
            
            Stage window = (Stage)((Node)event.getSource()).getScene().getWindow();
            window.setScene(screen);
            window.show();
        }
        else if(accountType.equals("Manager")) {
            Kiosk.setManager((Manager)Kiosk.receiveObject());
            Parent windowParent = FXMLLoader.load(getClass().getResource("/RB/GUI/ManagerMenu.fxml"));
            Scene screen = new Scene(windowParent);
            Kiosk.getOrderOfWindows().add("/RB/GUI/ManagerMenu.fxml");
            
            Stage window = (Stage)((Node)event.getSource()).getScene().getWindow();
            window.setScene(screen);
            window.setMaximized(true);
            window.show();
        } 
    }
}