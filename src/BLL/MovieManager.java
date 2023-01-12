package BLL;

import BE.Category;
import BE.Movie;
import DAL.IMovieDAO;
import DAL.MovieDAO;

import java.sql.Date;
import java.util.List;

public class MovieManager {

    private final IMovieDAO movieDAO;
    private MovieSearcher movieSearcher = new MovieSearcher();

    public MovieManager() {
        movieDAO = new MovieDAO();
    }


    public List<Movie> getAllMoviesCategory(Category category) throws Exception {
        return movieDAO.getMoviesWithCategory(category);
    }

    public List<Movie> getAllMovies() throws Exception {
        return movieDAO.getAllMovies();
    }

    public List<Movie> searchMovies(String query) throws Exception {
        List<Movie> allMovies = getAllMovies();
        List<Movie> searchResult = movieSearcher.search(allMovies, query);
        return searchResult;
    }

    public Movie createNewMovie(String title, int year, String length, double imdbRating, int personalRating, Date lastView, String pathToFile) throws Exception {
        return movieDAO.addMovie(title, year, length, imdbRating, personalRating, lastView, pathToFile);
    }

    public void deleteMovie(Movie movie) throws Exception {
        movieDAO.deleteMovie(movie);
    }


    public void updateMovie(Movie updatedMovie) throws Exception {
        movieDAO.editUpdateMovie(updatedMovie);
    }
    public List<Movie> searchAddMovie(String text) {
        return movieDAO.searchAddMovie(text);
    }

    public Movie searchSelectedMovie(String imdbID) {
        return movieDAO.searchSelectedMovie(imdbID);
    }

    public String getMovieCategories() {
        return movieDAO.getMovieCategories();
    }
}