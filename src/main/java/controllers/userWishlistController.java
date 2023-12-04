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
import services.ConnectionService;
import services.DatabaseConnectionService;

import java.io.IOException;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.Statement;

public class userWishlistController extends BaseController {
    @FXML
    VBox gameShop;

    String userName = BaseController.getCurrentUser();

    public void setUser(String name) {
        try{
            Connection con = ConnectionService.getConnection();
            PreparedStatement get_cart = con.prepareStatement("SELECT name FROM wishlist WHERE wishlister = ?");
            get_cart.setString(1, userName);
            ResultSet cart_games = get_cart.executeQuery();
            while(cart_games.next()){
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

                        Button b2 = new Button("X");
                        b2.setOnAction(removeCart);
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
                Label n = new Label(cart_games.getString(1));
                n.relocate(10, 12);
                g.getChildren().add(n);
                gameShop.getChildren().add(g);
            }
            cart_games.close();
            get_cart.close();
            con.close();
        } catch (Exception e){
            e.printStackTrace();
        }

    }

    private EventHandler<ActionEvent> addCart = new EventHandler<>() {
        public void handle(ActionEvent event) {
            Pane p;
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
                Connection con = ConnectionService.getConnection();
                PreparedStatement add_cart = con.prepareStatement("INSERT INTO user_cart VALUES (?,?)");
                add_cart.setString(1 , g);
                add_cart.setString(2 , userName);
                add_cart.executeUpdate();
                PreparedStatement delete_game = con.prepareStatement("DELETE FROM wishlist WHERE wishlister = ? and name = ?");
                delete_game.setString(1, userName);
                delete_game.setString(2 , g);
                delete_game.executeUpdate();
                add_cart.close();
                delete_game.close();
                con.close();
            } catch (Exception e){
                e.printStackTrace();
            }
        }
    };

    private EventHandler<ActionEvent> removeCart = new EventHandler<>() {
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
            try{
                Connection con = ConnectionService.getConnection();
                PreparedStatement remove_cart = con.prepareStatement("DELETE FROM wishlist WHERE wishlister = ? and name = ?");
                remove_cart.setString(1 , userName);
                remove_cart.setString(2 , g);
                remove_cart.executeUpdate();
                remove_cart.close();
                con.close();
            } catch (Exception e){
                e.printStackTrace();
            }
        }
    };

}
