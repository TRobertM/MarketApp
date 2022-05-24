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

public class userLibraryController {
    @FXML
    Button closeButton;
    @FXML
    Button minimizeButton;
    @FXML
    VBox gameShop;
    @FXML
    Button goBackButton;

    String userName;
    int i;

    public void closeWindow(){
        Stage stage = (Stage) closeButton.getScene().getWindow();
        stage.close();
    }

    public void minimizeWindow(){
        Stage stage = (Stage) minimizeButton.getScene().getWindow();
        stage.setIconified(true);
    }

    public void setUser(String name){
        userName = name;
        try {
            UserService.loadUsersFromFile();
            GameService.loadgamesfromfile();
        } catch (IOException e) {
            e.printStackTrace();
        }
        for(User user : UserService.users){
            if(user.getUsername().equals(userName)) {
                break;
            }
            i++;
        }
        for(Game game : GameService.games){
            if(UserService.users.get(i).getWishlist().contains(game)){
                continue;
            }
            if(UserService.users.get(i).getCart().contains(game)){
                continue;
            }
            if(UserService.users.get(i).getGames().contains(game)){
                continue;
            }
            else{
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
                        b.setOnAction(addCart);
                        b.setStyle("-fx-background-color: linear-gradient(to right bottom, #6af49e, #4dd787);");
                        b.relocate(590, 8.5);
                        g.getChildren().add(b);

                        Image img2 = new Image("wishlist.jpg");
                        ImageView view2 = new ImageView(img2);
                        view2.setFitHeight(15.0);
                        view2.setFitWidth(15.0);
                        Button b2 = new Button();
                        b2.setGraphic(view2);
                        b2.setOnAction(addWishlist);
                        b2.setStyle("-fx-background-color: linear-gradient(to right bottom, #c33a9a, #d74d54);");
                        b2.relocate(550,8.5);
                        g.getChildren().add(b2);
                    }
                });
                g.setOnMouseExited(new EventHandler<MouseEvent>() {

                    @Override
                    public void handle(MouseEvent t) {
                        g.setStyle("-fx-background-color:transparent;");
                        g.getChildren().remove(1);
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

    private EventHandler<ActionEvent> addWishlist = new EventHandler<>() {
        public void handle(ActionEvent event) {
            Pane p = new Pane();
            String g = "";
            int j = 0;
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
                    break;
                }
                j++;
            }
            UserService.users.get(i).getWishlist().add(GameService.games.get(j));
            UserService.persistUsers();
        }
    };

    private EventHandler<ActionEvent> addCart = new EventHandler<>() {
        public void handle(ActionEvent event) {
            Pane p = new Pane();
            String g = "";
            int j = 0;
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
                    break;
                }
                j++;
            }
            UserService.users.get(i).getCart().add(GameService.games.get(j));
            UserService.persistUsers();
        }
    };

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
