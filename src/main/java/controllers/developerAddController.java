package controllers;

import exception.GameAlreadyExistsException;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.stage.Stage;
import model.Developer;
import model.Game;
import services.DeveloperService;
import services.GameService;

import java.io.IOException;

public class developerAddController {
    Developer deve;
    @FXML
    Button addGameButton;
    @FXML
    Label gameName;
    @FXML
    Button goBackButton;
    @FXML
    TextField gameNameLabel;

    public void setDev(Developer dev){
        this.deve = dev;
    }

    public void add() throws GameAlreadyExistsException {
        String gm = gameNameLabel.getText();
        for(Developer dev : DeveloperService.developers){
            if(dev.getUsername().equals(deve.getUsername())){
                Game bufferGame = new Game(gm, dev.getUsername());
                GameService.addGame(gm, dev.getUsername());
                dev.addGame(bufferGame);
                GameService.persistUsers();
                DeveloperService.persistUsers();
                break;
            }
        }
    }

    public void goBack(ActionEvent e) throws IOException {
        FXMLLoader loader = new FXMLLoader(getClass().getClassLoader().getResource("developerWelcome.fxml"));
        Parent root = loader.load();
        developerWelcomeController w1 = loader.getController();
        w1.setCurrentDeveloper(deve);
        Stage stage = (Stage) ((Node) e.getSource()).getScene().getWindow();
        Scene scene = new Scene(root);
        stage.setScene(scene);
        stage.setResizable(false);
    }
}
