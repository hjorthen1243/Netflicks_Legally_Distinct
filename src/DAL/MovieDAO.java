package DAL;

import BE.Category;
import BE.Movie;

import java.sql.*;
import java.time.Duration;
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
                String title = rs.getString("Title");
                int year = rs.getInt("Year");
                Duration time = Duration.ofSeconds(rs.getInt("Length"));
                String length = time.toMinutesPart() + ":" + time.toSecondsPart();
                double imdbRating = rs.getDouble("IMDBRating");
                int pRating = rs.getInt("PersonalRating");
                Date lastView = rs.getDate("LastView");

                String pathToFile = rs.getString("PathToFile");


                //Add Movie to list allMovies
                Movie movie = new Movie(id, title, year, length, imdbRating, pRating , lastView, pathToFile);
                allMovies.add(movie);

            }
            return allMovies;

        } catch (SQLException ex) {
            ex.printStackTrace();
            throw new Exception("Could not get movies from database");
        }
    }

    @Override
    public Movie addMovie(String title, int year, String length, double imdbRating, int personalRating, Date lastViewed, String pathToFile) throws Exception {

        //SQL Statement and initializing id variable.
        String sql = "INSERT INTO Movie (Title, Year, Length, IMDBRating, PersonalRating, LastView, PathToFile) VALUES (?,?,?,?,?,?,?);";
        int id = 0;
        //Try with resources on the databaseConnector
        try (Connection conn = myDatabaseConnector.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql, RETURN_GENERATED_KEYS)) {

            //Bind parameters to the SQL statement.
            stmt.setString(1, title);
            stmt.setInt(2, year);
            stmt.setString(3, length);
            stmt.setDouble(4, imdbRating);
            stmt.setInt(5, personalRating);
            stmt.setDate(6, lastViewed);
            stmt.setString(7, pathToFile);

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

        //Generating and returning the new movie to be fed into the observable list
        return new Movie(id, title, year, length, imdbRating, personalRating, lastViewed, pathToFile);
    }

    @Override
    public void editUpdateMovie(Movie movie) throws Exception {

        //Try with resources on the databaseConnector
        try (Connection conn = myDatabaseConnector.getConnection()){

            //SQL Statement and initializing id variable.
            String sql = "UPDATE Movie SET Title=?, Year=?, IMDBRating=?, personalRating=?, LastView=?, PathToFile=?" +
                    "WHERE Id = ?;";

            PreparedStatement stmt = conn.prepareStatement(sql);

            //Bind parameters to the SQL statement.
            stmt.setString(1, movie.getTitle());
            stmt.setInt(2, movie.getYear());
            stmt.setDouble(3, movie.getImdbRating());
            stmt.setInt(4, movie.getPersonalRating());
            stmt.setDate(5, (Date) movie.getLastViewDate());
            stmt.setString(6, movie.getPathToFile());
            stmt.setInt(7, movie.getId());
            System.out.println(movie);

            //Execute the update into the DB
            stmt.executeUpdate();
        }
        catch (SQLException ex){
            ex.printStackTrace();
            throw new Exception("Could not update the movie", ex);
        }
    }

    @Override
    public void deleteMovie(Movie m) throws Exception {
        //Get the id of the chosen movie
        int mID = m.getId();
        //SQL String which deletes the movie from all moview in the DB
        String sql = "DELETE FROM Movie WHERE Id = " + mID + ";";

        //SQL String which deletes the link between the movie and the category
        //String sql2 = "DELETE FROM CatMovie WHERE MovieID = " + mID + ";";

        //Try with resources on the databaseConnector
        try (Connection conn = myDatabaseConnector.getConnection()) {

            //Statements are prepared SQL statements
            PreparedStatement ps = conn.prepareStatement(sql);
            //PreparedStatement ps2 = conn.prepareStatement(sql2);

            //Execute the update which removes the link between song and playlist first, then remove the song from the DB
            //ps2.executeUpdate();
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
                String name = rs.getString("Category");

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

        String sql = "INSERT INTO CatMovie (Category) VALUES (?);";
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
    public List<Movie> getMoviesWithCategory(Category category) throws Exception {
        //Make a list to return
        ArrayList<Movie> allMoviesWithCategory = new ArrayList<>();

        //Try with resources on the databaseConnector
        try (Connection conn = myDatabaseConnector.getConnection()) {

            //SQL String which gets all categories form the DB
            String sql = "SELECT DISTINCT * FROM CatMovie WHERE CategoryID =" + category.getId() + ";";

            Statement stmt = conn.createStatement();
            ResultSet rs = stmt.executeQuery(sql);

            //ArrayList<Integer> movieIDs = new ArrayList<>();
            //Loop through rows from the database result set
            while (rs.next()) {
                //Map DB row to Category Object
                int id = rs.getInt("MovieID");

                List<Movie> allMovies = getAllMovies();
                for(Movie movie: allMovies){
                    if(movie.getId() == id){
                        allMoviesWithCategory.add(movie);
                    }
                }
            }
            return allMoviesWithCategory;
        } catch (SQLException ex) {
            ex.printStackTrace();
            throw new Exception("Could not get it from database");
        }
    }

    @Override
    public void addCategoryToMovie(int movieId, int categoryId) {

    }

    @Override
    public void removeCategoryFromMovie(int movieId, int categoryId) {

    }

    @Override
    public List<Movie> searchAddMovie(String text) {
        return myOMDBConnector.searchQuery(text);
    }

    @Override
    public Movie searchSelectedMovie(String imdbID) {
        return myOMDBConnector.chosenMovieMoreInfo(imdbID);
    }

    public String getMovieCategories(){
        return myOMDBConnector.getMovieCategories();
    }
}
