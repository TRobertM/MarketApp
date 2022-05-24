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
import javafx.scene.layout.Pane;
import javafx.scene.text.Text;
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
    int i;

    public void setDev(Developer dev){
        this.deve = dev;
    }

    public void add() throws GameAlreadyExistsException, IOException {
        String gm = gameNameLabel.getText();
        GameService.loadgamesfromfile();
        for(Developer dev : DeveloperService.developers){
            if(dev.getUsername().equals(deve.getUsername())){
                break;
            }
            i++;
        }
        if(gameNameLabel.getText().trim().isEmpty()){
            declineText.setText("Invalid name");
            initialPane.setVisible(false);
            approvedPane.setVisible(false);
            declinePane.setVisible(true);
            throw new IOException();
        }
        for(Game game : GameService.games){
            if(game.getName().equals(gm)){
                declineText.setText("Game already exists");
                approvedPane.setVisible(false);
                initialPane.setVisible(false);
                declinePane.setVisible(true);
                throw new GameAlreadyExistsException(game.getName());
            }
        }
        approvedText.setText("Game added successfully");
        initialPane.setVisible(false);
        declinePane.setVisible(false);
        approvedPane.setVisible(true);
        Game bufferGame = new Game(gm, DeveloperService.developers.get(i).getUsername());
        GameService.addGame(gm, DeveloperService.developers.get(i).getUsername());
        DeveloperService.developers.get(i).addGame(bufferGame);
        GameService.persistUsers();
        DeveloperService.persistUsers();
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
