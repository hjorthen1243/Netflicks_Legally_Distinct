package GUI.Model;

import BE.Category;
import BLL.CategoryManager;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;

public class CategoryModel {

    private CategoryManager categoryManager;
    private ObservableList<Category> categoriesToBeViewed;
    private Category selectedCategory;

    public Category getSelectedCategory(){return selectedCategory;}

    public void setSelectedCategory(Category selectedCategory){this.selectedCategory = selectedCategory;}

    public CategoryModel() throws Exception {
        categoryManager = new CategoryManager();
        categoriesToBeViewed = FXCollections.observableArrayList();
        categoriesToBeViewed.addAll(categoryManager.getAllCategories());
    }

    public ObservableList<Category> getObservableCategories(){return categoriesToBeViewed;}

    public void deleteCategory(Category deletedCategory) throws Exception {
        categoryManager.deletedCategory(deletedCategory);
        categoriesToBeViewed.remove(deletedCategory);
    }

    public void updatedCategory(Category updatedCategory) throws Exception {
        categoryManager.updatedCategory(updatedCategory);

        categoriesToBeViewed.clear();
        categoriesToBeViewed.addAll(categoryManager.getAllCategories());
    }

    public void createNewCategory(String genre) throws Exception {
        Category category = categoryManager.createNewCategory(genre);
        categoriesToBeViewed.add(category);
    }
}
