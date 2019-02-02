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

/**
 *
 * @authors Anthony Spiteri
 *          Cristian Nuosci
 *          Shahezad Kassam
 */

public class CustomerMenuController {
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
    
    public void drinksMenuButtonWasPushed(ActionEvent event) throws IOException {
        Parent windowParent = FXMLLoader.load(getClass().getResource("/RB/GUI/DrinksMenu.fxml"));
        Scene screen = new Scene(windowParent);
        Kiosk.getOrderOfWindows().add("/RB/GUI/DrinksMenu.fxml");

        Stage window = (Stage)((Node)event.getSource()).getScene().getWindow();
        window.setScene(screen);
        window.setMaximized(true);
        window.show();
    }    
}