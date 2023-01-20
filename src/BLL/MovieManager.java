package BLL;

import BE.Category;
import BE.Movie;
import DAL.IMovieDAO;
import DAL.MovieDAO;

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

    /**
     *
     * Creates list of movies called allMovies and sets the values in the list to be the result of the getAllMovies method.
     * Creates list of movies called searchResult and sets the values to be the result of the search method in the MovieSearcher class
     * when given the list allMovies and the query.
     * Returns searchResult.
     * @throws Exception
     */
    public List<Movie> searchMovies(String query) throws Exception {
        List<Movie> allMovies = getAllMovies();
        return movieSearcher.search(allMovies, query);
    }

    /**
     * Creates list of movies called imdbSearchResult and sets the values to be the result of the searchImdbMin method in the MovieSearcher class
     * when given the list of movies called movies and the string imdbQuery.
     * returns imdbSearchResult.
     */
    public List<Movie> imdbSearchMin(String imdbQuery, List<Movie> movies) {
        return movieSearcher.searchImdbMin(movies, imdbQuery);
    }

    /**
     * Creates list of movies called imdbSearchResult and sets the values to be the result of the searchImdbMax method in the MovieSearcher class
     * when given the list of movies called movies and the string imdbQuery.
     * returns imdbSearchResult.
     */
    public List<Movie> imdbSearchMax(String imdbQuery, List<Movie> movies) {
        return movieSearcher.searchImdbMax(movies, imdbQuery);
    }
    /**
     * Creates list of movies called allMovies and sets the values in the list to be the result of the getAllMovies method.
     * Creates list of movies called imdbSearchResult and sets the values to be the result of the searchImdbMinAndMax method in the
     * MovieSearcher class when given the list allMovies, the string imdbMinStr and the string imdbMaxStr.
     * Returns imdbSearchResult.
     * @throws Exception
     */
    public List<Movie> imdbSearchMinAndMax(String imdbMinStr, String imdbMaxStr) throws Exception {
        List<Movie> allMovies = getAllMovies();
        return movieSearcher.searchImdbMinAndMax(allMovies, imdbMinStr, imdbMaxStr);
    }
    /**
     * Creates list of movies called pRateSearchResult and sets the values to be the result of the searchPRateMin method in the MovieSearcher class
     * when given the list of movies called movies and the string pRateQuery.
     * returns pRateSearchResult.
     */
    public List<Movie> pRateSearchMin(String pRateQuery, List<Movie> movies) {
        return movieSearcher.searchPRateMin(movies, pRateQuery);
    }
    /**
     * Creates list of movies called pRateSearchResult and sets the values to be the result of the searchPRateMax method in the MovieSearcher class
     * when given the list of movies called movies and the string pRateQuery.
     * returns pRateSearchResult.
     */
    public List<Movie> pRateSearchMax(String pRateQuery, List<Movie> movies) {
        return movieSearcher.searchPRateMax(movies, pRateQuery);
    }

    /**
     * Creates list of movies called allMovies and sets the values in the list to be the result of the getAllMovies method.
     * Creates list of movies called pRateSearchResult and sets the values to be the result of the searchPRateMinAndMax method in the
     * MovieSearcher class when given the list allMovies, the string pRateMinStr and the string pRateMaxStr.
     * Returns pRateSearchResult.
     * @throws Exception
     */
    public List<Movie> pRateSearchMinAndMax(String pRateMinStr, String pRateMaxStr) throws Exception {
        List<Movie> allMovies = getAllMovies();
        return movieSearcher.searchPRateMinAndMax(allMovies, pRateMinStr, pRateMaxStr);
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