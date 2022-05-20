package controllers;

import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.ComboBox;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.scene.effect.DropShadow;
import javafx.scene.paint.Color;
import services.UserService;

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
    private String[] roles = {"Developer", "Customer"};

    @Override
    public void initialize(URL arg0, ResourceBundle arg1) {
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
        if(roleSelector.getValue() == null){
            throw new Exception();
        }
        if(!(registerPasswordField.getText().equals(registerPasswordAgainField.getText()))){
            System.out.println("NU");
            throw new Exception();
        }
        try {
            UserService.addUser(registerUsernameField.getText() , registerPasswordField.getText() , roleSelector.getValue());
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
