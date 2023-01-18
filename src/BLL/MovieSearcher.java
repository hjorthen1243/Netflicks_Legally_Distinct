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
    public List<Movie> searchImdbMin(List<Movie> imdbSearchBase, String imdbQuery) {
        List<Movie> imdbSearchResult = new ArrayList<>();

        for(Movie movie : imdbSearchBase){
            if (compareIMDbRatingMin(imdbQuery, movie)) {
                imdbSearchResult.add(movie);
            }
        }
        return imdbSearchResult;
    }

    public List<Movie> searchImdbMax(List<Movie> imdbSearchBase, String imdbQuery) {
        List<Movie> imdbSearchResult = new ArrayList<>();


        for (Movie movie : imdbSearchBase) {
            if (compareIMDbRatingMax(imdbQuery, movie)) {
                imdbSearchResult.add(movie);
            }
        }
        return imdbSearchResult;
    }
    public List<Movie> searchImdbMinAndMax(List<Movie> imdbSearchBase, String imdbMinStr, String imdbMaxStr) {
        List<Movie> imdbSearchResult = new ArrayList<>();


        for (Movie movie : imdbSearchBase) {
            if (compareIMDbRating(imdbMinStr, imdbMaxStr, movie)) {
                imdbSearchResult.add(movie);
            }
        }
        return imdbSearchResult;
    }
    public List<Movie> searchPRateMin(List<Movie> pRateSearchBase, String pRateQuery) {
        List<Movie> pRateSearchResult = new ArrayList<>();

        for(Movie movie : pRateSearchBase){
            if (comparePRatingMin(pRateQuery, movie)) {
                pRateSearchResult.add(movie);
            }
        }
        return pRateSearchResult;
    }
    public List<Movie> searchPRateMax(List<Movie> pRateSearchBase, String pRateQuery) {
        List<Movie> pRateSearchResult = new ArrayList<>();

        for(Movie movie : pRateSearchBase){
            if (comparePRatingMax(pRateQuery, movie)) {
                pRateSearchResult.add(movie);
            }
        }
        return pRateSearchResult;
    }
    public List<Movie> searchPRateMinAndMax(List<Movie> pRateSearchBase, String pRateMinStr, String pRateMaxStr){
        List<Movie> pRateSearchResult = new ArrayList<>();


        for (Movie movie : pRateSearchBase) {
            if (comparePRating(pRateMinStr, pRateMaxStr, movie)) {
                pRateSearchResult.add(movie);
            }
        }
        return pRateSearchResult;
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
        if (movie.getImdbRating() >= Double.parseDouble(imdbQuery)) {
            imdbRatingMin = true;
        }
        return imdbRatingMin;
    }

    private boolean compareIMDbRatingMax(String imdbQuery, Movie movie) {
        boolean imdbRatingMax = false;
        if (movie.getImdbRating() <= Double.parseDouble(imdbQuery)) {
            imdbRatingMax = true;
        }
        return imdbRatingMax;
    }
    private boolean compareIMDbRating(String imdbMinStr, String imdbMaxStr, Movie movie) {
        boolean imdbRating = false;
        if (movie.getImdbRating() <= Double.parseDouble(imdbMaxStr) && movie.getImdbRating() >= Double.parseDouble(imdbMinStr)) {
            imdbRating = true;
        }
        return imdbRating;
    }
    private boolean comparePRatingMin(String pRateQuery, Movie movie) {
        boolean pRatingMin = false;
        if (movie.getPersonalRating() >= Integer.parseInt(pRateQuery)) {
            pRatingMin = true;
        }
        return pRatingMin;
    }
    private boolean comparePRatingMax(String pRateQuery, Movie movie) {
        boolean pRatingMax = false;
        if (movie.getPersonalRating() <= Integer.parseInt(pRateQuery)) {
            pRatingMax = true;
        }
        return pRatingMax;
    }
    private boolean comparePRating(String pRateMinStr, String pRateMaxStr, Movie movie) {
        boolean pRating = false;
        if (movie.getPersonalRating() <= Integer.parseInt(pRateMaxStr) && movie.getImdbRating() >= Integer.parseInt(pRateMinStr)) {
            pRating = true;
        }
        return pRating;
    }

}