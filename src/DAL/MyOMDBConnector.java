package DAL;

import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;

import java.io.IOException;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.Scanner;

public class MyOMDBConnector {

    public MyOMDBConnector() {
        try {
            String searchQuery = "";

            URL url = new URL("http://www.omdbapi.com/?apikey=40237601"+ searchQuery);

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


                //JSON simple library Setup with Maven is used to convert strings to JSON
                JSONParser parse = new JSONParser();
                JSONArray dataObject = (JSONArray) parse.parse(String.valueOf(informationString));

                /**
                 *  Get the first JSON object in the JSON array
                 *  System.out.println(dataObject.get(0));
                 *
                 *  JSONObject data = (JSONObject) dataObject.get(0);
                 *
                 *  System.out.println(data.get("something"));
                 */


            }
        } catch (IOException | ParseException e) {
            throw new RuntimeException(e);
        }
    }
}

