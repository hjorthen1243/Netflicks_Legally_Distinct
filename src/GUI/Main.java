package GUI;

import GUI.Controller.MainViewController;
import GUI.Model.MovieModel;
import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;

public class Main extends Application {

    public static void main(String[] args) {
        launch(args);
    }

    @Override
    public void start(Stage primaryStage) throws Exception {
        FXMLLoader loader = new FXMLLoader();
        loader.setLocation(getClass().getResource("View/MainView.fxml"));
        Parent root = loader.load();

        MainViewController controller = loader.getController();
        controller.setModel(new MovieModel());
        controller.setup();

        primaryStage.setTitle("Netflicks");
        primaryStage.setScene(new Scene(root));

        primaryStage.show();
    }
}