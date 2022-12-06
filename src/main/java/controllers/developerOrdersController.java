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
import services.ConnectionService;

import java.io.IOException;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.Statement;

public class developerOrdersController {
    @FXML
    Button minimizeButton;
    @FXML
    Button closeButton;
    @FXML
    Button backButton;
    @FXML
    VBox allOrders;

    String devName;
    int id = 0;

    public void closeWindow(){
        Stage stage = (Stage) closeButton.getScene().getWindow();
        stage.close();
    }

    public void minimizeWindow(){
        Stage stage = (Stage) minimizeButton.getScene().getWindow();
        stage.setIconified(true);
    }

    public void goBack(ActionEvent e) throws IOException {
        FXMLLoader loader = new FXMLLoader(getClass().getClassLoader().getResource("developerWelcome.fxml"));
        Parent root = loader.load();
        developerWelcomeController w1 = loader.getController();
        w1.setCurrentDeveloper(devName);
        Stage stage = (Stage) ((Node) e.getSource()).getScene().getWindow();
        Scene scene = new Scene(root);
        stage.setScene(scene);
        stage.setResizable(false);
    }

    /* Reads the orders database and appends all orders directed at the currently logged in developer in a pane together with
    2 buttons, one for approving the order and one for declining it
     */
    public void setDev(String name){
        devName = name;
        try {
            Connection con = ConnectionService.Connect();
            PreparedStatement check_orders = con.prepareStatement("SELECT id,game,buyer FROM orders WHERE seller = ?");
            check_orders.setString(1, devName);
            ResultSet all_orders = check_orders.executeQuery();
            while(all_orders.next()) {
                Pane g = new Pane();
                g.setOnMouseEntered(new EventHandler<MouseEvent>() {

                    @Override
                    public void handle(MouseEvent t) {
                        g.setStyle("-fx-background-color:#160e36;");
                        Button b = new Button("X");
                        b.setOnAction(declineOrder);
                        b.relocate(590, 8.5);
                        g.getChildren().add(b);

                        Button b2 = new Button("Approve");
                        b2.setOnAction(acceptOrder);
                        b2.relocate(500, 8.5);
                        g.getChildren().add(b2);
                    }
                });
                g.setOnMouseExited(new EventHandler<MouseEvent>() {

                    @Override
                    public void handle(MouseEvent t) {
                        g.setStyle("-fx-background-color:transparent;");
                        g.getChildren().remove(3);
                        g.getChildren().remove(3);
                    }
                });
                g.setMinHeight(40);
                g.setMinWidth(528);
                Label id = new Label(Integer.toString(all_orders.getInt(1)));
                Label u = new Label(all_orders.getString(2));
                u.relocate(25, 12);
                id.relocate(5, 12.25);
                g.getChildren().add(id);
                g.getChildren().add(u);
                Label gam = new Label("Order from: " + all_orders.getString(3));
                gam.relocate(200, 12);
                g.getChildren().add(gam);
                allOrders.getChildren().add(g);
            }
            con.close();
            check_orders.close();
            all_orders.close();
        } catch (Exception e){
            e.printStackTrace();
        }
    }

    // Declines the order, the user will be able to place another one
    private EventHandler<ActionEvent> declineOrder = new EventHandler<>() {
        public void handle(ActionEvent event) {
            Pane p;
            if(event.getSource() instanceof Button){
                if(((Button) event.getSource()).getParent() instanceof Pane){
                    for(Node node : ((Pane) ((Button) event.getSource()).getParent()).getChildren()){
                        if(node instanceof Label){
                            id = Integer.valueOf(((Label) node).getText());
                            p = (Pane)node.getParent();
                            allOrders.getChildren().remove(p);
                            break;
                        }
                    }
                }
            }
            try{
                Connection con = ConnectionService.Connect();
                PreparedStatement check_order = con.prepareStatement("DELETE FROM orders WHERE id = ?");
                check_order.setInt(1, id);
                check_order.executeUpdate();
                con.close();
                check_order.close();
            } catch (Exception e){
                e.printStackTrace();
            }
        }
    };

    // Accepts the order and edits the owned games database so the user now owns the game it ordered
    private EventHandler<ActionEvent> acceptOrder = new EventHandler<>() {
        public void handle(ActionEvent event) {
            Pane p;
            if(event.getSource() instanceof Button){
                if(((Button) event.getSource()).getParent() instanceof Pane){
                    for(Node node : ((Pane) ((Button) event.getSource()).getParent()).getChildren()){
                        if(node instanceof Label){
                            id = Integer.valueOf(((Label)node).getText());
                            p = (Pane)node.getParent();
                            allOrders.getChildren().remove(p);
                            break;
                        }
                    }
                }
            }
            try{
                Connection con = ConnectionService.Connect();
                PreparedStatement accept_order = con.prepareStatement("SELECT game,buyer,seller FROM orders WHERE id = ?");
                accept_order.setInt(1,id);
                ResultSet order_id = accept_order.executeQuery();
                PreparedStatement add_owned = con.prepareStatement("INSERT INTO owned (name,owner,developer) VALUES (?,?,?)");
                PreparedStatement delete_order = con.prepareStatement("DELETE FROM orders WHERE id = ?");
                if(order_id.next()){
                    add_owned.setString(1, order_id.getString(1));
                    add_owned.setString(2, order_id.getString(2));
                    add_owned.setString(3, order_id.getString(3));
                    add_owned.executeUpdate();
                }
                delete_order.setInt(1, id);
                delete_order.executeUpdate();
                con.close();
                accept_order.close();
                order_id.close();
            } catch (Exception e){
                e.printStackTrace();
            }
        }
    };
}
