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

    public Category getSelectedCategory(List<Category> categories) {
        return selectedCategory;
    }

    public void setSelectedCategory(Category selectedCategory) {
        this.selectedCategory = selectedCategory;
    }

    public CategoryModel() throws Exception {
        categoryManager = new CategoryManager();
        categoriesToBeViewed = FXCollections.observableArrayList();
        //categoriesToBeViewed.addAll(categoryManager.getAllCategories());
    }

    public Map<Integer, List<Category>> getObservableCategories() throws SQLServerException {
        return categoryManager.getCategoriesAttachedToMovies();
    }

    public ArrayList<Category> getAllCategories() {
        allCategories = new ArrayList<>();
        try {
            allCategories = categoryManager.getAllCategoriesArray();
            return allCategories;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    public void deleteCategory(Category deletedCategory) throws Exception {
        categoryManager.deletedCategory(deletedCategory);
        categoriesToBeViewed.remove(deletedCategory);
    }

    public void updatedCategory(String oldCategoryName, Category newCategoryName) throws Exception {
        categoryManager.editUpdateCategory(oldCategoryName, newCategoryName);

        categoriesToBeViewed.clear();
        categoriesToBeViewed.addAll(categoryManager.getAllCategories());
    }

    public void createNewCategory(String genre) throws Exception {
        Category category = categoryManager.createNewCategory(genre);
        categoriesToBeViewed.add(category);
    }

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
