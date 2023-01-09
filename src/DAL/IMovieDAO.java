package DAL;

import BE.Category;
import BE.Movie;

import java.util.List;

public interface IMovieDAO {

    List<Movie> getAllMovies() throws Exception;

    Movie addMovie(String name, double imdbRating, String pathToFile, int lastViewed) throws Exception;
    void editUpdateMovie(Movie movie) throws Exception;

    void deleteMovie(Movie movie) throws Exception;

    List<Category> getAllCategories() throws Exception;

    Category createCategory(String name) throws Exception;

    void deleteCategory(Category category) throws Exception;

    List<Movie> getMoviesWithCategoryID(int id) throws Exception;

    void addCategoryToMovie(int movieId, int categoryId);

    void removeCategoryFromMovie(int movieId, int categoryId);
}
