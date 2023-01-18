package BLL;

import BE.Movie;

import java.util.ArrayList;
import java.util.List;

public class MovieSearcher {
    public List<Movie> search(List<Movie> searchBase, String query) {
        List<Movie> searchResult = new ArrayList<>();

        for (Movie movie : searchBase) {
            if (compareToMovieTitle(query, movie) || compareMovieYear(query, movie)) {
                searchResult.add(movie);
            }
        }

        return searchResult;
    }

    public List<Movie> searchImdb(List<Movie> imdbSearchBase, String imdbQuery) {
        List<Movie> imdbSearchResult = new ArrayList<>();


        for (Movie movie : imdbSearchBase) {
            if(compareIMDbRatingMin(imdbQuery, movie) && compareIMDbRatingMax(imdbQuery, movie)){
                imdbSearchResult.add(movie);
            }
            else if (compareIMDbRatingMin(imdbQuery, movie)) {
                imdbSearchResult.add(movie);
            }
            else if (compareIMDbRatingMax(imdbQuery, movie)) {
                imdbSearchResult.add(movie);
            }
        }
        return imdbSearchResult;
    }

    /**
     * Checks if the user input matches any of the movies titles
     */
    private boolean compareToMovieTitle(String query, Movie movie) {
        return movie.getTitle().contains(query);
    }

    /**
     * Checks if the user input matches any of the years the movies came out
     */
    private boolean compareMovieYear(String query, Movie movie) {
        return movie.getYearString().contains(query);
    }

    private boolean compareIMDbRatingMin(String imdbQuery, Movie movie) {
        boolean imdbRatingMin = false;
        if (movie.getImdbRating() >= Double.parseDouble(imdbQuery) && 10>= movie.getImdbRating() && 0<= movie.getImdbRating()) {
            imdbRatingMin = true;
        }
        return imdbRatingMin;
    }

    private boolean compareIMDbRatingMax(String imdbQuery, Movie movie) {
        boolean imdbRatingMax = false;
        if (movie.getImdbRating() <= Double.parseDouble(imdbQuery) && 10 >= movie.getImdbRating() && 0 <= movie.getImdbRating()) {
            imdbRatingMax = true;
        }
        return imdbRatingMax;
    }
}