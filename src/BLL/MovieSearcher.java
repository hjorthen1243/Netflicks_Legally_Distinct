package BLL;

import BE.Movie;

import java.util.ArrayList;
import java.util.List;

public class MovieSearcher {
    /**
     * Creates list of movies called searchResult.
     * Runs all titles and years of the movies against the movies in the list called searchBase by using the methods compareToMovieTitle
     * and the method compareMovieYear if they match the movie is added to the searchResult.
     * Returns the searchResult.
     */
    public List<Movie> search(List<Movie> searchBase, String query) {
        List<Movie> searchResult = new ArrayList<>();

        for (Movie movie : searchBase) {
            if (compareToMovieTitle(query, movie) || compareMovieYear(query, movie)) {
                searchResult.add(movie);
            }
        }

        return searchResult;
    }
    /**
     * Creates list of movies called imdbSearchResult.
     * Runs all imdb ratings of the movies against the movies in the list called imdbSearchBase by using the result of the method
     * compareIMDbRatingMin when given the string imdbQuery and the movie
     * if they match the movie is added to the imdbSearchResult.
     * Returns the imdbSearchResult.
     */
    public List<Movie> searchImdbMin(List<Movie> imdbSearchBase, String imdbQuery) {
        List<Movie> imdbSearchResult = new ArrayList<>();

        for (Movie movie : imdbSearchBase) {
            if (compareIMDbRatingMin(imdbQuery, movie)) {
                imdbSearchResult.add(movie);
            }
        }
        return imdbSearchResult;
    }
    /**
     * Creates list of movies called imdbSearchResult.
     * Runs all imdb ratings of the movies against the movies in the list called imdbSearchBase by using the result of the method
     * compareIMDbRatingMax when given the string imdbQuery and the movie
     * if they match the movie is added to the imdbSearchResult.
     * Returns the imdbSearchResult.
     */
    public List<Movie> searchImdbMax(List<Movie> imdbSearchBase, String imdbQuery) {
        List<Movie> imdbSearchResult = new ArrayList<>();


        for (Movie movie : imdbSearchBase) {
            if (compareIMDbRatingMax(imdbQuery, movie)) {
                imdbSearchResult.add(movie);
            }
        }
        return imdbSearchResult;
    }
    /**
     * Creates list of movies called imdbSearchResult.
     * Runs all imdb ratings of the movies against the movies in the list called imdbSearchBase by using the result of the method
     * compareIMDbRating when given the string imdbMinStr, the string imdbMaxStr and the movie
     * if they match the movie is added to the imdbSearchResult.
     * Returns the imdbSearchResult.
     */
    public List<Movie> searchImdbMinAndMax(List<Movie> imdbSearchBase, String imdbMinStr, String imdbMaxStr) {
        List<Movie> imdbSearchResult = new ArrayList<>();


        for (Movie movie : imdbSearchBase) {
            if (compareIMDbRating(imdbMinStr, imdbMaxStr, movie)) {
                imdbSearchResult.add(movie);
            }
        }
        return imdbSearchResult;
    }
    /**
     * Creates list of movies called pRateSearchResult.
     * Runs all personal ratings of the movies against the movies in the list called pRateSearchBase by using the result of the method
     * comparePRatingMin when given the string pRateQuery and the movie
     * if they match the movie is added to the pRateSearchResult.
     * Returns the pRateSearchResult.
     */
    public List<Movie> searchPRateMin(List<Movie> pRateSearchBase, String pRateQuery) {
        List<Movie> pRateSearchResult = new ArrayList<>();

        for (Movie movie : pRateSearchBase) {
            if (comparePRatingMin(pRateQuery, movie)) {
                pRateSearchResult.add(movie);
            }
        }
        return pRateSearchResult;
    }
    /**
     * Creates list of movies called pRateSearchResult.
     * Runs all personal ratings of the movies against the movies in the list called pRateSearchBase by using the result of the method
     * comparePRatingMax when given the string pRateQuery and the movie
     * if they match the movie is added to the pRateSearchResult.
     * Returns the pRateSearchResult.
     */
    public List<Movie> searchPRateMax(List<Movie> pRateSearchBase, String pRateQuery) {
        List<Movie> pRateSearchResult = new ArrayList<>();

        for (Movie movie : pRateSearchBase) {
            if (comparePRatingMax(pRateQuery, movie)) {
                pRateSearchResult.add(movie);
            }
        }
        return pRateSearchResult;
    }
    /**
     * Creates list of movies called pRateSearchResult.
     * Runs all personal ratings of the movies against the movies in the list called pRateSearchBase by using the result of the method
     * comparePRating when given the string pRateMinStr, the string pRateMaxStr and the movie
     * if they match the movie is added to the pRateSearchResult.
     * Returns the pRateSearchResult.
     */
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

    /**
     * Makes imdbQuery into an integer and checks if it is equal to or greater than the movies imdb rating if it is returns true else it returns false.
     */
    private boolean compareIMDbRatingMin(String imdbQuery, Movie movie) {
        boolean imdbRatingMin = false;
        if (movie.getImdbRating() >= Double.parseDouble(imdbQuery)) {
            imdbRatingMin = true;
        }
        return imdbRatingMin;
    }
    /**
     * Makes imdbQuery into an integer and checks if it is equal to or less than the movies imdb rating if it is returns true else it returns false.
     */
    private boolean compareIMDbRatingMax(String imdbQuery, Movie movie) {
        boolean imdbRatingMax = false;
        if (movie.getImdbRating() <= Double.parseDouble(imdbQuery)) {
            imdbRatingMax = true;
        }
        return imdbRatingMax;
    }
    /**
     * Makes imdbMinStr and imdbMaxStr into integers and checks if the imdbMinStr is equal to or greater than the movies imdb rating and
     * the imdbMaxStr is equal to or less than the movies imdb rating if both are true it returns true if not it returns false.
     */
    private boolean compareIMDbRating(String imdbMinStr, String imdbMaxStr, Movie movie) {
        boolean imdbRating = false;
        if (movie.getImdbRating() <= Double.parseDouble(imdbMaxStr) && movie.getImdbRating() >= Double.parseDouble(imdbMinStr)) {
            imdbRating = true;
        }
        return imdbRating;
    }
    /**
     * Makes pRateQuery into an integer and checks if it is equal to or greater than the movies personal rating if it is
     * returns true else it returns false.
     */
    private boolean comparePRatingMin(String pRateQuery, Movie movie) {
        boolean pRatingMin = false;
        if (movie.getPersonalRating() >= Integer.parseInt(pRateQuery)) {
            pRatingMin = true;
        }
        return pRatingMin;
    }
    /**
     * Makes pRateQuery into an integer and checks if it is equal to or less than the movies personal rating if it is returns true else it returns false.
     */
    private boolean comparePRatingMax(String pRateQuery, Movie movie) {
        boolean pRatingMax = false;
        if (movie.getPersonalRating() <= Integer.parseInt(pRateQuery)) {
            pRatingMax = true;
        }
        return pRatingMax;
    }
    /**
     * Makes pRateMinStr and pRateMaxStr into integers and checks if the pRateMinStr is equal to or greater than the movies personal rating and
     * the  pRateMaxStr is equal to or less than the movies personal rating if both are true it returns true if not it returns false.
     */
    private boolean comparePRating(String pRateMinStr, String pRateMaxStr, Movie movie) {
        boolean pRating = false;
        if (movie.getPersonalRating() <= Integer.parseInt(pRateMaxStr) && movie.getImdbRating() >= Integer.parseInt(pRateMinStr)) {
            pRating = true;
        }
        return pRating;
    }
}