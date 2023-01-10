package BLL;

import BE.Category;
import DAL.CategoryDAO;
import GUI.Model.CategoryModel;

import java.util.ArrayList;

public class CategoryManager {
    CategoryDAO categoryDAO;

    public Category getAllCategories() {
        categoryDAO = new CategoryDAO();
        try {
            //ArrayList<Category> categories = categoryDAO.getAllCategories();

        } catch (Exception e) {
            throw new RuntimeException(e);
        }
        return null;
    }

    public void deletedCategory(Category deletedCategory) {

    }

    public void updatedCategory(Category updatedCategory) {

    }

    public Category getCategory() {
        return null;
    }

    public Category createNewCategory(String genre) {
        return null;
    }
}
