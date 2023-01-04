import GUI.Controller.DeleteMovieController;
import GUI.Model.Model;
import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.layout.AnchorPane;
import javafx.stage.Stage;

public class Main extends Application {
    public static void main(String[] args) {
        launch(args);
    }
    @Override
    public void start(Stage primaryStage) throws Exception {
        Parent root = FXMLLoader.load(getClass().getResource("/GUI/View/DeleteMovie.fxml"));
        Scene scene = new Scene(root); primaryStage.setResizable(false);
        primaryStage.setScene(scene);
        primaryStage.setTitle("Del");
        primaryStage.show();
    }
}