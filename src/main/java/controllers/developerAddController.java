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
import java.sql.*;

public class developerAddController {
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
    String currentDeveloper;

    public void setDev(String dev){
        currentDeveloper = dev;
    }

    public void add() throws GameAlreadyExistsException, IOException {
        String game_name = gameNameLabel.getText();
        //GameService.loadgamesfromfile();
        if(gameNameLabel.getText().trim().isEmpty()){
            declineText.setText("Invalid name");
            initialPane.setVisible(false);
            approvedPane.setVisible(false);
            declinePane.setVisible(true);
            throw new IOException();
        }
        try{
            Connection connection = DriverManager.getConnection("jdbc:postgresql://localhost:5432/postgres", "postgres", "admin");
            Statement get_games = connection.createStatement();
            ResultSet all_games = get_games.executeQuery("SELECT name FROM games");
            while(all_games.next()){
                if(all_games.getString(1).equals(game_name)){
                    declineText.setText("Game already exists");
                    approvedPane.setVisible(false);
                    initialPane.setVisible(false);
                    declinePane.setVisible(true);
                    get_games.close();
                    all_games.close();
                    throw new GameAlreadyExistsException(game_name);
                }
            }
            String query = "INSERT INTO games VALUES(?,?,?)";
            PreparedStatement add_game = connection.prepareStatement(query);
            PreparedStatement games_sequence = connection.prepareStatement("SELECT nextval('games_sq')");
            ResultSet sequence_number = games_sequence.executeQuery();
            if(sequence_number.next()){
                int ID = sequence_number.getInt(1);
                add_game.setInt(1, ID);
            }
            add_game.setString(2, game_name);
            add_game.setString(3, currentDeveloper);
            add_game.executeUpdate();
            approvedText.setText("Game added successfully");
            initialPane.setVisible(false);
            declinePane.setVisible(false);
            approvedPane.setVisible(true);
            connection.close();
            add_game.close();
            games_sequence.close();
            sequence_number.close();
        }
        catch (GameAlreadyExistsException e){
            e.printStackTrace();
        }
        catch (Exception e){
            //e.printStackTrace();
        }

    }

    public void goBack(ActionEvent e) throws IOException {
        FXMLLoader loader = new FXMLLoader(getClass().getClassLoader().getResource("developerWelcome.fxml"));
        Parent root = loader.load();
        developerWelcomeController w1 = loader.getController();
        w1.setCurrentDeveloper(currentDeveloper);
        Stage stage = (Stage) ((Node) e.getSource()).getScene().getWindow();
        Scene scene = new Scene(root);
        stage.setScene(scene);
        stage.setResizable(false);
    }
}
