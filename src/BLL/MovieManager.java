package BLL;

import BE.Category;
import BE.Movie;
import DAL.CategoryDAO;
import DAL.IMovieDAO;
import DAL.MovieDAO;
import com.microsoft.sqlserver.jdbc.SQLServerException;

import java.io.IOException;
import java.sql.Date;
import java.sql.SQLException;
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

    public List<Movie> getAllMovies() throws SQLException {
        return movieDAO.getAllMovies();
    }

    public List<Movie> searchMovies(String query) throws Exception {
        List<Movie> allMovies = getAllMovies();
        List<Movie> searchResult = movieSearcher.search(allMovies, query);
        return searchResult;
    }

    public List<Movie> imdbSearchMin(String imdbQuery, List<Movie> movies) {
        List<Movie> imdbSearchResult = movieSearcher.searchImdbMin(movies, imdbQuery);
        return imdbSearchResult;
    }

    public List<Movie> imdbSearchMax(String imdbQuery, List<Movie> movies) {
        List<Movie> imdbSearchResult = movieSearcher.searchImdbMax(movies, imdbQuery);
        return imdbSearchResult;
    }

    public List<Movie> imdbSearchMinAndMax(String imdbMinStr, String imdbMaxStr) throws Exception {
        List<Movie> allMovies = getAllMovies();
        List<Movie> imdbSearchResult = movieSearcher.searchImdbMinAndMax(allMovies, imdbMinStr, imdbMaxStr);
        return imdbSearchResult;
    }

    public List<Movie> pRateSearchMin(String pRateQuery, List<Movie> movies) {
        List<Movie> pRateSearchResult = movieSearcher.searchPRateMin(movies, pRateQuery);
        return pRateSearchResult;
    }

    public List<Movie> pRateSearchMax(String pRateQuery, List<Movie> movies) {
        List<Movie> pRateSearchResult = movieSearcher.searchPRateMax(movies, pRateQuery);
        return pRateSearchResult;
    }

    public List<Movie> pRateSearchMinAndMax(String pRateMinStr, String pRateMaxStr) throws Exception {
        List<Movie> allMovies = getAllMovies();
        List<Movie> pRateSearchResult = movieSearcher.searchPRateMinAndMax(allMovies, pRateMinStr, pRateMaxStr);
        return pRateSearchResult;
    }

    public Movie createNewMovie(String title, int year, String length, double imdbRating, int personalRating, Date lastView, String pathToFile) throws SQLException {
        return movieDAO.addMovie(title, year, length, imdbRating, personalRating, lastView, pathToFile);
    }

    public void deleteMovie(Movie m) throws SQLException {
        movieDAO.deleteMovie(m);
    }

    public void updateMovie(Movie updatedMovie) throws Exception {
        movieDAO.editUpdateMovie(updatedMovie);
    }

    public List<Movie> searchAddMovie(String text) {
        return movieDAO.searchAddMovie(text);
    }

    public void removeCategoryFromMovie(int movieId, int categoryId) throws SQLException {
        movieDAO.removeCategoryFromMovie(movieId, categoryId);
    }

    public Movie searchSelectedMovie(String imdbID) {
        return movieDAO.searchSelectedMovie(imdbID);
    }
}