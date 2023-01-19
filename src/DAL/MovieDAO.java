package DAL;

import BE.Category;
import BE.Movie;
import com.microsoft.sqlserver.jdbc.SQLServerException;

import java.io.IOException;
import java.sql.*;
import java.time.Duration;
import java.util.ArrayList;
import java.util.List;

import static java.sql.Statement.RETURN_GENERATED_KEYS;


public class MovieDAO implements IMovieDAO {

    MyDatabaseConnector myDatabaseConnector;
    MyOMDBConnector myOMDBConnector;

    public MovieDAO() throws IOException {
        myDatabaseConnector = new MyDatabaseConnector();
        myOMDBConnector = new MyOMDBConnector();
    }

    /**
     * Gets a list of all movies in the DB.
     * @return
     * @throws SQLException
     */
    @Override
    public List<Movie> getAllMovies() throws SQLException {

        //Makes a list of movies
        ArrayList<Movie> allMovies = new ArrayList<>();

        //Try with resources on the databaseConnector
        try (Connection conn = myDatabaseConnector.getConnection()) {

            //SQL String which selects all movies from the DB
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
                String length = String.format("%02d:%02d:%02d", time.toHours(), time.toMinutesPart(), time.toSecondsPart()); //Formats the length of the movie to hh:mm:ss
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
            throw new SQLException("Could not get movies from database");
        }
    }

    /**
     * Adds a Movie to the DB
     * @param title
     * @param year
     * @param length
     * @param imdbRating
     * @param personalRating
     * @param lastViewed
     * @param pathToFile
     * @return
     * @throws SQLException
     */
    @Override
    public Movie addMovie(String title, int year, String length, double imdbRating, int personalRating, Date lastViewed, String pathToFile) throws SQLException {

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
            throw new SQLException("Could not add movie " + ex);
        }

        //Generating and returning the new movie to be fed into the observable list
        return new Movie(id, title, year, length, imdbRating, personalRating, lastViewed, pathToFile);
    }

    /**
     * Update a movie in the DB with new values
     * @param movie
     * @throws SQLException
     */
    @Override
    public void editUpdateMovie(Movie movie) throws SQLException {

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
            throw new SQLException("Could not update movie" + ex);
        }
    }

    /**
     * Delete a movie from the DB
     * @param m
     * @throws SQLException
     */
    @Override
    public void deleteMovie(Movie m) throws SQLException {
        int mID = m.getId(); //Get the id of the chosen movie
        String sql = "DELETE FROM Movie WHERE Id = " + mID + ";";  //SQL String which deletes the movie from the DB
        String sql2 = "DELETE FROM CatMovie WHERE MovieID = " + mID + ";"; //SQL String which deletes the link between the movie and the category
        //Try with resources on the databaseConnector
        try (Connection conn = myDatabaseConnector.getConnection()) {
            PreparedStatement ps = conn.prepareStatement(sql);
            PreparedStatement ps2 = conn.prepareStatement(sql2);
            //Execute the update which removes the link between movie and category first, then remove the movie from the DB
            ps2.executeUpdate();
            ps.executeUpdate();
        } catch (SQLException ex) {
            ex.printStackTrace();
            throw new SQLException("Could not delete movie" + ex);
        }
    }

    /**
     * When the user wishes to only look for movies in the DB with a specific category,
     * this method gets a list of movies which it displays for the user.
     * @param category
     * @return list of movies
     * @throws SQLException
     */
    @Override
    public List<Movie> getMoviesWithCategory(Category category) throws SQLException {
        List<Movie> allMoviesWithCategory = new ArrayList<>();
        try (Connection conn = myDatabaseConnector.getConnection()) { //Try with resources on the databaseConnector
            //SQL String which gets all movie ID's, which are attached to the specific category,  form the DB
            String sql = "SELECT DISTINCT * FROM CatMovie WHERE CategoryID =" + category.getId() + ";";
            Statement stmt = conn.createStatement();
            ResultSet rs = stmt.executeQuery(sql);
            //Loop through rows from the database result set
            while (rs.next()) {
                int id = rs.getInt("MovieID"); //Retrieve the ID's of the movies
                List<Movie> allMovies = getAllMovies();
                for (Movie movie : allMovies) { //Loop through a list of all movies to find the ones which the DB provided
                    if (movie.getId() == id) {
                        allMoviesWithCategory.add(movie); //Add the specific movies to a list to be returned to the UI
                    }
                }
            }
            return allMoviesWithCategory;
        } catch (SQLException ex) {
            ex.printStackTrace();
            throw new SQLException("Could not get the movies from the database");
        }
    }

    /**
     * When the user wishes to remove a specific category from a specific movie,
     * this method allows them to save that change in the database
     * @param movieId
     * @param categoryId
     */
    @Override
    public void removeCategoryFromMovie(int movieId, int categoryId) throws SQLException {
        //SQL String which deletes the link between category & movie from the DB
        String sql = "DELETE FROM CatMovie WHERE CategoryID =" + categoryId + " AND MovieID = " + movieId + ";";
        //Try with resources on the databaseConnector
        try (Connection conn = myDatabaseConnector.getConnection()) {
            PreparedStatement ps = conn.prepareStatement(sql);
            //Execute the update
            ps.executeUpdate();
        } catch (SQLException e) {
            throw new SQLException("Could not remove the Category from the Movie");
        }
    }

    /**
     * When the user uses the search function in the add movie window,
     * this method sends the query to the OMDBConnector
     * @param text
     * @return
     */
    @Override
    public List<Movie> searchAddMovie(String text) {
        return myOMDBConnector.searchQuery(text);
    }

    /**
     * When the user chooses from the list of matching Movies in the add movie window,
     * this method returns additional information about the movie which is then displayed to the user.
     * @param imdbID
     * @return
     */
    @Override
    public Movie searchSelectedMovie(String imdbID) {
        return myOMDBConnector.chosenMovieMoreInfo(imdbID);
    }
}