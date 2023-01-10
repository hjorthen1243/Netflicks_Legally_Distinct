package GUI.Model;

import BE.Movie;
import BLL.MovieManager;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import java.util.List;


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


    public void searchMovie(String query) throws Exception {
        List<Movie> searchResults = movieManager.searchMovies(query);
    }

        public void createNewMovie (String title, double imdbRating, String pathToFile,int lastView) throws Exception{
        Movie movie = movieManager.createNewMovie(title, imdbRating, pathToFile, lastView);
            moviesToBeViewed.add(movie);
        }

        public void deleteMovie (Movie movie) throws Exception {
            movieManager.deleteMovie(movie);


            moviesToBeViewed.remove(movie);
        }

        public Movie getSelectedMovie () {
            return selectedMovie;
        }

        public void setSelectedMovie (Movie selectedMovie){
            this.selectedMovie = selectedMovie;
        }

        public void updateMovie (Movie updatedMovie) throws Exception {
            movieManager.updateMovie(updatedMovie);

            showList();
        }






        public void showList () throws Exception {
            moviesToBeViewed = FXCollections.observableArrayList();
            moviesToBeViewed.clear();
            moviesToBeViewed.addAll(movieManager.getAllMovies());
        }
}
