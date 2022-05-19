package controllers;

import services.UserService;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.ChoiceBox;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.scene.text.Text;

import java.net.URL;
import java.util.ResourceBundle;

public class Controller implements Initializable{
    @FXML
    private TextField userField;
    @FXML
    private Button regButton;
    @FXML
    private PasswordField passwordField;
    @FXML
    private ChoiceBox<String> roleField;
    @FXML
    private Text loginText;
    @FXML
    private Button loginButton;

    private String[] roles = {"Developer", "Customer"};

    @Override
    public void initialize(URL arg0, ResourceBundle arg1){
        roleField.getItems().addAll(roles);
    }

    public void register() throws Exception {
        if(roleField.getValue() == null){
            throw new Exception();
        }
        try {
            UserService.addUser(userField.getText() , passwordField.getText() , roleField.getValue());
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void login() {
        if(UserService.login(userField.getText(), passwordField.getText())){
            loginText.setText("Logged in!");
        } else loginText.setText("Username/Password incorrect!");
    }
}
