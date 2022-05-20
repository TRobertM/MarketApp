import services.UserService;
import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;

public class Main extends Application{
    public static void main(String[] args) {
        launch(args);
    }

    @Override
    public void start(Stage stage) throws Exception {
        UserService.loadUsersFromFile();
        Parent root = FXMLLoader.load(getClass().getResource("scene.fxml"));
        String css = this.getClass().getResource("styles.css").toExternalForm();
        Scene scene = new Scene(root, 1200, 700);
        scene.getStylesheets().add(css);
        stage.setResizable(false);
        stage.setTitle("JavaFX and Gradle");
        stage.setScene(scene);
        stage.show();
    }
}
