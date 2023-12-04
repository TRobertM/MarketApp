package services;

import controllers.BaseController;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;

import java.io.IOException;

public class FXMLChangerService {
    public static void changeSceneWithData(String path, Node node, String username) throws IOException {
        FXMLLoader loader = new FXMLLoader(FXMLChangerService.class.getClassLoader().getResource(path));
        BaseController.setCurrentUser(username);
        Parent root = loader.load();
        Scene scene = new Scene(root);
        Stage stage = (Stage) node.getScene().getWindow();
        stage.setScene(scene);
        stage.show();
    }

    public static void changeScene(String path, Node node) throws IOException {
        FXMLLoader loader = new FXMLLoader(FXMLChangerService.class.getClassLoader().getResource(path));
        Parent root = loader.load();
        Scene scene = new Scene(root);
        Stage stage = (Stage) node.getScene().getWindow();
        stage.setScene(scene);
        stage.show();
    }
}
