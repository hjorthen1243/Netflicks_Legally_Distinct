package BLL;

import BE.Movie;
import DAL.IMovieDAO;
import DAL.MovieDAO;

import java.util.List;

public class MovieManager {

    private IMovieDAO movieDAO;
    private MovieSearcher movieSearcher = new MovieSearcher();


    public void movieManager() {
        movieDAO = new MovieDAO();
    }

    public List<Movie> getAllMovies() throws Exception {
        return movieDAO.getAllMovies();
    }

    public List<Movie> searchMovies(String query) throws Exception {
        List<Movie> allMovies = getAllMovies();
        List<Movie> searchResult = movieSearcher.search(allMovies, query);
        return searchResult;
    }

    public Movie createNewMovie(String title, double imdbRating, String pathToFile, int lastView) throws Exception {
        return movieDAO.addMovie(title, imdbRating, pathToFile, lastView);
    }

    public void deleteMovie(Movie movie) throws Exception {
        movieDAO.deleteMovie(movie);
    }


    public void updateMovie(Movie updatedMovie) throws Exception {
        movieDAO.editUpdateMovie(updatedMovie);
    }

}