package controllers;

import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.stage.Stage;
import services.FXMLChangerService;
import services.UsersManagementService;
import java.io.IOException;
import java.sql.SQLException;

public abstract class BaseController {
    @FXML
    private Button closeButton;

    @FXML
    private Button minimizeButton;

    @FXML
    Button backButton;

    private static String currentUser;

    public void closeWindow(){
        Stage stage = (Stage) closeButton.getScene().getWindow();
        stage.close();
    }

    public void minimizeWindow(){
        Stage stage = (Stage) minimizeButton.getScene().getWindow();
        stage.setIconified(true);
    }

    public static String getCurrentUser(){
        return currentUser;
    }

    public static void setCurrentUser(String username){
        currentUser = username;
    }

    public void goBack() throws SQLException, IOException {
        String role = UsersManagementService.checkRole(currentUser);
        if(role.equals("Developer")){
            FXMLChangerService.changeSceneWithData("developerWelcome.fxml", backButton, currentUser);
        } else if(role.equals("Customer")){
            FXMLChangerService.changeSceneWithData("userWelcome.fxml", backButton, currentUser);
        }
    }
}
