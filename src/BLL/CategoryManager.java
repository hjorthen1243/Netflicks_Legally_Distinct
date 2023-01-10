package BLL;

import BE.Category;
import DAL.CategoryDAO;
import DAL.ICategoryDAO;

import java.util.List;

public class CategoryManager {
    ICategoryDAO categoryDAO;
    public CategoryManager(){categoryDAO = new CategoryDAO();
    }
    public List<Category> getAllCategories() throws Exception {
        return categoryDAO.getAllCategories();
    }

    public void deletedCategory(Category deletedCategory) throws Exception {
        categoryDAO.deleteCategory(deletedCategory);
    }

    /*public void updatedCategory(String oldCategoryName Category newCategoryName) {
        categoryDAO.editUpdateCategory(oldCategoryName, newCategoryName);

    }*/

    public Category createNewCategory(String genre) throws Exception {
        return categoryDAO.createNewCategory(genre);
    }

}
