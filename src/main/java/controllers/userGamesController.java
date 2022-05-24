package controllers;

import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.Pane;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;
import model.Game;
import model.User;
import services.DeveloperService;
import services.GameService;
import services.UserService;

import java.io.IOException;

public class userGamesController {
    @FXML
    Button closeButton;
    @FXML
    Button minimizeButton;
    @FXML
    Button backButton;
    @FXML
    VBox myGames;

    int i = 0;
    String userName;


    public void setUser(String name){
        userName = name;
        for(User user : UserService.users){
            if(user.getUsername().equals(userName)){
                break;
            }
            i++;
        }
        if(!GameService.games.isEmpty()) {
            for (Game game : DeveloperService.developers.get(i).getGames()) {
                if (UserService.users.get(i).getGames().contains(game)) {
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
                    myGames.getChildren().add(g);
                }
            }
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
                            myGames.getChildren().remove(p);
                            break;
                        }
                    }
                }
            }
            for(Game game : GameService.games){
                if(game.getName().equals(g)){
                    UserService.users.get(i).getGames().remove(game);
                    UserService.persistUsers();
                    break;
                }
            }
        }
    };

    public void closeWindow(){
        Stage stage = (Stage) closeButton.getScene().getWindow();
        stage.close();
    }

    public void minimizeWindow(){
        Stage stage = (Stage) minimizeButton.getScene().getWindow();
        stage.setIconified(true);
    }

    public void goBack(ActionEvent e) throws IOException {
        FXMLLoader loader = new FXMLLoader(getClass().getClassLoader().getResource("userWelcome.fxml"));
        Parent root = loader.load();
        userWelcomeController uw = loader.getController();
        uw.setDev(UserService.users.get(i).getUsername());
        Stage stage = (Stage) ((Node) e.getSource()).getScene().getWindow();
        Scene scene = new Scene(root);
        stage.setScene(scene);
        stage.setResizable(false);
    }
}
