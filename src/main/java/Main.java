import javafx.event.EventHandler;
import javafx.scene.image.Image;
import javafx.scene.input.MouseEvent;
import javafx.stage.StageStyle;
import services.DeveloperService;
import services.GameService;
import services.UserService;
import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;

public class Main extends Application{
    private double xOffset = 0;
    private double yOffset = 0;

    public static void main(String[] args) {
        launch(args);
    }

    @Override
    public void start(Stage stage) throws Exception {
        UserService.loadUsersFromFile();
        DeveloperService.loadDevelopersFromFile();
        GameService.loadgamesfromfile();
        Parent root = FXMLLoader.load(getClass().getResource("scene.fxml"));
        String css = this.getClass().getResource("styles.css").toExternalForm();
        Scene scene = new Scene(root, 1200, 700);
        scene.getStylesheets().add(css);
        root.setOnMousePressed(new EventHandler<MouseEvent>() {
            @Override
            public void handle(MouseEvent event) {
                xOffset = event.getSceneX();
                yOffset = event.getSceneY();
            }
        });
        root.setOnMouseDragged(new EventHandler<MouseEvent>() {
            @Override
            public void handle(MouseEvent event) {
                stage.setX(event.getScreenX() - xOffset);
                stage.setY(event.getScreenY() - yOffset);
            }
        });
        stage.setResizable(false);
        stage.setTitle("JavaFX and Gradle");
        stage.initStyle(StageStyle.UNDECORATED);
        stage.setScene(scene);
        stage.getIcons().add(new Image("logo.png"));
        stage.show();
    }
}
