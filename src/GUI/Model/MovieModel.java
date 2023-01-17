package GUI.Model;

import BE.Category;
import BE.Movie;
import BLL.MovieManager;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;

import java.sql.Date;
import java.util.ArrayList;
import java.util.List;

public class MovieModel {

    private ArrayList<Movie> movies;
    private ObservableList<Movie> moviesToBeViewed;

    private MovieManager movieManager;

    private Movie selectedMovie;

    public MovieModel() throws Exception {
        movieManager = new MovieManager();
        moviesToBeViewed = FXCollections.observableArrayList();
        moviesToBeViewed.addAll(movieManager.getAllMovies());
    }

    public ObservableList<Movie> getObservableMoviesCategory(Category category) throws Exception {
        moviesToBeViewed.addAll(movieManager.getAllMoviesCategory(category));
        return moviesToBeViewed;
    }
    public ObservableList<Movie> getObservableMovies() {
        return moviesToBeViewed;
    }

    public ObservableList<Movie> getAllMovies() throws Exception {
        moviesToBeViewed.clear();
        moviesToBeViewed.addAll(movieManager.getAllMovies());
        return moviesToBeViewed;
    }
    public ArrayList<Movie> getMovies() throws Exception {
        movies = (ArrayList<Movie>) movieManager.getAllMovies();
        return movies;
    }

    public void searchMovie(String query) throws Exception {
        //List<Movie> searchResults = movieManager.searchMovies(query);

    }

    public Movie addNewMovie (String title, int year, String length, double imdbRating, int personalRating, Date lastViewDate, String pathToFile) throws Exception{
        Movie movie = movieManager.createNewMovie(title, year, length, imdbRating, personalRating, lastViewDate, pathToFile);
        moviesToBeViewed.add(movie);
        return movie;
    }

    public void deleteMovie (Movie m) throws Exception {
        movieManager.deleteMovie(m);
        moviesToBeViewed.remove(m);
    }
    public Movie getSelectedMovie () {
        return selectedMovie;
    }

    public void setSelectedMovie (Movie selectedMovie){
        this.selectedMovie = selectedMovie;
    }
    public void updateMovie(Movie updatedMovie) throws Exception {
        movieManager.updateMovie(updatedMovie);
    }

    public ObservableList<Movie> searchAddMovie(String text) {
        ObservableList<Movie> movieResults = FXCollections.observableArrayList();
        movieResults.addAll(movieManager.searchAddMovie(text));
        return movieResults;
    }

    public Movie searchSelectedMovie(String imdbID) {
        return movieManager.searchSelectedMovie(imdbID);
    }

}


