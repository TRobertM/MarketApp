package controllers;

import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.Pane;
import javafx.scene.layout.VBox;
import services.ConnectionService;
import java.net.URL;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.ResourceBundle;

public class userGamesController extends BaseController implements Initializable {
    @FXML
    VBox myGames;

    String userName = BaseController.getCurrentUser();

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        try{
            Connection con = ConnectionService.getConnection();
            PreparedStatement get_games = con.prepareStatement("SELECT name FROM owned WHERE owner = ?");
            get_games.setString(1 , userName);
            ResultSet user_games = get_games.executeQuery();
            while(user_games.next()){
                Pane g = new Pane();
                g.setOnMouseEntered(new EventHandler<MouseEvent>() {

                    @Override
                    public void handle(MouseEvent t) {
                        g.setStyle("-fx-background-color:#160e36;");
                        Button b = new Button("X");
                        b.setOnAction(removeGame);
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
                Label n = new Label(user_games.getString(1));
                n.relocate(10, 12);
                g.getChildren().add(n);
                myGames.getChildren().add(g);
            }
            user_games.close();
            get_games.close();
            con.close();
        } catch(Exception e){
            e.printStackTrace();
        }
    }

    private EventHandler<ActionEvent> removeGame = new EventHandler<>() {
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
                PreparedStatement remove_game = con.prepareStatement("DELETE FROM owned WHERE owner = ? and name = ?");
                remove_game.setString(1 , userName);
                remove_game.setString(2, g);
                remove_game.executeUpdate();
                remove_game.close();
                con.close();
            } catch (Exception e){
                e.printStackTrace();
            }
        }
    };
}
