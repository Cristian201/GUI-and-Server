package RB.GUI;

import RB.Bartender.Kiosk;
import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;
import javafx.animation.Interpolator;
import javafx.animation.KeyFrame;
import javafx.animation.KeyValue;
import javafx.animation.Timeline;
import javafx.event.ActionEvent;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;
import javafx.util.Duration;

/**
 *
 * @authors Anthony Spiteri
 *          Cristian Nuosci
 *          Shahezad Kassam
 */

public class IdleScreenController {
    public void startupButtonWasPushed(ActionEvent event) throws IOException {
        Parent windowParent = FXMLLoader.load(getClass().getResource("/RB/GUI/LoginScreen.fxml"));
        Scene screen = new Scene(windowParent);
        Kiosk.getOrderOfWindows().add("/RB/GUI/LoginScreen.fxml");

        Stage window = (Stage)((Node)event.getSource()).getScene().getWindow();
        window.setScene(screen);
        window.setMaximized(true);
        window.show();
    }
}
