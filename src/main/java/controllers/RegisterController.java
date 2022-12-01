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
                //DeveloperService.addUser(registerUsernameField.getText() , registerPasswordField.getText());
                errorText.setText("Registered successfully");
                errorText.setFill(Color.GREEN);
                errorPane.setVisible(true);

                ////// SQL Database add developer account to database
                String username = registerUsernameField.getText();
                String password = DeveloperService.encodePassword(registerUsernameField.getText(), registerPasswordField.getText());
                Connection connection = DriverManager.getConnection("jdbc:postgresql://localhost:5432/postgres", "postgres", "admin");
                Statement check_developers = connection.createStatement();
                ResultSet all_developers = check_developers.executeQuery("SELECT username FROM users WHERE role = 'Developer'");
                while(all_developers.next()){
                    if(username.equals(all_developers.getString(1))){
                        throw new UsernameAlreadyExistsException(username);
                    }
                }
                String query = "INSERT INTO users VALUES(?,?,?,?)";
                PreparedStatement ps = connection.prepareStatement(query);
                PreparedStatement sequence_value = connection.prepareStatement("SELECT nextval('users_sq')");
                ResultSet rs = sequence_value.executeQuery();
                if(rs.next()){
                    int next_ID = rs.getInt(1);
                    ps.setInt(1,next_ID);
                }
                ps.setString(2, username);
                ps.setString(3, password);
                ps.setString(4, "Developer");
                ps.executeUpdate();
                ps.close();
                connection.close();
                /////
            } catch (UsernameAlreadyExistsException e){
                errorText.setFill(Color.RED);
                errorText.setText("An account with the given username already exists!");
                errorPane.setVisible(true);
            } catch (Exception e) {
                errorText.setFill(Color.RED);
                errorText.setText("Failed to create account, rewrite every field and try again!");
                errorPane.setVisible(true);
            }
        } else {
            try {
                String username = registerUsernameField.getText();
                String password = DeveloperService.encodePassword(registerUsernameField.getText(), registerPasswordField.getText());
                Connection connection = DriverManager.getConnection("jdbc:postgresql://localhost:5432/postgres", "postgres", "admin");
                Statement check_users = connection.createStatement();
                ResultSet all_customers = check_users.executeQuery("SELECT username FROM users WHERE role = 'Customer'");
                while(all_customers.next()){
                    if(username.equals(all_customers.getString(1))){
                        throw new UsernameAlreadyExistsException(username);
                    }
                }
                String query = "INSERT INTO users VALUES(?,?,?,?)";
                PreparedStatement ps = connection.prepareStatement(query);
                PreparedStatement sequence_value = connection.prepareStatement("SELECT nextval('users_sq')");
                ResultSet rs = sequence_value.executeQuery();
                if(rs.next()){
                    int next_ID = rs.getInt(1);
                    ps.setInt(1,next_ID);
                }
                ps.setString(2, username);
                ps.setString(3, password);
                ps.setString(4, "Customer");
                ps.executeUpdate();
                ps.close();
                connection.close();
                errorText.setText("Registered successfully");
                errorText.setFill(Color.GREEN);
                errorPane.setVisible(true);
            } catch (UsernameAlreadyExistsException e) {
                errorText.setFill(Color.RED);
                errorText.setText("An account with the given username already exists!");
                errorPane.setVisible(true);
                System.out.println(e);
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
