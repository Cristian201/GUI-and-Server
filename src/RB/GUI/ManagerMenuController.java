package RB.GUI;

import RB.Bartender.Kiosk;
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

/**
 * FXML Controller class
 *
 * @author crist
 */
public class ManagerMenuController implements Initializable {

    public void logOutButtonWasPushed(ActionEvent event) throws Exception
    {
        Kiosk.logout();
        Kiosk.getOrderOfWindows().clear();
        
        Parent tableViewParent = FXMLLoader.load(getClass().getResource("/RB/GUI/IdleScreen.fxml"));
        Scene tableViewScene = new Scene(tableViewParent);
        
        //This line gets the Stage information
        Stage window = (Stage)((Node)event.getSource()).getScene().getWindow();
        window.setScene(tableViewScene);
        window.show();
    }
    
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        // TODO
    }    
    
}
