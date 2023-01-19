package DAL;

import BE.Movie;

import java.io.IOException;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

public class MyOMDBConnector {

    public static String C1;
    public MyOMDBConnector() {
        try {
            String searchQuery = "";

            URL url = new URL("http://www.omdbapi.com/?apikey=40237601" + searchQuery);

            HttpURLConnection conn = (HttpURLConnection) url.openConnection();
            conn.setRequestMethod("GET");
            conn.connect();

            int responseCode = conn.getResponseCode();

            // 200 OK
            if (responseCode != 200) {
                throw new RuntimeException("HttpResponseCode: " + responseCode);
            } else {

                StringBuilder informationString = new StringBuilder();
                Scanner scanner = new Scanner(url.openStream());

                while (scanner.hasNext()) {
                    informationString.append(scanner.nextLine());
                }
                //Close the scanner
                scanner.close();
            }
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    public List<Movie> searchQuery(String text) {
        ArrayList<Movie> searchMovies = new ArrayList<>();

        try {
            URL url = new URL("http://www.omdbapi.com/?" + "s=" + text + "&type=movie&apikey=40237601");

            HttpURLConnection conn = (HttpURLConnection) url.openConnection();
            conn.setRequestMethod("GET");
            conn.connect();

            int responseCode = conn.getResponseCode();

            // 200 OK
            if (responseCode != 200) {
                throw new RuntimeException("HttpResponseCode: " + responseCode);
            } else {

                StringBuilder informationString = new StringBuilder();
                Scanner scanner = new Scanner(url.openStream());

                while (scanner.hasNext()) {

                    informationString.append(scanner.nextLine());
                }
                //Close the scanner
                scanner.close();

                String input = informationString.toString();
                String[] splitSearch = input.split("\\{");

                for (int i = 2; i < splitSearch.length; i++) {
                    String searchResults;
                    searchResults = splitSearch[i];
                    //Map the title, year and ID of the movies from the search result
                    String title = searchResults.substring(searchResults.indexOf("Title\":\"")+8, searchResults.indexOf("\","));
                    int year = Integer.parseInt(searchResults.substring(searchResults.indexOf("Year\":\"")+7, searchResults.indexOf("\",\"imdbID")));
                    String imdbID = searchResults.substring(searchResults.indexOf("imdbID\":\"")+11, searchResults.indexOf("\",\"Type"));

                    //Map the data into a Movie Object
                    Movie m = new Movie(title, year, imdbID);
                    searchMovies.add(m);
                }

            }
        } catch (IOException e) {
            throw new RuntimeException(e);

        }
        return searchMovies;
    }
    public Movie chosenMovieMoreInfo(String imdbID)  {
        C1 = null;
        Movie m;
        try {
            URL url = new URL("http://www.omdbapi.com/?" + "i=tt" + imdbID + "&apikey=40237601");

            HttpURLConnection conn = (HttpURLConnection) url.openConnection();
            conn.setRequestMethod("GET");
            conn.connect();

            int responseCode = conn.getResponseCode();

            // 200 OK
            if (responseCode != 200) {
                throw new RuntimeException("HttpResponseCode: " + responseCode);
            } else {

                StringBuilder informationString = new StringBuilder();
                Scanner scanner = new Scanner(url.openStream());

                while (scanner.hasNext()) {

                    informationString.append(scanner.nextLine());
                }
                //Close the scanner
                scanner.close();

                String length = informationString.substring(informationString.indexOf("Runtime\":\"")+10, informationString.indexOf(" min\","));
                double imdbRating = Double.parseDouble(informationString.substring(informationString.indexOf("imdbRating\":\"")+13, informationString.indexOf("\",\"imdbVotes")));
                if (informationString.toString().contains("Genre")) {
                    C1 = informationString.substring(informationString.indexOf("Genre\":\"") + 8, informationString.indexOf("\",\"Director"));
                }
                m = new Movie(length,imdbRating);
            }
        } catch (IOException e) {throw new RuntimeException(e);}
        return m;
    }

    public String getMovieCategories() {
        return C1;
    }
}