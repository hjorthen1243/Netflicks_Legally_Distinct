package DAL;

import BE.Category;
import com.microsoft.sqlserver.jdbc.SQLServerException;

import java.util.List;
import java.util.Map;

public interface ICategoryDAO {
    List<Category> getAllCategories() throws Exception;

    Category createNewCategory(String newCategoryName) throws Exception;

    Category editUpdateCategory(String oldCategoryName, Category newCategoryName) throws Exception;

    void deleteCategory(Category categoryName) throws Exception;

    Map<Integer, List<Category>> getCategoriesAttachedToMovies() throws SQLServerException;

    List<Category> getMovieCategories();

    void addCategoriesToMovie(List<Category> categories);
}
