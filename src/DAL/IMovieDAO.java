package DAL;

import BE.Category;
import BE.Movie;
import com.microsoft.sqlserver.jdbc.SQLServerException;

import java.sql.Date;
import java.util.List;

public interface IMovieDAO {

    List<Movie> getAllMovies() throws Exception;


    Movie addMovie(String title, int year, String length, double imdbRating, int personalRating, Date lastViewed, String pathToFile) throws Exception;

    void editUpdateMovie(Movie movie) throws Exception;

    void deleteMovie(Movie m) throws Exception;

    List<Category> getAllCategories() throws Exception;

    Category createCategory(String name) throws Exception;

    void deleteCategory(Category category) throws Exception;

    List<Movie> getMoviesWithCategory(Category category) throws Exception;

    void addCategoryToMovie(int movieId, int categoryId);

    void removeCategoryFromMovie(int movieId, int categoryId) throws SQLServerException;

    List<Movie> searchAddMovie(String text);

    Movie searchSelectedMovie(String imdbID);

}
