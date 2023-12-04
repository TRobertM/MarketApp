package controllers;

import com.fasterxml.jackson.databind.ser.Serializers;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.Pane;
import javafx.scene.layout.VBox;
import services.ConnectionService;
import services.DatabaseDataService;

import java.net.URL;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.ResourceBundle;

public class userLibraryController extends BaseController implements Initializable {
    @FXML
    VBox gameShop;

    String userName = BaseController.getCurrentUser();

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        try {
            Connection con = ConnectionService.getConnection();
            PreparedStatement get_games = con.prepareStatement("SELECT name FROM games WHERE name " +
                    "NOT IN(SELECT name FROM owned WHERE owner = ? " +
                    "UNION SELECT game_name FROM user_cart WHERE user_name = ? " +
                    "UNION SELECT name FROM wishlist WHERE wishlister = ? " +
                    "UNION SELECT game FROM orders WHERE buyer = ?)");
            get_games.setString(1, userName);
            get_games.setString(2, userName);
            get_games.setString(3, userName);
            get_games.setString(4, userName);
            ResultSet all_games = get_games.executeQuery();
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
            get_games.close();
            con.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private EventHandler<ActionEvent> addWishlist = new EventHandler<>() {
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
//                Connection con = ConnectionService.getConnection();
//                PreparedStatement wishlist_game = con.prepareStatement("SELECT name,developer FROM games WHERE name = ?");
//                PreparedStatement insert_wishlist = con.prepareStatement("INSERT INTO wishlist (name,developer,wishlister) VALUES (?,?,?)");
//                wishlist_game.setString(1 , g);
//                ResultSet game_information = wishlist_game.executeQuery();
//                if(game_information.next()){
//                    insert_wishlist.setString(1 , game_information.getString(1));
//                    insert_wishlist.setString(2, game_information.getString(2));
//                    insert_wishlist.setString(3, userName);
//                    insert_wishlist.executeUpdate();
//                }
//                insert_wishlist.close();
//                game_information.close();
//                wishlist_game.close();
//                con.close();
                String developer = DatabaseDataService.retrieveDeveloper(g);
                DatabaseDataService.addToWishlist(g, developer, BaseController.getCurrentUser());
            } catch (Exception e){
                e.printStackTrace();
            }
        }
    };

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
                PreparedStatement cart_game = con.prepareStatement("SELECT name FROM games WHERE name = ?");
                PreparedStatement add_cart = con.prepareStatement("INSERT INTO user_cart (game_name,User_name) VALUES (?,?)");
                cart_game.setString(1, g);
                ResultSet game_information = cart_game.executeQuery();
                if(game_information.next()){
                    add_cart.setString(1, game_information.getString(1));
                    add_cart.setString(2, userName);
                    add_cart.executeUpdate();
                }
                game_information.close();
                add_cart.close();
                cart_game.close();
                con.close();
            } catch (Exception e){
                e.printStackTrace();
            }
        }
    };
}
