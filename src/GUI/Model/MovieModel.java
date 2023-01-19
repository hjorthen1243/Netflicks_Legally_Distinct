package GUI.Model;

import BE.Category;
import BE.Movie;
import BLL.MovieManager;
import com.microsoft.sqlserver.jdbc.SQLServerException;
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

    /**
     *
     * Creates list of movies called searchResults and sets the values of the list to be the result of the method searchMovies
     * in the MovieManager class when given the query.
     * Removes all movies from the moviesToBeViewed list.
     * Adds all movies from the imdbSearchResult list in the moviesToBeViewed list.
     * @throws Exception
     */
    public void searchMovie(String query) throws Exception {
        List<Movie> searchResults = movieManager.searchMovies(query);
        moviesToBeViewed.clear();
        moviesToBeViewed.addAll(searchResults);

    }

    /**
     * Makes the list moviesToBeViewed into an observable list.
     * Creates list of movies called movies1 by making the substitute of the observable list called movies into a list.
     * Creates list of movies called imdbSearchResult and sets the values of the list to be the result of the method
     * imdbSearchMin in the MovieManager class when given the string imdbQuery and the list movies1.
     * Removes all movies from the moviesToBeViewed list.
     * Adds all movies from the imdbSearchResult list in the moviesToBeViewed list.
     * returns moviesToBeViewed list.
     * @throws Exception
     */
    public ObservableList<Movie> imdbSearchMin(String imdbQuery, ObservableList<Movie> movies) throws Exception {
        ObservableList<Movie> moviesToBeViewed = FXCollections.observableArrayList();
        List<Movie> movies1 = movies.subList(0, movies.size());
        List<Movie> imdbSearchResult = movieManager.imdbSearchMin(imdbQuery, movies1);
        moviesToBeViewed.clear();
        moviesToBeViewed.addAll(imdbSearchResult);
        return moviesToBeViewed;
    }

    /**
     * Makes the list moviesToBeViewed into an observable list.
     * Creates list of movies called movies1 by making the substitute of the observable list called movies into a list.
     * Creates list of movies called imdbSearchResult and sets the values of the list to be the result of the method
     * imdbSearchMax in the MovieManager class when given the string imdbQuery and the list movies1.
     * Removes all movies from the moviesToBeViewed list.
     * Adds all movies from the imdbSearchResult list in the moviesToBeViewed list.
     * Returns moviesToBeViewed list.
     * @throws Exception
     */
    public ObservableList<Movie> imdbSearchMax(String imdbQuery, ObservableList<Movie> movies) throws Exception {
        ObservableList<Movie> moviesToBeViewed = FXCollections.observableArrayList();
        List<Movie> movies1 = movies.subList(0, movies.size());
        List<Movie> imdbSearchResult = movieManager.imdbSearchMax(imdbQuery, movies1);
        moviesToBeViewed.clear();
        moviesToBeViewed.addAll(imdbSearchResult);
        return moviesToBeViewed;
    }
    /**
     * Makes moviesToBeViewed list into an observable list.
     * Creates list called imdbSearchResult and sets the values of the list to be the result of the method
     * imdbSearchMinAndMax when given the string imdbMinStr and the string imdbMaxStr.
     * Removes all movies from the moviesToBeViewed list.
     * Adds all movies from the pRateSearchResult list in the moviesToBeViewed list.
     * returns moviesToBeViewed list.
     * @throws Exception
     */
    public ObservableList imdbSearchMinAndMax(String imdbMinStr, String imdbMaxStr) throws Exception {
        ObservableList<Movie> moviesToBeViewed = FXCollections.observableArrayList();
        List<Movie> imdbSearchResult = movieManager.imdbSearchMinAndMax(imdbMinStr, imdbMaxStr);
        moviesToBeViewed.clear();
        moviesToBeViewed.addAll(imdbSearchResult);
        return moviesToBeViewed;
    }
    /**
     * Makes the list moviesToBeViewed into an observable list.
     * Creates list of movies called movies1 by making the substitute of the observable list called movies into a list.
     * Creates list of movies called pRateSearchResult and sets the values of the list to be the result of the method
     * pRateSearchMin in the MovieManager class when given the string pRateQuery and the list movies1.
     * Removes all movies from the moviesToBeViewed list.
     * Adds all movies from the pRateSearchResult list in the moviesToBeViewed list.
     * returns moviesToBeViewed list.
     * @throws Exception
     */
    public ObservableList pRateSearchMin(String pRateQuery, ObservableList<Movie> movies) {
        ObservableList<Movie> moviesToBeViewed = FXCollections.observableArrayList();
        List<Movie> movies1 = movies.subList(0, movies.size());
        List<Movie> pRateSearchResult = movieManager.pRateSearchMin(pRateQuery, movies1);
        moviesToBeViewed.clear();
        moviesToBeViewed.addAll(pRateSearchResult);
        return moviesToBeViewed;
    }
    /**
     * Makes the list moviesToBeViewed into an observable list.
     * Creates list of movies called movies1 by making the substitute of the observable list called movies into a list.
     * Creates list of movies called pRateSearchResult and sets the values of the list to be the result of the method
     * pRateSearchMax in the MovieManager class when given the string pRateQuery and the list movies1.
     * Removes all movies from the moviesToBeViewed list.
     * Adds all movies from the pRateSearchResult list in the moviesToBeViewed list.
     * returns moviesToBeViewed list.
     * @throws Exception
     */
    public ObservableList pRateSearchMax(String pRateQuery, ObservableList<Movie> movies) {
        ObservableList<Movie> moviesToBeViewed = FXCollections.observableArrayList();
        List<Movie> movies1 = movies.subList(0, movies.size());
        List<Movie> pRateSearchResult = movieManager.pRateSearchMax(pRateQuery, movies1);
        moviesToBeViewed.clear();
        moviesToBeViewed.addAll(pRateSearchResult);
        return moviesToBeViewed;
    }

    /**
     * Makes moviesToBeViewed list into an observable list.
     * Creates list called pRateSearchResult and sets the values of the list to be the result of the method
     * pRateSearchMinAndMax when given the string pRateMinStr and the string pRateMaxStr.
     * Removes all movies from the moviesToBeViewed list.
     * Adds all movies from the pRateSearchResult list in the moviesToBeViewed list.
     * returns moviesToBeViewed list.
     * @throws Exception
     */
    public ObservableList pRateSearchMinAndMax(String pRateMinStr, String pRateMaxStr) throws Exception {
        ObservableList<Movie> moviesToBeViewed = FXCollections.observableArrayList();
        List<Movie> pRateSearchResult = movieManager.pRateSearchMinAndMax(pRateMinStr, pRateMaxStr);
        moviesToBeViewed.clear();
        moviesToBeViewed.addAll(pRateSearchResult);
        return moviesToBeViewed;
    }

    public Movie addNewMovie(String title, int year, String length, double imdbRating, int personalRating, Date lastViewDate, String pathToFile) throws Exception {
        Movie movie = movieManager.createNewMovie(title, year, length, imdbRating, personalRating, lastViewDate, pathToFile);
        moviesToBeViewed.add(movie);
        return movie;
    }

    public void deleteMovie(Movie m) throws Exception {
        movieManager.deleteMovie(m);
        moviesToBeViewed.remove(m);
    }

    public Movie getSelectedMovie() {
        return selectedMovie;
    }

    public void setSelectedMovie(Movie selectedMovie) {
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

    public void removeCategoryFromMovie(int movieId, int categoryId) throws SQLServerException {
        movieManager.removeCategoryFromMovie(movieId, categoryId);
    }

    public Movie searchSelectedMovie(String imdbID) {
        return movieManager.searchSelectedMovie(imdbID);
    }

}


