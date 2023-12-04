package controllers;

import exception.UsernameAlreadyExistsException;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.scene.effect.DropShadow;
import javafx.scene.layout.Pane;
import javafx.scene.paint.Color;
import javafx.scene.text.Text;
import javafx.stage.Stage;
import model.User;
import org.postgresql.util.PSQLException;
import services.*;

import java.io.IOException;
import java.net.URL;
import java.sql.*;
import java.util.ResourceBundle;
import static javafx.scene.effect.BlurType.GAUSSIAN;

public class RegisterController extends BaseController implements Initializable {

    @FXML
    TextField registerUsernameField;
    @FXML
    PasswordField registerPasswordField;
    @FXML
    PasswordField registerPasswordAgainField;
    @FXML
    ComboBox<String> roleSelector;
    @FXML
    Text errorText;
    @FXML
    Pane errorPane;
    @FXML
    Button minimizeButton;
    @FXML
    Button backButton;

    private String[] roles = {null ,"Developer", "Customer"};

    // Added design
    @Override
    public void initialize(URL arg0, ResourceBundle arg1) {
        errorPane.setVisible(false);
        roleSelector.getItems().addAll(roles);
        DropShadow ds = new DropShadow();
        ds.setColor(Color.valueOf("1d103f"));
        ds.setSpread(-7.0);
        ds.setBlurType(GAUSSIAN);
        ds.setOffsetX(0.0);
        ds.setOffsetY(9.0);
        registerUsernameField.setEffect(ds);
        registerPasswordField.setEffect(ds);
        registerPasswordAgainField.setEffect(ds);
    }

    public void register() throws Exception {
        // Check if all fields have been completed with valid inputs
        if (registerUsernameField.getText().trim().isEmpty() || registerPasswordField.getText().trim().isEmpty() || registerPasswordAgainField.getText().trim().isEmpty()) {
            setErrorText("Fill in all fields", Color.RED);
            throw new IOException();
        }

        // Check that a role has been selected
        if(roleSelector.getValue() == null){
            setErrorText("Select a role", Color.RED);
            throw new Exception();
        }

        // Check that both passwords match
        if(!(registerPasswordField.getText().equals(registerPasswordAgainField.getText()))){
            setErrorText("Passwords do not match", Color.RED);
            throw new Exception();
        }

        try {
            if(UsersManagementService.checkAvailability(registerUsernameField.getText())){
                DatabaseDataService.registerUser(registerUsernameField.getText(), DeveloperService.encodePassword(registerUsernameField.getText(),registerPasswordField.getText()), roleSelector.getValue());
                setErrorText("Account created successfully!", Color.GREEN);
            }
        } catch (UsernameAlreadyExistsException e) {
            setErrorText("Username already exists", Color.RED);
            System.out.println(e.getMessage());
        }
        errorPane.setVisible(true);
    }

    public void backWindow() throws IOException {
        FXMLChangerService.changeScene("scene.fxml", backButton);
    }

    private void setErrorText(String text, Color color){
        errorText.setFill(color);
        errorText.setText(text);
    }
}
