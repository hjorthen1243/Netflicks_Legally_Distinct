package BLL;

import BE.Movie;
import DAL.IMovieDAO;
import DAL.MovieDAO;

import java.util.Date;
import java.util.List;

public class MovieManager {
    private IMovieDAO movieDAO;
    private MovieSearcher movieSearcher = new MovieSearcher();


    public void movieManager(){movieDAO = new MovieDAO();}

    public List<Movie> getAllMovies() throws Exception {
        return movieDAO.getAllMovies();
    }

    public List<Movie> searchMovies(String query) throws Exception {
        List<Movie> allMovies = getAllMovies();
        List<Movie> searchResult = movieSearcher.search(allMovies, query);
        return searchResult;
    }

    public Movie createNewMovie(String name, double imdbRating, String pathToFile, Date lastView) throws Exception {
        return movieDAO.addMovie(name, imdbRating, pathToFile, lastView);
    }

    public void deleteMovie(Movie movie) throws Exception {
        movieDAO.deleteMovie(movie);
    }

    public void updateMovie(Movie updatedMovie) throws Exception {
        movieDAO.editUpdateMovie(updatedMovie);
    }


}
