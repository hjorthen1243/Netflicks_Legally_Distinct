package BLL;

import BE.Movie;
import DAL.ICategoryDAO;
import DAL.IMovieDao;
import DAL.MovieDAO;

import java.util.List;

public class MovieManager {
    private final IMovieDao movieDAO;


    public MovieManager() {
        movieDAO = new MovieDAO();
    }
    public List<Movie> getAllMovies() throws Exception {
        return movieDAO.getAllMovies();
    }

}
