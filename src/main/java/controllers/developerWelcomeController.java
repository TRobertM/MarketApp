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
import services.ConnectionService;
import services.DeveloperService;

import java.io.IOException;
import java.net.URL;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.Statement;
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
    String currentDeveloper;

    // Again no use as of right now but DO NOT TOUCH
    @Override
    public void initialize(URL location, ResourceBundle resources) {
    }

    public void setCurrentDeveloper(String developer){
        currentDeveloper = developer;
        welcomeLabel.setText("Welcome, " + currentDeveloper);
        int totalOrders = 0;
        try {
            Connection con = ConnectionService.Connect();
            Statement check_orders = con.createStatement();
            ResultSet orders_information = check_orders.executeQuery("SELECT id FROM orders WHERE seller = '" + currentDeveloper + "'");
            while(orders_information.next()){
                totalOrders++;
            }
            con.close();
            check_orders.close();
        } catch (Exception e){
            e.printStackTrace();
        }
        if(totalOrders != 0){
            Button notificationButton = new Button(String.valueOf(totalOrders));
            notificationButton.setStyle("-fx-background-color: linear-gradient(to right bottom, #c33a9a, #d74d54);-fx-background-radius: 50px; -fx-text-fill: white");
            notificationPane.getChildren().add(notificationButton);
        }
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
        w1.setDevName(currentDeveloper);
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
        a2.setDev(currentDeveloper);
        Stage stage = (Stage) ((Node) e.getSource()).getScene().getWindow();
        Scene scene = new Scene(root);
        stage.setScene(scene);
        stage.setResizable(false);
        stage.show();
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
