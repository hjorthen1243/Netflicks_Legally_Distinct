package DAL;

import BE.Category;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

import static java.sql.Statement.RETURN_GENERATED_KEYS;

public class CategoryDAO implements ICategoryDAO {
    MyDatabaseConnector databaseConnector;

    public CategoryDAO(){
        databaseConnector =new MyDatabaseConnector();
    }

    /**
     * Return all the categories there is, even if there are no movies added to it
     * @return List<Category> allCategoryList
     * @throws Exception
     */
    @Override
    public List<Category> getAllCategories() throws Exception {
        List<Category> allCategoryList = new ArrayList<>();
        getCategories(allCategoryList);
        return allCategoryList; //Return the full set of categories
    }

    public ArrayList<Category> getAllCategoriesArray() throws Exception {
        ArrayList<Category> allCategoryList = new ArrayList<>();
        getCategories(allCategoryList);
        return allCategoryList; //Return the full set of categories
    }

    public void getCategories(List<Category> categoryLists) throws Exception {
        try (Connection conn = databaseConnector.getConnection()) {
            //SQL String which gets all Categories
            String sql = "SELECT * FROM Categories;";

            //Execute the SQL statement
            Statement stmt = conn.createStatement();
            ResultSet rs = stmt.executeQuery(sql);
            //Loop through rows from the database result set
            while (rs.next()) {
                //Map DB row to Song Object
                int id = rs.getInt("id");
                String catName = rs.getString("Category");

                //Create and add Categories to list
                Category category = new Category(id, catName);
                categoryLists.add(category);
            }
        } catch (Exception exception) {
            exception.printStackTrace();
            throw new Exception("Was not able to get all categories");
        }
    }


    /**
     *
     * @param categoryName
     * @return
     * @throws Exception
     */
    @Override
    public Category createNewCategory(String categoryName) throws Exception {
        //SQL String which adds the category to the DB
        String sql = "INSERT INTO Categories(categoryName) VALUES (?);";
        int id = 0; //ID is autogenerated by DB, so it is initially set to 0
        int numMovies = 0; //Number of movies is set to 0, as we create an empty category

        //Try with resources on the databaseConnector
        try (Connection conn = databaseConnector.getConnection()) {

            //Statement is a prepared SQL statement
            PreparedStatement ps = conn.prepareStatement(sql, RETURN_GENERATED_KEYS);

            //Bind parameters to the SQL statement
            ps.setString(1, categoryName);

            //Execute Update
            ps.executeUpdate();

            ResultSet rs = ps.getGeneratedKeys();
            //Gets the next key on the column index 1(id for playlists) sets id to the new value
            if (rs.next()) {
                id = rs.getInt(1);
            }
        } catch (SQLException ex) {
            ex.printStackTrace();
            throw new Exception("Could not create category" + ex);

        }
        //Return the category, so it can be fed into the observable list
        return new Category(id, categoryName);
    }
    @Override
    public Category editUpdateCategory(String oldCategoryName, Category newCategory) throws Exception {

        int id = newCategory.getId(); //Get the id of the playlist we have chosen to edit

        //SQL String where we update the title of the playlist with the id we got from earlier
        String sql = "UPDATE Categories SET categoryName = (?) WHERE id =" + id + ";";

        //Try with resources on the databaseConnector
        try (Connection conn = databaseConnector.getConnection()) {

            //executing the prepared SQL statement
            PreparedStatement ps = conn.prepareStatement(sql);
            ps.setString(1, oldCategoryName);
            ps.executeUpdate();

        } catch (SQLException ex) {
            ex.printStackTrace();
            throw new Exception("Could not update the category" + ex);

        }
        //Return the updated playlist to be fed into the observable list
        return new Category(id, newCategory.getCategory());
    }

    @Override
    public void deleteCategory(Category category) throws Exception {
        //Get id of the selected playlist
        int id = category.getId();

        //SQL String which deletes the playlist with the specific id from the DB
        String sql = "DELETE FROM Categories WHERE Id = " + id + ";";

        //SQL String which deletes the link between playlists & songs from the DB
        String sql2 = "DELETE FROM CatMovie WHERE id =" + id + ";";
        //Try with resources on the databaseConnector
        try (Connection conn = databaseConnector.getConnection()) {
            PreparedStatement ps = conn.prepareStatement(sql);
            PreparedStatement ps2 = conn.prepareStatement(sql2);
            //Execute the update
            ps2.executeUpdate();
            ps.executeUpdate();
        }
    }
}
