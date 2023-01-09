import BE.Movie;
import DAL.MovieDAO;
import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;

import java.util.List;
import java.util.Objects;

public class Main extends Application {

    public static MovieDAO movieDAO;
    public static void main(String[] args) {
        movieDAO = new MovieDAO();
        List<Movie> movies = null;
        try {
            movies = movieDAO.getAllMovies();
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
        for(Movie mov : movies){
            System.out.println(mov.getTitle());
        }

        launch(args);
    }

    @Override
    public void start(Stage primaryStage) throws Exception {
        Parent root = FXMLLoader.load(Objects.requireNonNull(getClass().getResource("/GUI/View/MainView.fxml")));
        //Parent root = FXMLLoader.load(Objects.requireNonNull(getClass().getResource("/GUI/View/PlayMovie.fxml")));
        //Parent root = FXMLLoader.load(Objects.requireNonNull(getClass().getResource("/GUI/View/DeleteMovie.fxml")));
        Scene scene = new Scene(root); primaryStage.setResizable(false);
        primaryStage.setScene(scene);
        primaryStage.setTitle("Netflicks System");
        //primaryStage.setTitle("Delete Movie");
        //primaryStage.setTitle("Play Movie");
        primaryStage.show();
    }
}