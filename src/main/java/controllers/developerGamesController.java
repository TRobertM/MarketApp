package controllers;

import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.layout.Pane;
import javafx.scene.layout.VBox;
import services.DatabaseDataService;
import java.net.URL;
import java.sql.*;
import java.util.ResourceBundle;

public class developerGamesController extends BaseController implements Initializable {

    @FXML
    VBox gameShop;
    @FXML
    Pane warningPane;
    @FXML
    Button backButton;

    @Override
    public void initialize(URL location, ResourceBundle resources){
        warningPane.setVisible(false);
        try {
            for(String game : DatabaseDataService.retrieveGames(this.getCurrentUser())){
                Pane gamePane = createGamePane(game);
                gameShop.getChildren().add(gamePane);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    private EventHandler<ActionEvent> removeGame = new EventHandler<>() {
        public void handle(ActionEvent event) {
            try {
                if (DatabaseDataService.hasOrders(BaseController.getCurrentUser())) {
                    warningPane.setVisible(true);
                } else {
                    Button sourceButton = (Button) event.getSource();
                    DatabaseDataService.removeGame((String)sourceButton.getUserData());
                    removeGamePane(sourceButton);
                }
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }
    };

    private void removeGamePane(Button sourceButton) {
        Pane pane = (Pane) sourceButton.getParent();
        gameShop.getChildren().remove(pane);
        pane.getChildren().clear();
    }

    private Pane createGamePane(String gameName) {
        Pane g = new Pane();
        g.getChildren().add(createButton("X", gameName));
        g.setMinHeight(40);
        g.setMinWidth(528);

        Label n = new Label(gameName);
        n.relocate(10, 12);
        g.getChildren().add(n);

        g.setOnMouseEntered(event -> handleMouseEntered(g));
        g.setOnMouseExited(event -> handleMouseExited(g));

        return g;
    }

    private Button createButton(String text, String gameName){
        Button b = new Button(text);
        b.setUserData(gameName);
        b.setOnAction(removeGame);
        b.relocate(590, 8.5);
        b.setVisible(false);
        return b;
    }

    private void handleMouseEntered(Pane pane) {
        pane.setStyle("-fx-background-color:#160e36;");
        pane.getChildren().get(0).setVisible(true);
    }

    private void handleMouseExited(Pane pane) {
        pane.setStyle("-fx-background-color:transparent;");
        pane.getChildren().get(0).setVisible(false);
    }
}
