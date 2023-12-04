package controllers;

import exception.GameAlreadyExistsException;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.layout.Pane;
import javafx.scene.text.Text;
import services.DatabaseDataService;

import java.io.IOException;
import java.sql.*;

public class developerAddController extends BaseController implements Initializable {
    @FXML
    Button addGameButton;
    @FXML
    Label gameName;
    @FXML
    TextField gameNameLabel;
    @FXML
    Pane initialPane;
    @FXML
    Pane approvedPane;
    @FXML
    Pane declinePane;
    @FXML
    Text approvedText;
    @FXML
    Text declineText;

    @Override
    public void initialize(java.net.URL location, java.util.ResourceBundle resources) {
    }

    public void add() throws GameAlreadyExistsException, IOException, SQLException {
        String game_name = gameNameLabel.getText();

        if(gameNameLabel.getText().trim().isEmpty()){
            declinedGame();
        }

        if(DatabaseDataService.gameExists(game_name)){
            gameExists();
        } else {
            DatabaseDataService.insertGame(game_name, this.getCurrentUser());
            approvedGame();
        }
    }

    private void declinedGame(){
        declineText.setText("Invalid name");
        initialPane.setVisible(false);
        approvedPane.setVisible(false);
        declinePane.setVisible(true);
    }

    private void approvedGame(){
        approvedText.setText("Game added");
        initialPane.setVisible(false);
        approvedPane.setVisible(true);
        declinePane.setVisible(false);
    }

    private void gameExists(){
        declineText.setText("Game already exists");
        approvedPane.setVisible(false);
        initialPane.setVisible(false);
        declinePane.setVisible(true);
    }
}
