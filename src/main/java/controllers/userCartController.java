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
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.Pane;
import javafx.scene.layout.VBox;
import javafx.scene.text.Text;
import javafx.stage.Stage;
import model.Developer;
import model.Game;
import model.Order;
import model.User;
import services.DeveloperService;
import services.GameService;
import services.UserService;

import java.io.IOException;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;
import java.util.ResourceBundle;

public class userCartController {
    @FXML
    Button closeButton;
    @FXML
    Button minimizeButton;
    @FXML
    Button backButton;
    @FXML
    VBox myGames;
    @FXML
    Pane successPane;
    @FXML
    Text textPane;
    @FXML
    ScrollPane megaPane;

    int i;
    String userName;

    public void setUser(String name){
        userName = name;
        for(User user : UserService.users){
            if(user.getUsername().equals(userName)){
                break;
            }
            i++;
        }

        if(UserService.users.get(i).getCart().isEmpty()){
            textPane.setText("Your cart is empty!");
            textPane.setStyle("-fx-text-fill: white");
            successPane.setVisible(true);
        }

        else {
            successPane.setVisible(false);
            megaPane.setVisible(true);
            for (Game game : GameService.games) {
                if (UserService.users.get(i).getCart().contains(game)) {
                    Pane g = new Pane();
                    g.setOnMouseEntered(new EventHandler<MouseEvent>() {

                        @Override
                        public void handle(MouseEvent t) {
                            g.setStyle("-fx-background-color:#160e36;");
                            Button b = new Button("X");
                            b.setOnAction(removeCart);
                            b.setStyle("-fx-background-color: linear-gradient(to right bottom, #c33a9a, #d74d54);");
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

    private EventHandler<ActionEvent> removeCart = new EventHandler<>() {
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
                    UserService.users.get(i).getCart().remove(game);
                    UserService.persistUsers();
                    break;
                }
            }
        }
    };

    public void sendOrder(){
        List<Game> games = UserService.users.get(i).getCart();
        for(Game game : UserService.users.get(i).getCart()){
            for(Developer dev : DeveloperService.developers){
                if(dev.getGames().contains(game)){
                    dev.getOrders().add(new Order(userName, game.getName(), dev.getUsername()));
                    DeveloperService.persistUsers();
                    break;
                }
            }
        }
        myGames.setVisible(false);
        textPane.setText("Order sent successfully!");
        textPane.setStyle("-fx-text-fill: green");
        successPane.setVisible(true);
        UserService.users.get(i).getCart().removeAll(games);
        UserService.persistUsers();
    }

    public void closeWindow(){
        Stage stage = (Stage) closeButton.getScene().getWindow();
        stage.close();
    }

    public void minimizeWindow(){
        Stage stage = (Stage) minimizeButton.getScene().getWindow();
        stage.setIconified(true);
    }

    public void backWindow(ActionEvent e) throws IOException {
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
