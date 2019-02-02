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
import javafx.scene.control.Label;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.stage.Stage;
import RB.Bartender.*;

/**
 *
 * @authors Anthony Spiteri
 *          Cristian Nuosci
 *          Shahezad Kassam
 */

public class ViewOrderScreenController implements Initializable {

    private Drink selectedDrink;
    
    @FXML private TableView<Drink> viewOrderTable;
    @FXML private Label cost;
    @FXML private Button checkoutButton;
    @FXML private TableColumn<Drink, String> nameColumn;
    @FXML private TableColumn<Drink, String> priceColumn;
    
    public void logoutButtonWasPushed(ActionEvent event) throws IOException
    {
        Parent tableViewParent = FXMLLoader.load(getClass().getResource("/RB/GUI/IdleScreen.fxml"));
        Scene tableViewScene = new Scene(tableViewParent);
        
        //This line gets the Stage information
        Stage window = (Stage)((Node)event.getSource()).getScene().getWindow();
        window.setScene(tableViewScene);
        window.show();
    }
    
    public void backButtonWasPushed(ActionEvent event) throws IOException
    {
        Parent tableViewParent = FXMLLoader.load(getClass().getResource("/RB/GUI/ustomerMenu.fxml"));
        Scene tableViewScene = new Scene(tableViewParent);
        
        //This line gets the Stage information
        Stage window = (Stage)((Node)event.getSource()).getScene().getWindow();
        window.setScene(tableViewScene);
        window.show();
    }
    
    public void initData(Drink drink)
    {
        selectedDrink = drink;
        cost.setText(selectedDrink.getPrice().toString());
        
    }
    
    public ObservableList<Drink> fillTable()
    {
        ObservableList<Drink> drinklist = FXCollections.observableArrayList();
        drinklist.add(selectedDrink);
        
        return drinklist;
    }
    
    @Override
    public void initialize(URL url, ResourceBundle rb)
    {
       
        nameColumn.setCellValueFactory(new PropertyValueFactory<Drink, String>("Name"));
        priceColumn.setCellValueFactory(new PropertyValueFactory<Drink, String>("Price"));
        
        viewOrderTable.setItems(fillTable());
        
    }    
}
