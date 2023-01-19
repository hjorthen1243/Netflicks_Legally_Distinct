package DAL;

import BE.Category;
import com.microsoft.sqlserver.jdbc.SQLServerException;

import java.io.IOException;
import java.sql.*;
import java.util.*;

import static java.sql.Statement.RETURN_GENERATED_KEYS;

public class CategoryDAO implements ICategoryDAO {
    MyDatabaseConnector databaseConnector;
    MyOMDBConnector myOMDBConnector;

    //constructor
    public CategoryDAO() throws IOException {
        databaseConnector =new MyDatabaseConnector();
        myOMDBConnector = new MyOMDBConnector();
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

    //Gets all the different categories
    public ArrayList<Category> getAllCategoriesArray() throws Exception {
        ArrayList<Category> allCategoryList = new ArrayList<>();
        getCategories(allCategoryList);
        return allCategoryList; //Return the full set of categories
    }

    //Gets all the categories from the db
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
     * Create a new category
     * @param categoryName Sting
     * @return
     * @throws Exception
     */
    @Override
    public Category createNewCategory(String categoryName) throws Exception {
        //SQL String which adds the category to the DB
        String sql = "INSERT INTO Categories(Category) VALUES (?);";
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

    //update the name of a category
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

    // Remove a category from the db
    @Override
    public void removeCategory(Category category) throws Exception {
        //Get id of the selected playlist
        int id = category.getId();

        //SQL String which removes the category with the specific id from the DB
        String sql = "DELETE FROM Categories WHERE Id = " + id + ";";

        //SQL String which deletes the link between category & movie from the DB
        String sql2 = "DELETE FROM CatMovie WHERE CategoryID =" + id + ";";
        //Try with resources on the databaseConnector
        try (Connection conn = databaseConnector.getConnection()) {
            PreparedStatement ps = conn.prepareStatement(sql);
            PreparedStatement ps2 = conn.prepareStatement(sql2);
            //Execute the update
            ps2.executeUpdate();
            ps.executeUpdate();
        }
    }


    //TODO get Ane to understand how to join
    //Gets all the categories, that has a movie linked to it
    @Override
    public Map<Integer, List<Category>> getCategoriesAttachedToMovies() throws SQLServerException {
        Map<Integer, List<Category>> moviesWithCategories = new HashMap<Integer, List<Category>>();
        ArrayList<Category> categories = new ArrayList<>();
        //join on values
        String sql = """ 
                SELECT DISTINCT MovieID, Movie.Title, Categories.Category, Categories.id
                FROM CatMovie
                JOIN Categories ON CatMovie.categoryID = Categories.Id
                JOIN Movie ON CatMovie.MovieID = Movie.Id
                ORDER BY MovieID;""";

        try (Connection conn = databaseConnector.getConnection()) {
            Statement statement = conn.createStatement();
            ResultSet rs = statement.executeQuery(sql);
            int lastID = 0;
            boolean firstLine = true;
            String Category;
            int CategoryID;
            int movieID = 0;
            while (rs.next()) {
                movieID = rs.getInt("MovieID");
                if (firstLine) {
                    lastID = movieID;
                    firstLine = false;
                } if (lastID == movieID) {
                    Category = rs.getString("Category");
                    CategoryID = rs.getInt("id");
                    Category c = new Category(CategoryID, Category);
                    categories.add(c);
                } else  {
                    moviesWithCategories.putIfAbsent(lastID, categories);
                    lastID = movieID;
                    categories = new ArrayList<>();
                    Category = rs.getString("Category");
                    CategoryID = rs.getInt("id");
                    Category c = new Category(CategoryID, Category);
                    categories.add(c);
                }
            }
            moviesWithCategories.putIfAbsent(movieID, categories);
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
        return moviesWithCategories;
    }

    //TODO understand
    public List<Category> getMovieCategories(){
        String movieCategories = myOMDBConnector.getMovieCategories();
        String[] c = movieCategories.split(", ");
        ArrayList<Category> categories = new ArrayList<>();
        String sql =
        "SELECT * FROM Categories WHERE";
        for (int i = 0; i < c.length; i++) {
            sql = sql + " Category = '" + c[i] + "'" + " OR";
        }
        sql = sql.substring(0,sql.length()-3) + ";";
        try (Connection conn = databaseConnector.getConnection()) {
            Statement stmt = conn.createStatement();
            ResultSet rs = stmt.executeQuery(sql);
            while (rs.next()) {
                int id = rs.getInt("id");
                String catName = rs.getString("Category");
                //Create and add Categories to list
                Category category = new Category(id, catName);
                categories.add(category);
            }

    } catch (SQLServerException e) {
            throw new RuntimeException(e);
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
        return categories;
    }

    /**
     * Adds one or more categories to a movie.
     * @param mID the movie
     * @param categories List of categories
     */
    @Override
    public void addCategoriesToMovie(int mID, List<Category> categories) {
        //SQL String which adds the categories attached to the movie
        String sql = "INSERT INTO CatMovie(MovieID, CategoryID) VALUES ";
        String c = "";
        for (int i = 0; i < categories.size(); i++) {
            c = c + "(" + mID + "," + categories.get(i).getId() + "), ";
        }
        c = c.substring(0,c.length()-2);
        c = c + ";";
        //Try with resources on the databaseConnector
        try (Connection conn = databaseConnector.getConnection()) {

            //Statement is a prepared SQL statement
            PreparedStatement ps = conn.prepareStatement(sql + c);
            //Execute Update
            ps.executeUpdate();

        } catch (SQLException ex) {
            ex.printStackTrace();
        }
    }

    /**
     * Gets all the categories equal to the ones that is put into it
     * @param categories
     * @return
     */
    @Override
    public List<Category> getUpdatedCategories(List<Category> categories) {
        //The list that is returned, when the list is
        List<Category>  categories1 = new ArrayList<>();
        //Takes the categories, where name is equal to X OR X OR X OR X.....X OR;
        String[] c = categories.toString().split(", ");
        String sql = "SELECT * FROM Categories WHERE";
        for (int i = 0; i < c.length; i++) {
            sql = sql + " Category = '" + c[i] + "'" + " OR";
        }
        //Removes the last .... X" OR" and puts in an ";"
        sql = sql.substring(0,sql.length()-3) + ";";
        sql = sql.replaceAll("\\[", "");
        sql = sql.replaceAll("]", "");
        try (Connection conn = databaseConnector.getConnection()) {
            //Statement is a prepared SQL statement
            Statement stmt = conn.createStatement();
            ResultSet rs = stmt.executeQuery(sql);

            //Puts the resultSet into the return set
            while (rs.next()) {
                int id = rs.getInt("id");
                String catName = rs.getString("Category");
                Category category = new Category(id, catName);;
                categories1.add(category);
            }

        } catch (SQLException ex) {
            ex.printStackTrace();
        }
        return categories1;
    }
}

