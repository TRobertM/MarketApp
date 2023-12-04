package controllers;

import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.Pane;
import javafx.scene.text.Text;
import javafx.scene.text.TextFlow;
import javafx.stage.Stage;
import services.ConnectionService;
import services.DatabaseDataService;
import services.FXMLChangerService;

import javax.xml.crypto.Data;
import java.io.IOException;
import java.net.URL;
import java.sql.*;
import java.util.ResourceBundle;

public class developerWelcomeController extends BaseController implements Initializable {

    @FXML
    Label welcomeLabel;
    @FXML
    Pane avatarPane;
    @FXML
    TextFlow myGames;
    @FXML
    TextFlow myOrders;
    @FXML
    TextFlow addGame;
    @FXML
    Button logoutButton;
    @FXML
    Pane notificationPane;

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        welcomeLabel.setText("Welcome, " + this.getCurrentUser());
        try {
            int orders = DatabaseDataService.retrieveNumberOfOrders(BaseController.getCurrentUser());
            if(DatabaseDataService.retrieveNumberOfOrders(BaseController.getCurrentUser()) != 0){
                Button notificationButton = new Button(String.valueOf(orders));
                notificationButton.setStyle("-fx-background-color: linear-gradient(to right bottom, #c33a9a, #d74d54);-fx-background-radius: 50px; -fx-text-fill: white");
                notificationPane.getChildren().add(notificationButton);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    // Works as a logout button
    @FXML
    public void goBack() throws IOException {
        FXMLChangerService.changeScene("scene.fxml", logoutButton);
    }

    // Changes window to developer library window
    public void goToGames(MouseEvent e) throws IOException {
        FXMLChangerService.changeSceneWithData("developerGames.fxml", myGames, this.getCurrentUser());
    }

    public void addGame(MouseEvent e) throws IOException {
        FXMLChangerService.changeScene("developerAdd.fxml", addGame);
    }

    public void goToOrders(MouseEvent e) throws IOException {
        FXMLChangerService.changeScene("developerOrders.fxml", myOrders);
    }

    // Everything below this is implemented only for design purposes
    public void myGamesHover(){
        ((Text)myGames.getChildren().get(1)).setText(" G A M E S >");
    }

    public void myGamesExit(){
        ((Text)myGames.getChildren().get(1)).setText(" G A M E S");
    }

    public void addGameHover(){
        ((Text)addGame.getChildren().get(1)).setText(" G A M E >");
    }

    public void addGameExit(){
        ((Text)addGame.getChildren().get(1)).setText(" G A M E");
    }

    public void myOrdersHover(){
        ((Text)myOrders.getChildren().get(1)).setText(" O R D E R S >");
    }

    public void myOrdersExit(){
        ((Text)myOrders.getChildren().get(1)).setText(" O R D E R S");
    }
}
