package DAL;

import BE.Category;
import com.microsoft.sqlserver.jdbc.SQLServerException;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public interface ICategoryDAO {
    List<Category> getAllCategories() throws Exception;

    Category createNewCategory(String newCategoryName) throws Exception;

    Category editUpdateCategory(String oldCategoryName, Category newCategoryName) throws Exception;

    void removeCategory(Category categoryName) throws Exception;

    Map<Integer, List<Category>> getCategoriesAttachedToMovies() throws SQLServerException;

    List<Category> getMovieCategories();

    void addCategoriesToMovie(int mID, List<Category> categories);

    List<Category> getUpdatedCategories(List<Category> categories);

    ArrayList<Category> getAllCategoriesArray() throws Exception;
}