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
        for(Developer dev : DeveloperService.developers){
            if(dev.getUsername().equals(devName)){
                w1.setCurrentDeveloper(dev);
            }
        }
        Stage stage = (Stage) ((Node) e.getSource()).getScene().getWindow();
        Scene scene = new Scene(root);
        stage.setScene(scene);
        stage.setResizable(false);
    }

    public void setDev(String name){
        devName = name;
        for(Developer dev : DeveloperService.developers){
            if(dev.getUsername().equals(devName)){
                break;
            }
            i++;
        }
        for(Order order : DeveloperService.developers.get(i).getOrders()){
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
                        g.getChildren().remove(2);
                        g.getChildren().remove(2);
                    }
                });
                g.setMinHeight(40);
                g.setMinWidth(528);
                Label u = new Label(order.getGameName());
                u.relocate(10, 12);
                g.getChildren().add(u);
                Label gam = new Label("Order from: " + order.getCustomerName());
                gam.relocate(155, 12);
                g.getChildren().add(gam);
                allOrders.getChildren().add(g);
        }
    }

    private EventHandler<ActionEvent> declineOrder = new EventHandler<>() {
        public void handle(ActionEvent event) {
            Pane p = new Pane();
            String g = "";
            if(event.getSource() instanceof Button){
                if(((Button) event.getSource()).getParent() instanceof Pane){
                    for(Node node : ((Pane) ((Button) event.getSource()).getParent()).getChildren()){
                        if(node instanceof Label){
                            g += ((Label) node).getText();
                            p = (Pane)node.getParent();
                            allOrders.getChildren().remove(p);
                            break;
                        }
                    }
                }
            }
            for(Order order : DeveloperService.developers.get(i).getOrders()){
                if(order.getGameName().equals(g)){
                    DeveloperService.developers.get(i).getOrders().remove(order);
                    DeveloperService.persistUsers();
                    break;
                }
            }
        }
    };

    private EventHandler<ActionEvent> acceptOrder = new EventHandler<>() {
        public void handle(ActionEvent event) {
            Pane p = new Pane();
            String g = "";
            int gamePos = 0;
            String userN = "";
            if(event.getSource() instanceof Button){
                if(((Button) event.getSource()).getParent() instanceof Pane){
                    for(Node node : ((Pane) ((Button) event.getSource()).getParent()).getChildren()){
                        if(node instanceof Label){
                            g += ((Label) node).getText();
                            p = (Pane)node.getParent();
                            allOrders.getChildren().remove(p);
                            break;
                        }
                    }
                }
            }
            for(Order order : DeveloperService.developers.get(i).getOrders()){
                if(order.getGameName().equals(g)){
                    DeveloperService.developers.get(i).getOrders().remove(order);
                    userN += order.getCustomerName();
                    DeveloperService.persistUsers();
                    break;
                }
            }
            for(Game game : GameService.games){
                if(game.getName().equals(g)){
                    break;
                }
                gamePos++;
            }
            for(User user : UserService.users){
                if(user.getUsername().equals(userN)){
                    user.getGames().add(GameService.games.get(gamePos));
                    UserService.persistUsers();
                }
            }
        }
    };
}
