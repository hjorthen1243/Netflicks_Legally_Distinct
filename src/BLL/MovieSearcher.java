package BLL;

import BE.Movie;

import java.util.ArrayList;
import java.util.List;

public class MovieSearcher {
    public List<Movie> search(List<Movie> searchBase, String query){
        List<Movie> searchResult = new ArrayList<>();

        for (Movie movie : searchBase){
            if(compareTitle(query, movie) || compareYear(query, movie)){
                searchResult.add(movie);
            }
        }
        return searchResult;
    }
    private boolean compareTitle(String query, Movie movie) {return movie.getTitle().toLowerCase().contains(query.toLowerCase());}

    private boolean compareYear(String query, Movie movie) {return movie.getYearString().contains(query);}
}
