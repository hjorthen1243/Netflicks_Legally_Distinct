package GUI.Controller;

import BE.Movie;
import GUI.Model.MovieModel;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.stage.Stage;

import java.net.URL;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.Locale;
import java.util.ResourceBundle;
import java.util.concurrent.TimeUnit;

public class DeleteMovieController extends BaseController implements Initializable {
    @FXML
    private TableColumn titleColumn, yearColumn, lengthColumn, categoryColumn, ratingColumn, pRatingColumn, lastViewColumn;

    private MovieModel movieModel;
    private ObservableList<Movie> observableMovies;
    @FXML
    private Button removemovie;
    @FXML
    private TableView movieTable;
    @FXML
    private Button btnYes;
    @FXML
    private Button btnNo;

    @Override
    public void setup() {
        movieModel = getModel().getMovieModel();
    }
    @Override
    public void initialize(URL location, ResourceBundle resources) {
        disableEnableComponents(true);
        observableMovies = FXCollections.observableArrayList();
        updateMovieList();
        addListenerMovieTable();

    }

    private void disableEnableComponents(Boolean bool) {
        removemovie.setDisable(bool);
    }


    public void deleteAll() {
        try {
            Alert alert = new Alert(Alert.AlertType.CONFIRMATION, "Remove all the movies with a personal" +
                    "rating below 6 and have not been seen the last 2 years", ButtonType.YES, ButtonType.NO);
            alert.showAndWait();
            if (alert.getResult() == ButtonType.YES) {
                for (Movie m: observableMovies) {
                    movieModel.deleteMovie(m);
                    observableMovies.remove(m);
                }

            }
        }catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    public void deleteMovie() {
        try {
            Movie m = (Movie) movieTable.getSelectionModel().getSelectedItem();
            Alert alert = new Alert(Alert.AlertType.CONFIRMATION, "Remove; " + m.getTitle() + " - " + m.getYearString() + "?", ButtonType.YES, ButtonType.NO);
            alert.showAndWait();
            if (alert.getResult() == ButtonType.YES) {
                movieModel.deleteMovie(m);
                observableMovies.remove(m);
            }
        }catch (Exception e) {
            throw new RuntimeException(e);
        }

    }

    public void updateMovieList() {

        try {
            ArrayList<Movie> movies = new ArrayList<>();
            movieModel = new MovieModel();
            Date currentDate = new Date();

            movies = movieModel.getMovies(movies);
            for (Movie movie: movies) {
                Date moviedate = movie.getLastViewDate();
                long diffInMillies = Math.abs(currentDate.getTime() - moviedate.getTime());
                long diff = TimeUnit.DAYS.convert(diffInMillies, TimeUnit.MILLISECONDS);
                long biggestDiff = 730;
                if(diff > biggestDiff && movie.getPersonalRating() < 6){
                    observableMovies.add(movie);
                }
            }
            Methods.setValues(titleColumn, yearColumn, lengthColumn, ratingColumn, pRatingColumn, lastViewColumn, movieTable);
            movieTable.setItems(observableMovies);


        } catch (Exception e) {
            e.printStackTrace();
        }
    }
    private void addListenerMovieTable() {
        movieTable.getSelectionModel().selectedItemProperty().addListener((observable, oldValue, newValue) -> {
            //If something is selected, buttons will be enabled, else they will be disabled
            if (newValue != null) {
                disableEnableComponents(false);
                System.out.println("\nMovie chosen" + movieTable.getSelectionModel().getSelectedItem());

            } else {
                disableEnableComponents(true);
            }
        });
    }

}
