package BLL;

import BE.Movie;
import DAL.IMovieDAO;
import DAL.MovieDAO;

import java.util.List;

public class MovieManager {
    private final IMovieDAO movieDAO;

    public MovieManager() {
        movieDAO = new MovieDAO();
    }
    public List<Movie> getAllMovies() throws Exception {
        return movieDAO.getAllMovies();
    }
    public void updatedMovie(Movie updatedMovie) {
    }

    public void deleteMovie(Movie movie) {
    }

    public Movie createNewMovie(String title, double imdbRating, double personalRating, Date lastView, String pathToFile) {
        return null;
    }

    public List<Movie> searchMovies(String query) {
        return null;
    }

}
