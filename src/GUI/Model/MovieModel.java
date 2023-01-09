package GUI.Model;

import BE.Movie;
import BLL.MovieManager;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;

import java.util.Date;

public class MovieModel {

    private ObservableList<Movie> moviesToBeViewed;

    private MovieManager movieManager;

    private Movie selectedMovie;

    public MovieModel() throws Exception {
        movieManager = new MovieManager();
        showList();
    }

    public ObservableList<Movie> getObservableMovies() throws Exception {
        showList();
        return moviesToBeViewed;
    }

    public void searchMovie(String query){
        //List<Movie> searchResults = movieManager.searchMovies(query);
        moviesToBeViewed.clear();
        //moviesToBeViewed.addAll(searchResults);
    }
    public void createNewMovie(String title, double imdbRating, double personalRating, Date lastView, String pathToFile){
        //Movie movie =movieManager.createNewMovie(title, imdbRating, personalRating, lastView, pathToFile);
        //moviesToBeViewed.add(movie);
    }

    public void deleteMovie(Movie movie){
        //movieManager.deleteMovie(movie);
        moviesToBeViewed.remove(movie);
    }

    public Movie getSelectedMovie(){return selectedMovie;}

    public void setSelectedMovie(Movie selectedMovie){this.selectedMovie = selectedMovie;}

    public void updateMovie(Movie updatedMovie){
        //movieManager.updatedMovie(updatedMovie);
    }

    public void showList() throws Exception {
    moviesToBeViewed = FXCollections.observableArrayList();
    moviesToBeViewed.addAll(movieManager.getAllMovies());
    }
}
