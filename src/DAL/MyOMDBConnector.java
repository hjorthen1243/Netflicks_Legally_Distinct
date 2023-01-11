package DAL;

import BE.Movie;
import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;

import java.io.IOException;
import java.net.HttpURLConnection;
import java.net.URL;
import java.sql.Connection;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Scanner;

public class MyOMDBConnector {

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

                System.out.println(informationString);

                /**
                 //JSON simple library Setup with Maven is used to convert strings to JSON
                 JSONParser parse = new JSONParser();
                 JSONArray dataObject = (JSONArray) parse.parse(String.valueOf(informationString));
                 *  Get the first JSON object in the JSON array
                 *  System.out.println(dataObject.get(0));
                 *
                 *  JSONObject data = (JSONObject) dataObject.get(0);
                 *
                 *  System.out.println(data.get("something"));
                 */


            }
        } catch (IOException /** | ParseException */e) {
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
                    int id = Integer.parseInt(searchResults.substring(searchResults.indexOf("imdbID\":\"")+11, searchResults.indexOf("\",\"Type")));

                    System.out.println(title);
                    System.out.println(year);
                    System.out.println(id);

                    //Map the data into a Movie Object
                    Movie m = new Movie(id, title, year);
                    searchMovies.add(m);
                }

            }
        } catch (IOException e) {
            throw new RuntimeException(e);

        }
        return searchMovies;
    }
}