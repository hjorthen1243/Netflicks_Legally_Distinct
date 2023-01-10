package DAL;

import BE.Category;

import java.util.List;

public interface ICategoryListDAO {
    List<Category> getAllCategoryLists() throws Exception;

    Category createNewCategoryList(String newCategoryName) throws Exception;

    Category editUpdateCategoryList(String oldCategoryName, Category newCategoryName) throws Exception;

    void deleteCategoryList(Category categoryName) throws Exception;
}
