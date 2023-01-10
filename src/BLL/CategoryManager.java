package BLL;

import BE.Category;
import DAL.CategoryDAO;
import DAL.ICategoryDAO;

import java.util.List;

public class CategoryManager {
    ICategoryDAO categoryListDAO;
    public CategoryManager(){categoryListDAO = new CategoryDAO();
    }
    public List<Category> getAllCategories() throws Exception {
        return categoryListDAO.getAllCategories();
    }

    public void deletedCategory(Category deletedCategory) throws Exception {
        categoryListDAO.deleteCategory(deletedCategory);
    }

    public void updatedCategory(Category updatedCategory) {
        categoryListDAO.editUpdateCategory(updatedCategory);

    }

    public Category createNewCategory(String genre) throws Exception {
        return categoryListDAO.createNewCategory(genre);
    }

}
