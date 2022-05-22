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
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.Pane;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;
import model.Game;
import model.User;
import services.GameService;
import services.UserService;

import java.io.IOException;

public class userWishlistController {
    @FXML
    VBox gameShop;
    @FXML
    Button closeButton;
    @FXML
    Button minimizeButton;
    @FXML
    Button backButton;
    String userName;
    int i;

    public void setUser(String name) {
        userName = name;
        for (User user : UserService.users) {
            if (user.getUsername().equals(userName)) {
                break;
            }
            i++;
        }
        for (Game game : GameService.games) {
            if (UserService.users.get(i).getWishlist().contains(game)) {
                Pane g = new Pane();
                g.setOnMouseEntered(new EventHandler<MouseEvent>() {

                    @Override
                    public void handle(MouseEvent t) {
                        Image img = new Image("cart.png");
                        ImageView view = new ImageView(img);
                        view.setFitWidth(15.0);
                        view.setFitHeight(15.0);
                        g.setStyle("-fx-background-color:#160e36;");
                        Button b = new Button();
                        b.setGraphic(view);
//                        b.setOnAction(addCart);
                        b.setStyle("-fx-background-color: linear-gradient(to right bottom, #6af49e, #4dd787);");
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
    }

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
