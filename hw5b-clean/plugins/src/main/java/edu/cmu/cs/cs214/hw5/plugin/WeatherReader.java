package edu.cmu.cs.cs214.hw5.plugin;

import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import edu.cmu.cs.cs214.hw5.core.ClientPoint;
import edu.cmu.cs.cs214.hw5.core.DataPlugin;

import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * The WeatherReader uses the OpenWeatherMap API (http://api.openweathermap.org) to fetch
 * 5-Day forecast data. The user is expected to provide their own API key (see plugin documentation in the Wiki).
 */
public class WeatherReader implements DataPlugin {
    private final static String TOKEN_PARAM = "API Token";
    private final static String COUNTRY_LABEL = "Country Code (ISO 3166)";
    private final static String CITY_NAME = "City";
    private final static String NAME = "Weather Reader";

    /**
     * @return Name of this data plugin in the selection screen
     */
    @Override
    public String getName() {
        return NAME;
    }

    /**
     * @return List of field labels that the framework will prompt the client to provide
     */
    @Override
    public List<String> getPopupParameters() {
        return new ArrayList<>(Arrays.asList(TOKEN_PARAM,COUNTRY_LABEL, CITY_NAME));
    }

    /**
     * The client implements this method to fetch the data from the data source. The client is provided the results
     * of the getPopupParameters they implemented earlier in the form of a mapping from key (parameter field label)
     * to value (input result from client). The client is then responsible for validating these parameters.
     *
     * @param argumentMap map from field parameters to client-provided arguments
     * @return collection of Points ready to be input into the framework
     * @throws Exception if there was an I/O or parsing error (FileNotFound, I/O Exception, ParseException). The
     * framework will display an appropriate error to the user.
     */
    @Override
    public Collection<ClientPoint> getCollection(Map<String, String> argumentMap) throws Exception {
        ArrayList<ClientPoint> pointList = new ArrayList<>();

        //Fetching parameters from the client and setting up the API Call
        String website = "http://api.openweathermap.org/data/2.5/forecast?q=";
        String token = "&APPID=" + argumentMap.get(TOKEN_PARAM);
        String city = argumentMap.get(CITY_NAME);
        String country = argumentMap.get(COUNTRY_LABEL);
        String requestURL = website + city + "," + country + token;
        URL url = new URL(requestURL);
        InputStream is = url.openStream();
        try {
            // Now read the retrieved document from the stream.
            HttpURLConnection request = (HttpURLConnection) url.openConnection();
            request.connect();

            // Convert to a JSON object to print data
            JsonParser jp = new JsonParser(); //from gson
            //Convert the input stream to a json element
            JsonElement root = jp.parse(new InputStreamReader((InputStream) request.getContent()));
            JsonObject rootobj = root.getAsJsonObject(); //May be an array, may be an object.

            JsonObject cityJson = rootobj.getAsJsonObject("city");
            JsonObject coords = cityJson.getAsJsonObject("coord");
            Double lon = coords.get("lon").getAsDouble();
            Double lat = coords.get("lat").getAsDouble();

            JsonArray list = rootobj.getAsJsonArray("list");

            //Extract relevant parameters to double format
            for(JsonElement dayElem : list){
                JsonObject day = (JsonObject) dayElem;
                Double time = day.get("dt").getAsDouble();

                JsonObject dayMain = day.getAsJsonObject("main");
                Double temp = dayMain.get("temp").getAsDouble();
                Double tempmin = dayMain.get("temp_min").getAsDouble();
                Double tempmax = dayMain.get("temp_max").getAsDouble();
                Double pressure = dayMain.get("pressure").getAsDouble();
                Double seaLV = dayMain.get("sea_level").getAsDouble();
                Double grndLV = dayMain.get("grnd_level").getAsDouble();
                Double humidity = dayMain.get("humidity").getAsDouble();

                JsonObject wind = day.getAsJsonObject("wind");
                Double windSpeed = wind.get("speed").getAsDouble();
                Double windDegree = wind.get("deg").getAsDouble();

                JsonObject rain = day.getAsJsonObject("rain");
                Double rain3h = 0.0;
                if(rain != null){
                    if (rain.size() != 0){
                        rain3h = rain.get("3h").getAsDouble();
                    }
                }

                Map<String, Double> attrs = new HashMap<>();
                attrs.put("temp", temp);
                attrs.put("tempmin",tempmin);
                attrs.put("tempmax",tempmax);
                attrs.put("pressure",pressure);
                attrs.put("seaLV",seaLV);
                attrs.put("grndLV",grndLV);
                attrs.put("humidity",humidity);
                attrs.put("windSpeed", windSpeed);
                attrs.put("windDegree", windDegree);
                attrs.put("rain3h", rain3h);

                //Create ClientPoint and add it to the collection
                ClientPoint cp = new ClientPoint(lon, lat, time, attrs, "3 Hour Incremental");
                pointList.add(cp);
            }

        } finally {
            //clean up
            is.close();
        }

        return pointList;
    }

}
