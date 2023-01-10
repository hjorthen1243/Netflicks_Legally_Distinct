package DAL;

import BE.Category;
import BE.Movie;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

import static java.sql.Statement.RETURN_GENERATED_KEYS;

public class MovieDAO implements IMovieDAO {
    MyDatabaseConnector myDatabaseConnector;
    MyOMDBConnector myOMDBConnector;

    public MovieDAO (){
        myDatabaseConnector = new MyDatabaseConnector();
        myOMDBConnector = new MyOMDBConnector();
    }

    @Override
    public List<Movie> getAllMovies() throws Exception {

        //Makes a list of movies
        ArrayList<Movie> allMovies = new ArrayList<>();

        //Try with resources on the databaseConnector
        try (Connection conn = myDatabaseConnector.getConnection()) {

            //SQL String which selects all songs from the DB
            String sql = "SELECT * FROM Movie;";

            Statement stmt = conn.createStatement();
            ResultSet rs = stmt.executeQuery(sql);

            //Loop through rows from the database result set
            while (rs.next()) {
                //Map DB row to Movie Object
                int id = rs.getInt("Id");
                String name = rs.getString("Name");
                double imdbRating= rs.getDouble("IMDBrating");
                String pathToFile = rs.getString("PathToFile");
                String lastView = rs.getString("LastView");

                //Add Movie to list allmovies
                Movie movie = new Movie(id, imdbRating, name ,pathToFile, lastView);
                allMovies.add(movie);
            }
            return allMovies;

        } catch (SQLException ex) {
            ex.printStackTrace();
            throw new Exception("Could not get movies from database");
        }
    }

    @Override
    public Movie addMovie(String name, double imdbRating, String pathToFile, int lastViewed) throws Exception {

        //SQL Statement and initializing id variable.
        String sql = "INSERT INTO Movie (Name, IMDBrating, PathToFile, LastView) VALUES (?,?,?,?);";
        int id = 0;
        //Try with resources on the databaseConnector
        try (Connection conn = myDatabaseConnector.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql, RETURN_GENERATED_KEYS)) {

            //Bind parameters to the SQL statement.
            stmt.setString(1, name);
            stmt.setString(2, String.valueOf(imdbRating));
            stmt.setString(3, pathToFile);
            stmt.setInt(4, Integer.parseInt(String.valueOf(lastViewed)));

            //Execute the update into the DB
            stmt.executeUpdate();

            //Get the new ID from DB.
            ResultSet rs = stmt.getGeneratedKeys();
            if (rs.next()) {
                id = rs.getInt(1);
            }

        } catch (SQLException ex) {
            ex.printStackTrace();
            throw new Exception("Could not add movie" + ex);
        }

        String lastViewedString = lastViewed + "";
        //Generating and returning the new movie to be fed into the observable list
        //return new Movie(id, imdbRating, name, pathToFile, lastViewedString);
        return null;
    }

    @Override
    public void editUpdateMovie(Movie movie) throws Exception {

        //Try with resources on the databaseConnector
        try (Connection conn = myDatabaseConnector.getConnection()){

            //SQL Statement and initializing id variable.
            String sql = "UPDATE Movie SET Name=?, IMDBRating=?, PathToFile=?, LastView=?" +
                    "WHERE Id = ?;";

            PreparedStatement stmt = conn.prepareStatement(sql);

            //Bind parameters to the SQL statement.
            stmt.setString(1, movie.getTitle());
            stmt.setString(2, String.valueOf(movie.getImdbRating()));
            stmt.setString(3, movie.getPathToFile());
            stmt.setInt(4, Integer.parseInt(String.valueOf(movie.getLastViewDate())));
            stmt.setInt(5, movie.getId());

            //Execute the update into the DB
            stmt.executeUpdate();
        }
        catch (SQLException ex){
            ex.printStackTrace();
            throw new Exception("Could not update the movie", ex);
        }
    }

    @Override
    public void deleteMovie(Movie movie) throws Exception {
        //Get the id of the chosen movie
        int id = movie.getId();

        //SQL String which deletes the movie from all moview in the DB
        String sql = "DELETE FROM Movie WHERE Id = " + id + ";";

        //SQL String which deletes the link between the movie and the category
        String sql2 = "DELETE FROM CatMovie WHERE id = " + id + ";";

        //Try with resources on the databaseConnector
        try (Connection conn = myDatabaseConnector.getConnection()) {

            //Statements are prepared SQL statements
            PreparedStatement ps = conn.prepareStatement(sql);
            PreparedStatement ps2 = conn.prepareStatement(sql2);

            //Execute the update which removes the link between song and playlist first, then remove the song from the DB
            ps2.executeUpdate();
            ps.executeUpdate();
        }
    }

    @Override
    public List<Category> getAllCategories() throws Exception {
        //Make a list to return
        ArrayList<Category> allCategories = new ArrayList<>();

        //Try with resources on the databaseConnector
        try (Connection conn = myDatabaseConnector.getConnection()) {

            //SQL String which gets all categories form the DB
            String sql = "SELECT * FROM Category;";

            Statement stmt = conn.createStatement();
            ResultSet rs = stmt.executeQuery(sql);

            //Loop through rows from the database result set
            while (rs.next()) {
                //Map DB row to Category Object
                int id = rs.getInt("id");
                String name = rs.getString("categoryName");

                Category category = new Category(id, name);
                allCategories.add(category);
            }
            return allCategories; //Return categories to be fed to the observable list

        } catch (SQLException ex) {
            ex.printStackTrace();
            throw new Exception("Could not get categories from database");
        }
    }

    @Override
    public Category createCategory(String name) throws Exception {

        String sql = "INSERT INTO CatMovie (categoryName) VALUES (?);";
        int id = 0;

        //Try with resources on the databaseConnector
        try (Connection conn = myDatabaseConnector.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql, RETURN_GENERATED_KEYS)) {

            //Bind parameters to the SQL statement.
            stmt.setString(1, name);
            //Execute the update into the DB
            stmt.executeUpdate();

            //Get the new ID from DB.
            ResultSet rs = stmt.getGeneratedKeys();
            if (rs.next()) {
                id = rs.getInt(1);
            }
        } catch (SQLException ex) {
            ex.printStackTrace();
        }
        return new Category(id, name);
    }

    @Override
    public void deleteCategory(Category category) throws Exception {
        int id = category.getId(); //Get the id of the category the user has chosen
        //SQL String which deletes the category with the chosen id from the DB
        String sql = "DELETE FROM Category WHERE Id = " + id + ";";
        //Try with resources on the databaseConnector
        try (Connection conn = myDatabaseConnector.getConnection()) {
            //Statement is a prepared SQL statements
            PreparedStatement ps = conn.prepareStatement(sql);
            ps.executeUpdate(); //Execute the update in the DB
        }
    }

    @Override
    public List<Movie> getMoviesWithCategoryID(int id) throws Exception {
        return null;
    }

    @Override
    public void addCategoryToMovie(int movieId, int categoryId) {

    }

    @Override
    public void removeCategoryFromMovie(int movieId, int categoryId) {

    }
}
