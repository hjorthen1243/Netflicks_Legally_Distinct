package BLL;

import BE.Category;
import DAL.CategoryDAO;

import DAL.ICategoryDAO;
import com.microsoft.sqlserver.jdbc.SQLServerException;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 * This class manages the categories and works as a link between DAL and GUI
 */
public class CategoryManager {
    ICategoryDAO categoryDAO;
    public CategoryManager() throws IOException {
        categoryDAO = new CategoryDAO();
    }
    public List<Category> getAllCategories() throws Exception {
        return categoryDAO.getAllCategories();
    }

    public ArrayList<Category> getAllCategoriesArray() throws Exception {
        return categoryDAO.getAllCategoriesArray();
    }

    public void removeCategory(Category removeCategory) throws Exception {
        categoryDAO.removeCategory(removeCategory);
    }

    public void editUpdateCategory(String oldCategoryName, Category newCategoryName) throws Exception {
        categoryDAO.editUpdateCategory(oldCategoryName, newCategoryName);
    }

    public Category createNewCategory(String genre) throws Exception {
        return categoryDAO.createNewCategory(genre);
    }

    //Gets categories linked to movies
    public Map<Integer, List<Category>> getCategoriesAttachedToMovies() throws SQLServerException {
        return categoryDAO.getCategoriesAttachedToMovies();
    }

    public List<Category> getMovieCategories() {
        return categoryDAO.getMovieCategories();
    }

    public void addCategoriesToMovie(int mID, List<Category> categories) {
        categoryDAO.addCategoriesToMovie(mID, categories);
    }

    public List<Category> getUpdatedCategories(List<Category> categories) {
        return categoryDAO.getUpdatedCategories(categories);
    }
}