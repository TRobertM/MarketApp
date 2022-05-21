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
import javafx.scene.control.ScrollPane;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.Pane;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;
import model.Developer;
import model.Game;
import services.DeveloperService;
import services.GameService;


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

    String devName;
    int i;

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
