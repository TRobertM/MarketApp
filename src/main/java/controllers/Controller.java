package controllers;

import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.control.Label;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.scene.effect.DropShadow;
import javafx.scene.layout.Pane;
import javafx.scene.paint.Color;
import javafx.scene.text.Text;
import services.AuthenticationService;
import services.FXMLChangerService;
import javafx.event.ActionEvent;
import java.io.IOException;
import java.net.URL;
import java.sql.SQLException;
import java.util.ResourceBundle;
import static javafx.scene.effect.BlurType.GAUSSIAN;

public class Controller extends BaseController implements Initializable {

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
    public void login(ActionEvent e) throws IOException, SQLException {
        // Check if the fields are empty
        if (usernameField.getText().trim().isEmpty() || passwordField.getText().trim().isEmpty()) {
            errorText.setText("Please fill everything before trying to log in");
            errorPane.setVisible(true);
            throw new IOException();
        }

        String role = AuthenticationService.checkCredentials(usernameField.getText(), passwordField.getText());
        // Customer login
        if (role.equals("Customer")) {
            FXMLChangerService.changeSceneWithData("userWelcome.fxml", (Node) e.getSource(), usernameField.getText());
        }
        // Developer login
        else if(role.equals("Developer")){
            FXMLChangerService.changeSceneWithData("developerWelcome.fxml", (Node) e.getSource(), usernameField.getText());
        }
        // Error if the user is not found in the database
        else {
            errorText.setText("Wrong username or password");
            errorPane.setVisible(true);
        }
    }

    // Changes tab to register
    @FXML
    public void goToRegister() throws IOException {
        FXMLChangerService.changeScene("registerScene.fxml", goRegisterButton );
    }
}
