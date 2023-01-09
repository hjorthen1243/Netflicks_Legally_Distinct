package BLL;

import BE.Category;
import DAL.CategoryListDAO;
import DAL.ICategoryListDAO;

import java.util.List;

public class CategoryManager {
    ICategoryListDAO categoryListDAO;
    public CategoryManager(){categoryListDAO = new CategoryListDAO();
    }
    public List<Category> getAllCategories() throws Exception {
        return categoryListDAO.getAllCategoryLists();
    }

    public void deletedCategory(Category deletedCategory) throws Exception {
        categoryListDAO.deleteCategoryList(deletedCategory);
    }

    public void updatedCategory(Category updatedCategory) {
        categoryListDAO.editUpdateCategoryList(updatedCategory);

    }

    public Category createNewCategory(String genre) throws Exception {
        return categoryListDAO.createNewCategoryList(genre);
    }

}
