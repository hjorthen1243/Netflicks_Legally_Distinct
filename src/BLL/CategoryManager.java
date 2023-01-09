package BLL;

import BE.Category;
import DAL.CategoryListDAO;
import DAL.ICategoryDAO;

public class CategoryManager {
    ICategoryDAO categoryDAO;

    public void categoryManager(){categoryDAO = new CategoryListDAO;}

    public Category createCategory(){

        return null;
    }

    public void deleteCategory(){

    }

    public void updateCategory() {
    }
}
