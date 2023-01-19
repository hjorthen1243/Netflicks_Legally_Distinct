package DAL;

import BE.Category;
import BE.Movie;
import com.microsoft.sqlserver.jdbc.SQLServerException;

import java.sql.Date;
import java.sql.SQLException;
import java.util.List;

public interface IMovieDAO {

    List<Movie> getAllMovies() throws SQLException;

    Movie addMovie(String title, int year, String length, double imdbRating, int personalRating, Date lastViewed, String pathToFile) throws SQLException;

    void editUpdateMovie(Movie movie) throws SQLException;

    void deleteMovie(Movie m) throws SQLException;

    List<Movie> getMoviesWithCategory(Category category) throws SQLException;

    void removeCategoryFromMovie(int movieId, int categoryId) throws SQLException;

    List<Movie> searchAddMovie(String text);

    Movie searchSelectedMovie(String imdbID);

}