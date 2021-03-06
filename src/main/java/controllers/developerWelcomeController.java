package controllers;

import javafx.event.ActionEvent;
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
import model.Developer;
import model.Order;
import services.DeveloperService;

import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;

public class developerWelcomeController implements Initializable {

    @FXML
    Button xButton;
    @FXML
    Button minimizeButton;
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
    Developer currentDeveloper;

    // Again no use as of right now but DO NOT TOUCH
    @Override
    public void initialize(URL location, ResourceBundle resources) {
    }

    public void closeWindow(){
        Stage stage = (Stage) xButton.getScene().getWindow();
        stage.close();
    }

    public void minimizeWindow(){
        Stage stage = (Stage) minimizeButton.getScene().getWindow();
        stage.setIconified(true);
    }

    // Works as a logout button
    @FXML
    public void goBack() throws IOException {
        Parent root = FXMLLoader.load((getClass().getClassLoader().getResource("scene.fxml")));
        Stage stage = (Stage)logoutButton.getScene().getWindow();
        Scene scene = new Scene(root);
        stage.setScene(scene);
        stage.setResizable(false);
    }

    // Changes window to developer library window
    public void goToGames(MouseEvent e) throws IOException {
        FXMLLoader loader = new FXMLLoader(getClass().getClassLoader().getResource("developerGames.fxml"));
        Parent root = loader.load();
        developerGamesController w1 = loader.getController();
        w1.setDevName(currentDeveloper.getUsername());
        Stage stage = (Stage) ((Node) e.getSource()).getScene().getWindow();
        Scene scene = new Scene(root);
        stage.setScene(scene);
        stage.setResizable(false);
    }

    public void addGame(MouseEvent e) throws IOException {
        FXMLLoader loader = new FXMLLoader(getClass().getClassLoader().getResource("developerAdd.fxml"));
        Parent root = loader.load();
        developerAddController a1 = loader.getController();
        a1.setDev(currentDeveloper);
        Stage stage = (Stage) ((Node) e.getSource()).getScene().getWindow();
        Scene scene = new Scene(root);
        stage.setScene(scene);
        stage.setResizable(false);
        stage.show();
    }

    public void goToOrders(MouseEvent e) throws IOException {
        FXMLLoader loader = new FXMLLoader(getClass().getClassLoader().getResource("developerOrders.fxml"));
        Parent root = loader.load();
        developerOrdersController a2 = loader.getController();
        a2.setDev(currentDeveloper.getUsername());
        Stage stage = (Stage) ((Node) e.getSource()).getScene().getWindow();
        Scene scene = new Scene(root);
        stage.setScene(scene);
        stage.setResizable(false);
        stage.show();
    }

    // Used by previous controller to send information to this controller
    public void setCurrentDeveloper(Developer developer){
        currentDeveloper = new Developer(developer);
        welcomeLabel.setText("Welcome, " + currentDeveloper.getUsername());
        if(!(currentDeveloper.getOrders().isEmpty())){
            int orders = 0;
            for(Order order : currentDeveloper.getOrders()){
                orders++;
            }
            Button notificationButton = new Button(String.valueOf(orders));
            notificationButton.setStyle("-fx-background-color: linear-gradient(to right bottom, #c33a9a, #d74d54);-fx-background-radius: 50px; -fx-text-fill: white");
            notificationPane.getChildren().add(notificationButton);
        }
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
