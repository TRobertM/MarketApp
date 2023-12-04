package controllers;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.input.MouseEvent;
import javafx.scene.text.Text;
import javafx.scene.text.TextFlow;
import services.FXMLChangerService;

import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;

public class userWelcomeController extends BaseController implements Initializable {
    @FXML
    TextFlow gamesLibrary;
    @FXML
    TextFlow myWishlist;
    @FXML
    TextFlow myGames;
    @FXML
    Button logoutButton;
    @FXML
    Label welcomeLabel;

    String currentUser = BaseController.getCurrentUser();

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        welcomeLabel.setText("Welcome, " + currentUser);
    }

    public void logoutUser(ActionEvent e) throws IOException {
        FXMLChangerService.changeScene("scene.fxml", (Node) e.getSource());
    }

    public void setDev(String name){
        welcomeLabel.setText("Welcome, " + currentUser);
    }

    public void goToLibrary(MouseEvent e) throws IOException {
        FXMLChangerService.changeSceneWithData("userLibrary.fxml", gamesLibrary, currentUser);
    }

    public void goToWishlist(MouseEvent e) throws IOException {
        FXMLChangerService.changeSceneWithData("userWishlist.fxml", myWishlist, currentUser);
    }

    public void goToGames(MouseEvent e) throws IOException {
        FXMLChangerService.changeSceneWithData("userGames.fxml", myGames, currentUser);
    }

    public void goToCart(MouseEvent e) throws IOException {
        FXMLChangerService.changeSceneWithData("userCart.fxml", myGames, currentUser);
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
