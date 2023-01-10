package GUI.Model;

import BE.Category;
import BLL.CategoryManager;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;

import java.util.ArrayList;

public class CategoryModel {

    private CategoryManager categoryManager;
    private ArrayList<Category> allCategories;
    private ObservableList<Category> categoriesToBeViewed;
    private Category selectedCategory;

    public Category getSelectedCategory(){return selectedCategory;}

    public void setSelectedCategory(Category selectedCategory){this.selectedCategory = selectedCategory;}

    public CategoryModel() throws Exception {
        categoryManager = new CategoryManager();
        categoriesToBeViewed = FXCollections.observableArrayList();
        //categoriesToBeViewed.addAll(categoryManager.getAllCategories());
    }

    public ObservableList<Category> getObservableCategories(){
        return categoriesToBeViewed;
    }
    public ArrayList<Category> getAllCategories(){
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
}
