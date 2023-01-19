package DAL;

import BE.Category;
import BE.Movie;

import java.io.IOException;
import java.sql.*;
import java.time.Duration;
import java.util.ArrayList;
import java.util.List;

import static java.sql.Statement.RETURN_GENERATED_KEYS;


public class MovieDAO implements IMovieDAO {

    //getting the database connectors
    MyDatabaseConnector myDatabaseConnector;
    MyOMDBConnector myOMDBConnector;

    public MovieDAO() throws IOException {
        myDatabaseConnector = new MyDatabaseConnector();
        myOMDBConnector = new MyOMDBConnector();
    }

    /**
     * Gets all the movies from the database
     * @return List of movies
     * @throws Exception SQLException
     */

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
                String length = String.format("%02d:%02d:%02d", time.toHours(), time.toMinutesPart(), time.toSecondsPart());
                double imdbRating = rs.getDouble("IMDBRating");
                int pRating = rs.getInt("PersonalRating");
                Date lastView = rs.getDate("LastView");

                String pathToFile = rs.getString("PathToFile");

                //Add Movie to list allMovies
                Movie movie = new Movie(id, title, year, length, imdbRating, pRating, lastView, pathToFile);
                allMovies.add(movie);

            }
            return allMovies;

        } catch (SQLException ex) {
            ex.printStackTrace();
            throw new Exception("Could not get movies from database");
        }
    }

    /**
     * Add a new movie to the database
     * @param title         String
     * @param year          int
     * @param length        String
     * @param imdbRating    double
     * @param personalRating int
     * @param lastViewed    Date
     * @param pathToFile    String
     * @return              Movie
     * @throws Exception    SQL
     */
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

    /**
     * Change values in the specific movie.
     * @param movie the updated movie, with the same id as the last movie
     */
    @Override
    public void editUpdateMovie(Movie movie) {

        //Try with resources on the databaseConnector
        try (Connection conn = myDatabaseConnector.getConnection()) {

            //SQL Statement and initializing id variable.
            String sql = "UPDATE Movie SET Title=?, Year=?, IMDBRating=?, personalRating=?, LastView=?, PathToFile=? WHERE Id = ?;";

            PreparedStatement stmt = conn.prepareStatement(sql);

            //Bind parameters to the SQL statement.
            stmt.setString(1, movie.getTitle());
            stmt.setInt(2, movie.getYear());
            stmt.setDouble(3, movie.getImdbRating());
            stmt.setInt(4, movie.getPersonalRating());
            stmt.setDate(5, (Date) movie.getLastViewDate());
            stmt.setString(6, movie.getPathToFile());
            stmt.setInt(7, movie.getId());

            //Execute the update into the DB
            stmt.executeUpdate();
        } catch (SQLException ex) {
            ex.printStackTrace();
        }
    }

    /**
     * Deletes the movie, and the connection between categories and movies
     * @param m movie
     * @throws Exception ex
     */
    @Override
    public void deleteMovie(Movie m) throws Exception {
        //Get the id of the chosen movie
        int mID = m.getId();
        //SQL String which deletes the movie from all movie in the DB
        String sql = "DELETE FROM Movie WHERE Id = " + mID + ";";

        //SQL String which deletes the link between the movie and the category
        String sql2 = "DELETE FROM CatMovie WHERE MovieID = " + mID + ";";

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

    /**
     * Gets all the movie, with a specific category
     * @param category the specific category
     * @return list of movies,with the specific categories
     * @throws Exception SQL
     */
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
                for (Movie movie : allMovies) {
                    if (movie.getId() == id) {
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

    /**
     * Removes the link between a specific movie and a category.
     * @param movieId id on movie
     * @param categoryId id on category
     */
    @Override
    public void removeCategoryFromMovie(int movieId, int categoryId) {
        //SQL String which deletes the link between category & movie from the DB
        String sql = "DELETE FROM CatMovie WHERE CategoryID =" + categoryId + " AND MovieID = " + movieId + ";";
        //Try with resources on the databaseConnector
        try (Connection conn = myDatabaseConnector.getConnection()) {
            PreparedStatement ps = conn.prepareStatement(sql);
            //Execute the update
            ps.executeUpdate();
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    //TODO do not understand
    @Override
    public List<Movie> searchAddMovie(String text) {
        return myOMDBConnector.searchQuery(text);
    }

    @Override
    public Movie searchSelectedMovie(String imdbID) {
        return myOMDBConnector.chosenMovieMoreInfo(imdbID);
    }
}