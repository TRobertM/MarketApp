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
import javafx.scene.text.Text;
import javafx.scene.text.TextFlow;
import javafx.stage.Stage;
import java.io.IOException;

public class userWelcomeController {
    @FXML
    TextFlow gamesLibrary;
    @FXML
    TextFlow myWishlist;
    @FXML
    TextFlow myGames;
    @FXML
    Button logoutButton;
    @FXML
    Button closeButton;
    @FXML
    Button minimizeButton;
    @FXML
    Label welcomeLabel;
    String currentDev;

    public void logoutUser(ActionEvent e) throws IOException {
        FXMLLoader loader = new FXMLLoader(getClass().getClassLoader().getResource("scene.fxml"));
        Parent root = loader.load();
        Stage stage = (Stage) ((Node) e.getSource()).getScene().getWindow();
        Scene scene = new Scene(root);
        stage.setScene(scene);
        stage.setResizable(false);
    }

    public void closeWindow(){
        Stage stage = (Stage) closeButton.getScene().getWindow();
        stage.close();
    }

    public void minimizeWindow(){
        Stage stage = (Stage) minimizeButton.getScene().getWindow();
        stage.setIconified(true);
    }

    public void setDev(String name){
        welcomeLabel.setText("Welcome, " + name);
        currentDev = name;
    }

    public void goToLibrary(MouseEvent e) throws IOException {
        FXMLLoader loader = new FXMLLoader(getClass().getClassLoader().getResource("userLibrary.fxml"));
        Parent root = loader.load();
        userLibraryController u1 = loader.getController();
        u1.setUser(currentDev);
        Stage stage = (Stage) ((Node) e.getSource()).getScene().getWindow();
        Scene scene = new Scene(root);
        stage.setScene(scene);
        stage.setResizable(false);
    }

    // ------------------------------------------------- Everything below this is implemented only for design purposes -----------------------------------------------------
    public void gamesLibraryHover(){
        ((Text)gamesLibrary.getChildren().get(1)).setText(" L I B R A R Y >");
    }

    public void gamesLibraryExit(){
        ((Text)gamesLibrary.getChildren().get(1)).setText(" L I B R A R Y");
    }

    public void myWishlistHover(){
        ((Text)myWishlist.getChildren().get(1)).setText(" W I S H L I S T >");
    }

    public void myWishlistExit(){
        ((Text)myWishlist.getChildren().get(1)).setText(" W I S H L I S T");
    }

    public void myGamesHover(){
        ((Text)myGames.getChildren().get(1)).setText(" G A M E S>");
    }

    public void myGamesExit(){
        ((Text)myGames.getChildren().get(1)).setText(" G A M E S");
    }
}
