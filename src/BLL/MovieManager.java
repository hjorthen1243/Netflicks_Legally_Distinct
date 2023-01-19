package BLL;

import BE.Category;
import BE.Movie;
import DAL.CategoryDAO;
import DAL.IMovieDAO;
import DAL.MovieDAO;
import com.microsoft.sqlserver.jdbc.SQLServerException;

import java.io.IOException;
import java.sql.Date;
import java.util.List;

public class MovieManager {

    private final IMovieDAO movieDAO;
    private MovieSearcher movieSearcher = new MovieSearcher();

    public MovieManager() throws IOException {
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
    public  List<Movie> imdbSearch(String imdbQuery) throws Exception {
        List<Movie> allMovies = getAllMovies();
        List<Movie> imdbSearchResult = movieSearcher.searchImdb(allMovies, imdbQuery);
        return imdbSearchResult;
    }

    public Movie createNewMovie(String title, int year, String length, double imdbRating, int personalRating, Date lastView, String pathToFile) throws Exception {
        return movieDAO.addMovie(title, year, length, imdbRating, personalRating, lastView, pathToFile);
    }

    public void deleteMovie(Movie m) throws Exception {
        movieDAO.deleteMovie(m);
    }


    public void updateMovie(Movie updatedMovie) throws Exception {
        movieDAO.editUpdateMovie(updatedMovie);
    }
    public List<Movie> searchAddMovie(String text) {
        return movieDAO.searchAddMovie(text);
    }
    public void removeCategoryFromMovie(int movieId, int categoryId) throws SQLServerException{
        movieDAO.removeCategoryFromMovie(movieId, categoryId);
    }
    public Movie searchSelectedMovie(String imdbID) {
        return movieDAO.searchSelectedMovie(imdbID);
    }



}