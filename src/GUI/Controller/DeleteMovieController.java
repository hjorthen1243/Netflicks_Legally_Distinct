package GUI.Controller;

import BE.Movie;
import GUI.Model.MovieModel;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.*;

import java.net.URL;
import java.util.ArrayList;
import java.util.Date;
import java.util.ResourceBundle;
import java.util.concurrent.TimeUnit;

public class DeleteMovieController extends BaseController implements Initializable {
    @FXML
    private TableColumn titleColumn, yearColumn, lengthColumn, categoryColumn, ratingColumn, pRatingColumn, lastViewColumn;

    private MovieModel movieModel;
    private ObservableList<Movie> observableMovies;
    private ArrayList<Movie> movies;
    @FXML
    private Button removemovie;
    @FXML
    private TableView movieTable;
    private boolean isStarting = true;
    private ArrayList<Movie> moviesToDelete;

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
                for (Movie m: moviesToDelete) {
                    System.out.println("Movie to delete: " + m);
                    movieModel.deleteMovie(m);
                }
                observableMovies.clear();
                updateMovieList();
            }
        }catch (Exception e) {
            e.printStackTrace();
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
                updateMovieList();
            }
        }catch (Exception e) {
            e.printStackTrace();
        }

    }

    public void updateMovieList() {

        try {
            if(isStarting) {
                isStarting = false;
                movies = new ArrayList<>();
                moviesToDelete = new ArrayList<>();
                movieModel = new MovieModel();
                Date currentDate = new Date();
                movies = movieModel.getMovies();
                for (Movie movie : movies) {
                    Date moviedate = movie.getLastViewDate();
                    long diffInMillies = Math.abs(currentDate.getTime() - moviedate.getTime());
                    long diff = TimeUnit.DAYS.convert(diffInMillies, TimeUnit.MILLISECONDS);
                    long biggestDiff = 730;
                    if (diff > biggestDiff && movie.getPersonalRating() < 6) {
                        observableMovies.add(movie);
                        moviesToDelete.add(movie);

                        Methods.setValues(titleColumn, yearColumn, lengthColumn, ratingColumn, pRatingColumn, lastViewColumn, movieTable);
                    }
                }
            }

            for (Movie m: observableMovies) {
                System.out.println("Movies to be added to table: " + m);
            }
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
