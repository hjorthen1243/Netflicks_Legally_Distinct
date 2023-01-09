package DAL;

import BE.Category;

import java.util.List;

public interface ICategoryDAO {
    List<Category> getAllCategorys() throws Exception;

    Category createNewCategory(String newCategoryName) throws Exception;

    Category editUpdateCategory(String oldCategoryName, Category newCategoryName) throws Exception;

    void deleteCategory(Category categoryName) throws Exception;
}
