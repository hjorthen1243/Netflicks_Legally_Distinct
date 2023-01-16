package BLL;

import BE.Category;
import BE.Movie;
import DAL.CategoryDAO;

import DAL.ICategoryDAO;
import com.microsoft.sqlserver.jdbc.SQLServerException;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class CategoryManager {
    ICategoryDAO categoryDAO;
    CategoryDAO categoryDAONew;
    public CategoryManager() throws IOException {categoryDAO = new CategoryDAO();
    }
    public List<Category> getAllCategories() throws Exception {
        return categoryDAO.getAllCategories();
    }

    public ArrayList<Category> getAllCategoriesArray() throws Exception {
        categoryDAONew = new CategoryDAO();
        return categoryDAONew.getAllCategoriesArray();
    }

    public void deletedCategory(Category deletedCategory) throws Exception {
        categoryDAO.deleteCategory(deletedCategory);
    }

    public void editUpdateCategory(String oldCategoryName, Category newCategoryName) throws Exception {
        categoryDAO.editUpdateCategory(oldCategoryName, newCategoryName);
    }

    public Category createNewCategory(String genre) throws Exception {
        return categoryDAO.createNewCategory(genre);
    }

    public Map<Integer, List<Category>> getCategoriesAttachedToMovies() throws SQLServerException {
        return categoryDAO.getCategoriesAttachedToMovies();
    }
    public String getMovieCategories() {
        return categoryDAO.getMovieCategories();
    }
}
