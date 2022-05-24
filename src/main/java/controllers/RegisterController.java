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
import services.DeveloperService;
import services.UserService;
import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;
import static javafx.scene.effect.BlurType.GAUSSIAN;

public class RegisterController implements Initializable {

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
    Button xButton;
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

    // Checks that every field is completed, checks that role is selected and lastly verifies
    // User can create both developer and customer accounts with the same name this is KNOWN and will be fixed
    public void register() throws Exception {
        if (registerUsernameField.getText().trim().isEmpty() || registerPasswordField.getText().trim().isEmpty() || registerPasswordAgainField.getText().trim().isEmpty()) {
            errorText.setFill(Color.RED);
            errorText.setText("Complete all fields to register");
            errorPane.setVisible(true);
            throw new IOException();
        }
        if(roleSelector.getValue() == null){
            errorText.setFill(Color.RED);
            errorText.setText("Role not selected");
            errorPane.setVisible(true);
            throw new Exception();
        }
        if(!(registerPasswordField.getText().equals(registerPasswordAgainField.getText()))){
            errorText.setFill(Color.RED);
            errorText.setText("Passwords do not match");
            errorPane.setVisible(true);
            throw new Exception();
        }
        if(roleSelector.getValue().equals("Developer")){
            try {
                DeveloperService.addUser(registerUsernameField.getText() , registerPasswordField.getText());
                errorText.setText("Registered successfully");
                errorText.setFill(Color.GREEN);
                errorPane.setVisible(true);
            } catch (UsernameAlreadyExistsException e) {
                errorText.setFill(Color.RED);
                errorText.setText("Account with given name already exists");
                errorPane.setVisible(true);
                System.out.println(e);
            }
        } else {
            try {
                UserService.addUser(registerUsernameField.getText(), registerPasswordField.getText(), roleSelector.getValue());
                errorText.setText("Registered successfully");
                errorText.setFill(Color.GREEN);
                errorPane.setVisible(true);
            } catch (UsernameAlreadyExistsException e) {
                errorText.setFill(Color.RED);
                errorText.setText("Account with given name already exists");
                errorPane.setVisible(true);
                System.out.println(e);
            }
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

    public void backWindow() throws IOException {
        Parent root = FXMLLoader.load((getClass().getClassLoader().getResource("scene.fxml")));
        Stage stage = (Stage)backButton.getScene().getWindow();
        Scene scene = new Scene(root);
        stage.setScene(scene);
        stage.setResizable(false);
    }
}
