package GUI.Model;

import BE.Category;
import BLL.CategoryManager;
import com.microsoft.sqlserver.jdbc.SQLServerException;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class CategoryModel {
    private List<Category> categoriesToBeViewed;
    private CategoryManager categoryManager;
    private ArrayList<Category> allCategories;
    private Category selectedCategory;

    private ObservableList<Category> categoriesAttachedToMovies;

    //TODO what does this gat and set do?
    public Category getSelectedCategory(List<Category> categories) {
        return selectedCategory;
    }
    public void setSelectedCategory(Category selectedCategory) {
        this.selectedCategory = selectedCategory;
    }

    /**
     * Constructor for the categoryModel. Categories to be viewed is set to be all the categories
     * @throws Exception
     */
    public CategoryModel() throws Exception {
        categoryManager = new CategoryManager();
        categoriesToBeViewed = FXCollections.observableArrayList();
        categoriesToBeViewed.addAll(categoryManager.getAllCategories());
    }

    /**
     * Gets all the categories, that has a link to a movie.
     * @return the integer is the movieID and the list of Category is the linked categories
     * @throws SQLServerException
     */
    public Map<Integer, List<Category>> getObservableCategories() throws SQLServerException {
        return categoryManager.getCategoriesAttachedToMovies();
    }

    /**
     * Returns all the categories from the db
     * @return ArrayList<Category>
     */
    public ArrayList<Category> getAllCategories() {
        try {
            return categoryManager.getAllCategoriesArray();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    /**
     * Removes the specific category
     * @param deletedCategory category to delete
     * @throws Exception
     */
    public void removeCategory(Category deletedCategory) throws Exception {
        categoryManager.removeCategory(deletedCategory);
        categoriesToBeViewed.remove(deletedCategory);
    }

    /**
     * Updates a specific category
     * @param oldCategoryName String
     * @param newCategoryName Category
     * @throws Exception
     */
    public void updatedCategory(String oldCategoryName, Category newCategoryName) throws Exception {
        categoryManager.editUpdateCategory(oldCategoryName, newCategoryName);
        categoriesToBeViewed.clear();
        categoriesToBeViewed.addAll(categoryManager.getAllCategories());
    }

    /**
     * Creates a new category from a string and adds the category to categories to be viewed
     * @param newcategory String on new name
     * @throws Exception
     */
    public void createNewCategory(String newcategory) throws Exception {
        Category category = categoryManager.createNewCategory(newcategory);
        categoriesToBeViewed.add(category);
    }

    /**
     *
     * @return
     */
    public ObservableList<Category> getMovieCategories() {
        ObservableList<Category> categories = FXCollections.observableArrayList();
        categories.addAll(categoryManager.getMovieCategories());
        return categories;
    }

    public void addCategoriesToMovie(int mID,List<Category> categories) {
        categoryManager.addCategoriesToMovie(mID,categories);
    }

    public List<Category> getUpdatedCategories(List<Category> categories) {
        return categoryManager.getUpdatedCategories(categories);
    }
}
