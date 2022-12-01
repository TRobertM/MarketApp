package controllers;

import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.scene.effect.DropShadow;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.Pane;
import javafx.scene.paint.Color;
import javafx.scene.text.Text;
import javafx.stage.Stage;
import model.Developer;
import model.User;
import services.DeveloperService;
import services.UserService;

import javafx.event.ActionEvent;
import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;

import static javafx.scene.effect.BlurType.GAUSSIAN;

public class Controller implements Initializable {

    @FXML
    PasswordField passwordField;
    @FXML
    TextField usernameField;
    @FXML
    Pane errorPane;
    @FXML
    Text errorText;
    @FXML
    Label goRegisterButton;
    @FXML
    Button xButton;
    @FXML
    Button minimizeButton;



    @Override
    // Initialize some added effects on the elements
    public void initialize(URL arg0, ResourceBundle arg1) {
        errorPane.setVisible(false);
        DropShadow ds = new DropShadow();
        ds.setColor(Color.valueOf("1d103f"));
        ds.setSpread(-7.0);
        ds.setBlurType(GAUSSIAN);
        ds.setOffsetX(0.0);
        ds.setOffsetY(9.0);
        usernameField.setEffect(ds);
        passwordField.setEffect(ds);
    }


    // Login method
    public void login(ActionEvent e) throws IOException {
        // Check if the fields are empty and modify errorText accordingly.
        if (usernameField.getText().trim().isEmpty() || passwordField.getText().trim().isEmpty()) {
            errorText.setText("Please fill everything before trying to log in");
            errorPane.setVisible(true);
            throw new IOException();
        }

        // If the user trying to log in is a customer, log him in and send him to the customer main page
        if (UserService.login(usernameField.getText(), passwordField.getText())) {
            FXMLLoader loader = new FXMLLoader(getClass().getClassLoader().getResource("userWelcome.fxml"));
            Parent root = loader.load();
            userWelcomeController userWelcome = loader.getController();
            userWelcome.setDev(usernameField.getText());
            Stage stage = (Stage) ((Node) e.getSource()).getScene().getWindow();
            Scene scene = new Scene(root);
            stage.setScene(scene);
            stage.setResizable(false);
        }

        // If the user is a developer, log him into the developer main page and send to the developer main page information about the developer that just logged in
        else if(DeveloperService.login(usernameField.getText(), passwordField.getText())){
            FXMLLoader loader = new FXMLLoader(getClass().getClassLoader().getResource("developerWelcome.fxml"));
            Parent root = loader.load();
            developerWelcomeController w1 = loader.getController();
//            for(Developer dev : DeveloperService.developers){
//                if(dev.getUsername().equals(usernameField.getText())){
            w1.setCurrentDeveloper(usernameField.getText());
//                    break;
//                }
//            }
            Stage stage = (Stage) ((Node) e.getSource()).getScene().getWindow();
            Scene scene = new Scene(root);
            stage.setScene(scene);
            stage.setResizable(false);
        }

        // If the user is not found in any file it means the username or password are wrong
        else {
            errorText.setText("Wrong username or password");
            errorPane.setVisible(true);
        }
    }


    // Changes tab to register
    @FXML
    public void goToRegister() throws IOException {
        Parent root = FXMLLoader.load((getClass().getClassLoader().getResource("registerScene.fxml")));
        Stage stage = (Stage)goRegisterButton.getScene().getWindow();
        Scene scene = new Scene(root);
        stage.setScene(scene);
        stage.setResizable(false);
    }

    // Closes window
    public void closeWindow(){
        Stage stage = (Stage) xButton.getScene().getWindow();
        stage.close();
    }

    // Minimize window
    public void minimizeWindow(){
        Stage stage = (Stage) minimizeButton.getScene().getWindow();
        stage.setIconified(true);
    }
}
