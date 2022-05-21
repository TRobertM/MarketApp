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
import javafx.scene.layout.Pane;
import javafx.scene.paint.Color;
import javafx.scene.text.Text;
import javafx.stage.Stage;
import model.Developer;
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

    public void login(ActionEvent e) throws IOException {
        developerWelcomeController c1 = new developerWelcomeController();
        if (usernameField.getText().trim().isEmpty() || passwordField.getText().trim().isEmpty()) {
            errorText.setText("Please fill everything before trying to log in");
            errorPane.setVisible(true);
            throw new IOException();
        }
        if (UserService.login(usernameField.getText(), passwordField.getText())) {
            System.out.println("Not implemented yet");
//            FXMLLoader loader = new FXMLLoader(getClass().getClassLoader().getResource("developerWelcome.fxml"));
//            Parent root = loader.load();
//            welcomeController w1 = loader.getController();
//            Stage stage = (Stage) ((Node) e.getSource()).getScene().getWindow();
//            Scene scene = new Scene(root);
//            stage.setScene(scene);
//            stage.setResizable(false);
        }
        else if(DeveloperService.login(usernameField.getText(), passwordField.getText())){
            FXMLLoader loader = new FXMLLoader(getClass().getClassLoader().getResource("developerWelcome.fxml"));
            Parent root = loader.load();
            developerWelcomeController w1 = loader.getController();
            for(Developer dev : DeveloperService.developers){
                if(dev.getUsername().equals(usernameField.getText())){
                    w1.setCurrentDeveloper(dev);
                    break;
                }
            }
            Stage stage = (Stage) ((Node) e.getSource()).getScene().getWindow();
            Scene scene = new Scene(root);
            stage.setScene(scene);
            stage.setResizable(false);
        }
        else {
            errorText.setText("Wrong username or password");
            errorPane.setVisible(true);
        }
    }

    @FXML
    public void goToRegister() throws IOException {
        Parent root = FXMLLoader.load((getClass().getClassLoader().getResource("registerScene.fxml")));
        Stage stage = (Stage)goRegisterButton.getScene().getWindow();
        Scene scene = new Scene(root);
        stage.setScene(scene);
        stage.setResizable(false);
    }

    public void closeWindow(){
        Stage stage = (Stage) xButton.getScene().getWindow();
        stage.close();
    }

    public void minimizeWindow(){
        Stage stage = (Stage) minimizeButton.getScene().getWindow();
        stage.setIconified(true);
    }
}
