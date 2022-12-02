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
import model.Developer;
import model.Game;
import model.Order;
import model.User;
import services.DeveloperService;
import services.GameService;
import services.UserService;

import java.io.IOException;
import java.sql.Connection;
import java.sql.DriverManager;
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
    int i;

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

    public void setDev(String name){
        devName = name;
        try {
            Connection connection = DriverManager.getConnection("jdbc:postgresql://localhost:5432/postgres", "postgres", "admin");
            Statement check_orders = connection.createStatement();
            ResultSet all_orders = check_orders.executeQuery("SELECT id, game, buyer FROM orders WHERE seller = '" + devName + "'");
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
        } catch (Exception e){
            e.printStackTrace();
        }
    }

    private EventHandler<ActionEvent> declineOrder = new EventHandler<>() {
        public void handle(ActionEvent event) {
            Pane p = new Pane();
            int id = 0;
            if(event.getSource() instanceof Button){
                if(((Button) event.getSource()).getParent() instanceof Pane){
                    for(Node node : ((Pane) ((Button) event.getSource()).getParent()).getChildren()){
                        if(node instanceof Label){
                            id += Integer.valueOf(((Label) node).getText());
                            p = (Pane)node.getParent();
                            allOrders.getChildren().remove(p);
                            break;
                        }
                    }
                }
            }
            try{
                Connection connection = DriverManager.getConnection("jdbc:postgresql://localhost:5432/postgres", "postgres", "admin");
                Statement check_order = connection.createStatement();
                check_order.executeUpdate("DELETE FROM orders WHERE id = '" + id + "'");
                connection.close();
                check_order.close();
            } catch (Exception e){
                e.printStackTrace();
            }
        }
    };

    private EventHandler<ActionEvent> acceptOrder = new EventHandler<>() {
        public void handle(ActionEvent event) {
            Pane p = new Pane();
            int id = 0;
            if(event.getSource() instanceof Button){
                if(((Button) event.getSource()).getParent() instanceof Pane){
                    for(Node node : ((Pane) ((Button) event.getSource()).getParent()).getChildren()){
                        if(node instanceof Label){
                            id += Integer.valueOf(((Label)node).getText());
                            p = (Pane)node.getParent();
                            allOrders.getChildren().remove(p);
                            break;
                        }
                    }
                }
            }
            try{
                Connection connection = DriverManager.getConnection("jdbc:postgresql://localhost:5432/postgres", "postgres", "admin");
                Statement accept_order = connection.createStatement();
                ResultSet order_id = accept_order.executeQuery("SELECT game , buyer , seller FROM orders WHERE id = '" + id + "'");
                if(order_id.next()){
                    accept_order.executeUpdate("INSERT INTO owned (name, developer, owner) VALUES ('" + order_id.getString(1) + "', '"
                                                + order_id.getString(3) + "', '" + order_id.getString(2) + "')");
                    accept_order.executeUpdate("DELETE FROM orders WHERE id = '" + id + "'");
                }
                connection.close();
                accept_order.close();
                order_id.close();
            } catch (Exception e){
                e.printStackTrace();
            }
        }
    };
}
