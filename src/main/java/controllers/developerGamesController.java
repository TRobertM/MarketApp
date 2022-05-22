package controllers;

import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.Pane;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;
import model.Developer;
import model.Game;
import model.User;
import services.DeveloperService;
import services.GameService;
import services.UserService;


import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;

public class developerGamesController implements Initializable {
    @FXML
    Button xButton;
    @FXML
    Button minimizeButton;
    @FXML
    Button backButton;
    @FXML
    VBox gameShop;

    // devName is used to store the name of the logged in developer and i is used to know the place of the developer in the developers.json file
    String devName;
    int i;

    // Useless ATM used it for some checks and testing
    @Override
    public void initialize(URL location, ResourceBundle resources){
    }

    public void closeWindow(){
        Stage stage = (Stage) xButton.getScene().getWindow();
        stage.close();
    }

    public void minimizeWindow(){
        Stage stage = (Stage) minimizeButton.getScene().getWindow();
        stage.setIconified(true);
    }

    // Goes back to the main page of the developer
    public void backWindow(ActionEvent e) throws IOException {
        FXMLLoader loader = new FXMLLoader(getClass().getClassLoader().getResource("developerWelcome.fxml"));
        Parent root = loader.load();
        developerWelcomeController w1 = loader.getController();
        w1.setCurrentDeveloper(DeveloperService.developers.get(0));
        Stage stage = (Stage) ((Node) e.getSource()).getScene().getWindow();
        Scene scene = new Scene(root);
        stage.setScene(scene);
        stage.setResizable(false);
    }

    /*
    Outdated method name DO NOT EDIT.
    Method reads all games from current developer and creates a button for each of them. After that it appends all elements
    into a scrollPane. Also adds some minor hover effects for every item.
     */
    public void setDevName(String name){
        devName = name;
        try {
            GameService.loadUsersFromFile();
        } catch (IOException e) {
            e.printStackTrace();
        }
        for(Developer dev : DeveloperService.developers){
            if(dev.getUsername().equals(devName)){
                break;
            }
            i++;
        }
        for(Game game : DeveloperService.developers.get(i).getGames()){
                Pane g = new Pane();
                g.setOnMouseEntered(new EventHandler<MouseEvent>() {

                @Override
                public void handle(MouseEvent t) {
                    g.setStyle("-fx-background-color:#160e36;");
                    Button b = new Button("X");
                    b.setOnAction(removeGame);
                    b.relocate(590, 8.5);
                    g.getChildren().add(b);
                }
                });
                g.setOnMouseExited(new EventHandler<MouseEvent>() {

                    @Override
                    public void handle(MouseEvent t) {
                        g.setStyle("-fx-background-color:transparent;");
                        g.getChildren().remove(1);
                    }
                });
                g.setMinHeight(40);
                g.setMinWidth(528);
                Label n = new Label(game.getName());
                n.relocate(10, 12);
                g.getChildren().add(n);
                gameShop.getChildren().add(g);
        }
    }

    /*
    Method of the button. Upon button click, the method gets the name of the connected game and removes it from the scrollPane,
    removes it from the list of games, removes it from the developers list of games and removes it from every users list of games
     */
    private EventHandler<ActionEvent> removeGame = new EventHandler<>() {
        public void handle(ActionEvent event) {
            Pane p = new Pane();
            String g = "";
            if(event.getSource() instanceof Button){
                if(((Button) event.getSource()).getParent() instanceof Pane){
                    for(Node node : ((Pane) ((Button) event.getSource()).getParent()).getChildren()){
                        if(node instanceof Label){
                            g += ((Label) node).getText();
                            p = (Pane)node.getParent();
                            gameShop.getChildren().remove(p);
                            break;
                        }
                    }
                }
            }
            for(Game game : GameService.games){
                if(game.getName().equals(g)){
                    for(User user : UserService.users){
                        if(user.getGames().contains(game)){
                            user.getGames().remove(game);
                            UserService.persistUsers();
                        }
                    }
                    GameService.games.remove(game);
                    DeveloperService.developers.get(i).getGames().remove(game);
                    GameService.persistUsers();
                    DeveloperService.persistUsers();
                    break;
                }
            }
        }
    };
}
