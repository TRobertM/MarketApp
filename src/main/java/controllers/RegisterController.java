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
import services.ConnectionService;
import services.DeveloperService;
import services.UserService;
import java.io.IOException;
import java.net.URL;
import java.sql.*;
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

    public void register() throws Exception {
        // Check if all fields have been completed with valid inputs
        if (registerUsernameField.getText().trim().isEmpty() || registerPasswordField.getText().trim().isEmpty() || registerPasswordAgainField.getText().trim().isEmpty()) {
            errorText.setFill(Color.RED);
            errorText.setText("Complete all fields to register");
            errorPane.setVisible(true);
            throw new IOException();
        }

        // Check that a role has been selected
        if(roleSelector.getValue() == null){
            errorText.setFill(Color.RED);
            errorText.setText("Role not selected");
            errorPane.setVisible(true);
            throw new Exception();
        }

        // Check that both passwords match
        if(!(registerPasswordField.getText().equals(registerPasswordAgainField.getText()))){
            errorText.setFill(Color.RED);
            errorText.setText("Passwords do not match");
            errorPane.setVisible(true);
            throw new Exception();
        }

        // Creates an account after checking that the username is unique
        if(roleSelector.getValue().equals("Developer")){
            try {
                String username = registerUsernameField.getText();
                String password = DeveloperService.encodePassword(registerUsernameField.getText(), registerPasswordField.getText());
                Connection con = ConnectionService.Connect();
                Statement check_developers = con.createStatement();
                ResultSet all_developers = check_developers.executeQuery("SELECT username FROM users");
                while(all_developers.next()){
                    if(username.equals(all_developers.getString(1))){
                        throw new UsernameAlreadyExistsException(username);
                    }
                }
                PreparedStatement ps = con.prepareStatement("INSERT INTO users VALUES(?,?,?)");
                ps.setString(1, username);
                ps.setString(2, password);
                ps.setString(3, "Developer");
                ps.executeUpdate();
                ps.close();
                check_developers.close();
                con.close();
                errorText.setText("Registered successfully");
                errorText.setFill(Color.GREEN);
                errorPane.setVisible(true);
            } catch (UsernameAlreadyExistsException e){
                errorText.setFill(Color.RED);
                errorText.setText("An account with the given username already exists!");
                errorPane.setVisible(true);
            } catch (Exception e) {
                errorText.setFill(Color.RED);
                errorText.setText("Failed to create account, rewrite every field and try again!");
                errorPane.setVisible(true);
                e.printStackTrace();
            }
        } else {
            try {
                String username = registerUsernameField.getText();
                String password = DeveloperService.encodePassword(registerUsernameField.getText(), registerPasswordField.getText());
                Connection con = ConnectionService.Connect();
                Statement check_users = con.createStatement();
                ResultSet all_customers = check_users.executeQuery("SELECT username FROM users");
                while(all_customers.next()){
                    if(username.equals(all_customers.getString(1))){
                        throw new UsernameAlreadyExistsException(username);
                    }
                }
                String query = "INSERT INTO users VALUES(?,?,?)";
                PreparedStatement ps = con.prepareStatement(query);
                ps.setString(1, username);
                ps.setString(2, password);
                ps.setString(3, "Customer");
                ps.executeUpdate();
                ps.close();
                check_users.close();
                con.close();
                errorText.setText("Registered successfully");
                errorText.setFill(Color.GREEN);
                errorPane.setVisible(true);
            } catch (UsernameAlreadyExistsException e) {
                errorText.setFill(Color.RED);
                errorText.setText("An account with the given username already exists!");
                errorPane.setVisible(true);
            } catch (Exception e){
                errorText.setFill(Color.RED);
                errorText.setText("Failed to create account, rewrite every field and try again!");
                errorPane.setVisible(true);
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
