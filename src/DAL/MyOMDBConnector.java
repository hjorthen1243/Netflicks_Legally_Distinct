package DAL;

import BE.Category;
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

    private String C1;
    private String C2;
    private String C3;
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
                    String imdbID = searchResults.substring(searchResults.indexOf("imdbID\":\"")+11, searchResults.indexOf("\",\"Type"));

                    System.out.println(title);
                    System.out.println(year);
                    System.out.println(imdbID);

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
        C2 = null;
        C3 = null;
        Movie m = new Movie("0", 0);
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

                if (informationString.toString().contains("Genre") == true) {
                    String Categories = informationString.substring(informationString.indexOf("Genre\":\"") + 8, informationString.indexOf("\",\"Director"));
                    if (Categories.contains(",")) {
                        C1 = Categories.substring(Categories.indexOf(Categories.charAt(0)), Categories.indexOf(","));
                        C2 = Categories.substring(Categories.indexOf(",")+2);
                        if (C2.contains(",")){
                            C3 = C2.substring(C2.indexOf(" ") + 1);
                            C2 = C2.substring(C2.indexOf(C2.charAt(0)), C2.indexOf(","));
                        }
                    } else {
                        C1 = Categories;
                    }
                    System.out.println(length);
                    System.out.println(imdbRating);
                    System.out.println(Categories);
                    System.out.println(C1);
                    System.out.println(C2);
                    System.out.println(C3);

                    m = new Movie(length,imdbRating);
                }
            }
        } catch (IOException e) {throw new RuntimeException(e);}
        return m;
    }


    public String getMovieCategories() {
        String Categories;
        if(C1 == null) {
            Categories = "";
        } else if (C2 == null) {
            Categories = C1;
        } else if (C3 == null) {
            Categories = C1 + ", " + C2;
        } else {
            Categories= C1 + ", " + C2 + ", " + C3;
        }
        return Categories;
    }
}