package controllers;

import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.ScrollPane;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.Pane;
import javafx.scene.layout.VBox;
import javafx.scene.text.Text;
import services.ConnectionService;
import services.DatabaseDataService;
import java.net.URL;
import java.sql.*;
import java.util.ResourceBundle;

public class userCartController extends BaseController implements Initializable {
    @FXML
    VBox myGames;
    @FXML
    Pane successPane;
    @FXML
    Text textPane;
    @FXML
    ScrollPane megaPane;
    String userName = this.getCurrentUser();

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        try{
            Connection con = ConnectionService.getConnection();
            PreparedStatement get_cart = con.prepareStatement("SELECT game_name FROM user_cart WHERE user_name = ?");
            get_cart.setString(1 , userName);
            ResultSet cart_games = get_cart.executeQuery();
            if(!cart_games.next()){
                textPane.setText("Your cart is empty!");
                textPane.setStyle("-fx-text-fill: white");
                successPane.setVisible(true);
            }
            else{
                successPane.setVisible(false);
                megaPane.setVisible(true);
                do{
                    String gameName = cart_games.getString(1);
                    Pane g = new Pane();
                    g.setOnMouseEntered(new EventHandler<MouseEvent>() {
                        @Override
                        public void handle(MouseEvent onClick) {
                            g.setStyle("-fx-background-color:#160e36;");
                            Button b = new Button("X");
                            b.setOnAction(event -> {
                                DatabaseDataService.removeFromCart(gameName, userName);
                            });
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
                    Label n = new Label(cart_games.getString(1));
                    n.relocate(10, 12);
                    g.getChildren().add(n);
                    myGames.getChildren().add(g);
                } while(cart_games.next());
            }
        } catch(Exception e){
            e.printStackTrace();
        }
    }

    @FXML
    private EventHandler<ActionEvent> removeCart = new EventHandler<>() {
        public void handle(ActionEvent event) {
            Pane p;
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
            try{
                Connection con = ConnectionService.getConnection();
                PreparedStatement remove_cart = con.prepareStatement("DELETE FROM user_cart WHERE game_name = ? and user_name = ?");
                remove_cart.setString(1 , g);
                remove_cart.setString(2, userName);
                remove_cart.executeUpdate();
                remove_cart.close();
                con.close();
            }catch(Exception e){
                e.printStackTrace();
            }
        }
    };

    public void sendOrder(){
        try{
            int ID = 0;
            Connection con = ConnectionService.getConnection();
            Statement send_orders = con.createStatement();
            PreparedStatement delete_game = con.prepareStatement("DELETE FROM user_cart WHERE game_name = ? and user_name = ?");
            PreparedStatement add_order = con.prepareStatement("INSERT INTO orders VALUES(?,?,?,?)");
            PreparedStatement order_seq = con.prepareStatement("SELECT nextval('orders_sq')");
            PreparedStatement select_info = con.prepareStatement("SELECT games.developer, games.name, user_cart.user_name FROM games INNER JOIN user_cart on " +
                    "games.name = user_cart.game_name WHERE user_cart.user_name = ?");
            select_info.setString(1, userName);
            ResultSet all_orders = select_info.executeQuery();

            while(all_orders.next()){
                ResultSet sequence_number = order_seq.executeQuery();
                if(sequence_number.next()){
                    ID = sequence_number.getInt(1);
                }
                String dev = all_orders.getString(1);
                String gam = all_orders.getString(2);
                String nam = all_orders.getString(3);

                add_order.setInt(1, ID);
                add_order.setString(2, gam);
                add_order.setString(3, nam);
                add_order.setString(4, dev);

                add_order.executeUpdate();

                delete_game.setString(1, gam);
                delete_game.setString(2, nam);

                delete_game.executeUpdate();
                sequence_number.close();
            }
            select_info.close();
            all_orders.close();
            send_orders.close();
            delete_game.close();
            add_order.close();
            order_seq.close();
            con.close();
        } catch(Exception e){
            e.printStackTrace();
        }
        textPane.setText("Your cart is empty!");
        textPane.setStyle("-fx-text-fill: white");
        successPane.setVisible(true);
        megaPane.setVisible(false);
    }
}
