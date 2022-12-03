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
import services.ConnectionService;
import services.GameService;
import services.UserService;

import java.io.IOException;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.Statement;

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

    public void closeWindow(){
        Stage stage = (Stage) closeButton.getScene().getWindow();
        stage.close();
    }

    public void minimizeWindow(){
        Stage stage = (Stage) minimizeButton.getScene().getWindow();
        stage.setIconified(true);
    }

    public void setUser(String name) {
        userName = name;
        try {
            Connection con = ConnectionService.Connect();
            Statement get_games = con.createStatement();
            ResultSet all_games = get_games.executeQuery(
                    "SELECT name FROM games WHERE name NOT IN(" +
                            "SELECT name FROM owned WHERE owner = '" + userName + "' " +
                            "UNION " +
                            "SELECT name FROM wishlist WHERE wishlister = '" + userName + "' " +
                            "UNION " +
                            "SELECT game FROM orders WHERE buyer = '" + userName + "' " +
                            "UNION " +
                            "SELECT game_name FROM user_cart WHERE user_name = 'test' )"
            );
            while (all_games.next()) {
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
                        b2.relocate(550, 8.5);
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
                Label n = new Label(all_games.getString(1));
                n.relocate(10, 12);
                g.getChildren().add(n);
                gameShop.getChildren().add(g);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private EventHandler<ActionEvent> addWishlist = new EventHandler<>() {
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
            try{
                Connection con = ConnectionService.Connect();
                Statement wishlist_game = con.createStatement();
                ResultSet game_information = wishlist_game.executeQuery("SELECT name, developer FROM games WHERE name = '" + g + "'");
                if(game_information.next()){
                    wishlist_game.executeUpdate("INSERT INTO wishlist (name, developer, wishlister) VALUES ('" + game_information.getString(1) + "', '" +
                            game_information.getString(2) + "', '" + userName + "')" );
                }
                game_information.close();
                wishlist_game.close();
                con.close();
            } catch (Exception e){
                e.printStackTrace();
            }
        }
    };

    private EventHandler<ActionEvent> addCart = new EventHandler<>() {
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
            try{
                Connection con = ConnectionService.Connect();
                Statement cart_game = con.createStatement();
                ResultSet game_information = cart_game.executeQuery("SELECT name FROM games WHERE name = '" + g + "'");
                if(game_information.next()){
                    cart_game.executeUpdate("INSERT INTO user_cart VALUES ('" + game_information.getString(1) + "', '" + userName + "')" );
                }
                game_information.close();
                cart_game.close();
                con.close();
            } catch (Exception e){
                e.printStackTrace();
            }
        }
    };

    public void goBack(ActionEvent e) throws IOException {
        FXMLLoader loader = new FXMLLoader(getClass().getClassLoader().getResource("userWelcome.fxml"));
        Parent root = loader.load();
        userWelcomeController uw = loader.getController();
        uw.setDev(userName);
        Stage stage = (Stage) ((Node) e.getSource()).getScene().getWindow();
        Scene scene = new Scene(root);
        stage.setScene(scene);
        stage.setResizable(false);
    }
}
