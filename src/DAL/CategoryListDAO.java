package DAL;

import BE.Category;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class CategoryListDAO implements ICategoryListDAO {
    MyDatabaseConnector myDatabaseConnector;

    @Override
    public List<Category> getAllCategoryLists() throws Exception {
        List<Category> allCategoryLists = new ArrayList<>();

        //(allPlaylists); //Loop through all the playlists with songs
        //(allPlaylists); //Loop through all the playlists without songs

        return allCategoryLists; //Return the full set of playlists
    }

    @Override
    public Category createNewCategoryList(String newCategoryName) throws Exception {
        return null;
    }

    @Override
    public Category editUpdateCategoryList(String oldCategoryName, Category newCategory) throws Exception {

        int id = newCategory.getId(); //Get the id of the playlist we have chosen to edit

        //SQL String where we update the title of the playlist with the id we got from earlier
        String sql = "UPDATE Categories SET categoryName = (?) WHERE id =" + id + ";";

        //Try with resources on the databaseConnector
        try (Connection conn = myDatabaseConnector.getConnection()) {

            //executing the prepared SQL statement
            PreparedStatement ps = conn.prepareStatement(sql);
            ps.setString(1, oldCategoryName);
            ps.executeUpdate();

        } catch (SQLException ex) {
            ex.printStackTrace();
            throw new Exception("Could not update the category" + ex);

        }
        //Return the updated playlist to be fed into the observable list
        return new Category(id, newCategory.getName());
    }

    @Override
    public void deleteCategoryList(Category category) throws Exception {
        //Get id of the selected playlist
        int id = category.getId();

        //SQL String which deletes the playlist with the specific id from the DB
        String sql = "DELETE FROM Categories WHERE Id = " + id + ";";

        //SQL String which deletes the link between playlists & songs from the DB
        String sql2 = "DELETE FROM CatMovie WHERE id =" + id + ";";
        //Try with resources on the databaseConnector
        try (Connection conn = myDatabaseConnector.getConnection()) {
            PreparedStatement ps = conn.prepareStatement(sql);
            PreparedStatement ps2 = conn.prepareStatement(sql2);
            //Execute the update
            ps2.executeUpdate();
            ps.executeUpdate();
        }
    }
}
