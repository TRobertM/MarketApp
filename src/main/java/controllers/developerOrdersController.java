package controllers;

import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.layout.Pane;
import javafx.scene.layout.VBox;
import model.Order;
import services.DatabaseDataService;
import java.net.URL;
import java.sql.*;
import java.util.List;
import java.util.ResourceBundle;

public class developerOrdersController extends BaseController implements Initializable {
    @FXML
    Button backButton;
    @FXML
    VBox allOrders;

    String devName = BaseController.getCurrentUser();

    @Override
    public void initialize(URL location, ResourceBundle resources){
        try {
            createPane(DatabaseDataService.retrieveOrders(devName));
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    private void createPane(List<Order> orders) throws SQLException {
        for(Order order : orders){
            Pane gamePane = new Pane();

            Button acceptButton = createAcceptButton("Accept", order.getID());
            Button declineButton = createDeclineButton("X", order.getID());

            Label id = new Label(String.valueOf(order.getID()));
            Label buyer = new Label(order.getCustomerName());
            Label game = new Label(order.getGameName());

            id.relocate(5, 12.25);
            buyer.relocate(25, 12);
            game.relocate(200, 12);
            gamePane.getChildren().addAll(acceptButton, declineButton, id, buyer, game);
            gamePane.setMinHeight(40);
            gamePane.setMinWidth(528);

            gamePane.setOnMouseEntered(event -> handleMouseEntered(gamePane));
            gamePane.setOnMouseExited(event -> handleMouseExited(gamePane));
            allOrders.getChildren().add(gamePane);
        }
    }

    private Button createAcceptButton(String text, int ID){
        Button b = new Button(text);
        b.setUserData(ID);
        b.setOnAction(acceptOrder);
        b.relocate(500, 8.5);
        b.setVisible(false);
        return b;
    }

    private Button createDeclineButton(String text, int ID){
        Button b = new Button(text);
        b.setUserData(ID);
        b.setOnAction(declineOrder);
        b.relocate(590, 8.5);
        b.setVisible(false);
        return b;
    }

    private final EventHandler<ActionEvent> declineOrder = event -> {
        Button sourceButton = (Button)event.getSource();
        DatabaseDataService.removeOrder((int)sourceButton.getUserData());
        removePane(sourceButton);
    };

    private final EventHandler<ActionEvent> acceptOrder = event -> {
        Button sourceButton = (Button)event.getSource();
        try {
            Order information = DatabaseDataService.retrieveOrderInformation((int)sourceButton.getUserData());
            DatabaseDataService.buyGame(information.getCustomerName(), information.getGameName(), information.getDeveloperName());
            DatabaseDataService.removeOrder((int)sourceButton.getUserData());
        } catch (SQLException e) {
            e.printStackTrace();
        }
        removePane(sourceButton);
    };

    private void removePane(Button sourceButton) {
        allOrders.getChildren().remove(sourceButton.getParent());
        ((Pane)sourceButton.getParent()).getChildren().clear();
    }


    private void handleMouseEntered(Pane pane) {
        pane.setStyle("-fx-background-color:#160e36;");
        pane.getChildren().get(0).setVisible(true);
        pane.getChildren().get(1).setVisible(true);
    }

    private void handleMouseExited(Pane pane) {
        pane.setStyle("-fx-background-color:transparent;");
        pane.getChildren().get(0).setVisible(false);
        pane.getChildren().get(1).setVisible(false);
    }
}
