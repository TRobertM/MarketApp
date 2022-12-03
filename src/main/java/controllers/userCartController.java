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
import javafx.scene.control.ScrollPane;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.Pane;
import javafx.scene.layout.VBox;
import javafx.scene.text.Text;
import javafx.stage.Stage;
import services.ConnectionService;
import java.io.IOException;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.Statement;

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
    String userName;

    public void setUser(String name){
        userName = name;
        try{
            Connection con = ConnectionService.Connect();
            Statement get_cart = con.createStatement();
            ResultSet cart_games = get_cart.executeQuery("SELECT game_name FROM user_cart WHERE user_name = '" + userName + "'");
            if(cart_games.next() == false){
                textPane.setText("Your cart is empty!");
                textPane.setStyle("-fx-text-fill: white");
                successPane.setVisible(true);
            }
            else{
                successPane.setVisible(false);
                megaPane.setVisible(true);
                do{
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
            try{
                Connection con = ConnectionService.Connect();
                Statement remove_cart = con.createStatement();
                remove_cart.executeUpdate("DELETE FROM user_cart WHERE game_name = '" + g + "' and user_name = '" + userName + "'");
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
            Connection con = ConnectionService.Connect();
            Statement send_orders = con.createStatement();
            PreparedStatement delete_game = con.prepareStatement("DELETE FROM user_cart WHERE game_name = ? and user_name = ?");
            PreparedStatement order_sequence = con.prepareStatement("INSERT INTO orders VALUES(?,?,?,?)");
            PreparedStatement order_seq = con.prepareStatement("SELECT nextval('orders_sq')");
            ResultSet all_orders = send_orders.executeQuery("SELECT games.developer, games.name, user_cart.user_name " +
                    "FROM games INNER JOIN user_cart on games.name = user_cart.game_name WHERE user_cart.user_name = '" + userName + "'");

            while(all_orders.next()){
                ResultSet sequence_number = order_seq.executeQuery();
                if(sequence_number.next()){
                    ID = sequence_number.getInt(1);
                }
                String dev = all_orders.getString(1);
                String gam = all_orders.getString(2);
                String nam = all_orders.getString(3);

                order_sequence.setInt(1, ID);
                order_sequence.setString(2, gam);
                order_sequence.setString(3, nam);
                order_sequence.setString(4, dev);

                order_sequence.executeUpdate();

                delete_game.setString(1, gam);
                delete_game.setString(2, nam);

                delete_game.executeUpdate();
                sequence_number.close();
            }
            all_orders.close();
            send_orders.close();
            delete_game.close();
            order_sequence.close();
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
        uw.setDev(userName);
        Stage stage = (Stage) ((Node) e.getSource()).getScene().getWindow();
        Scene scene = new Scene(root);
        stage.setScene(scene);
        stage.setResizable(false);
    }
}
