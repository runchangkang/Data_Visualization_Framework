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
import java.util.*;

public class WeatherReader implements DataPlugin {
    private final String TOKEN = "70a98b0d680c7284430c43e811999bdd";
    private final String COUNTRY_LABEL = "Country Code (ISO 3166)";
    private final String CITY_NAME = "City";
    private final String NAME = "Weather Reader";

    /**
     * @return name of this data plugin in the selection screen
     */
    @Override
    public String getName() {
        return NAME;
    }

    /**
     * @return list of field labels that the framework will prompt the client to provide
     */
    @Override
    public List<String> getPopupParameters() {
        return new ArrayList<>(Arrays.asList(COUNTRY_LABEL, CITY_NAME));
    }

    /**
     * @param argumentMap map from field parameters to client-provided arguments
     * @return collection of Points ready to be parsed into a DataSet
     * @throws Exception if there was an I/O or parsing error (FileNotFound, I/O Exception, ParseException)
     */
    @Override
    public Collection<ClientPoint> getCollection(Map<String, String> argumentMap) throws Exception {
        ArrayList<ClientPoint> pointList = new ArrayList<>();

        String website = "http://api.openweathermap.org/data/2.5/forecast?q=";
        String token = "&APPID=" + TOKEN;
        String city = argumentMap.get(CITY_NAME);
        String country = argumentMap.get(COUNTRY_LABEL);
        String requestURL = website + city + "," + country + token;
        URL url = new URL(requestURL);
        InputStream is = url.openStream();
        try {
              /* Now read the retrieved document from the stream. */
            HttpURLConnection request = (HttpURLConnection) url.openConnection();
            request.connect();

            // Convert to a JSON object to print data
            JsonParser jp = new JsonParser(); //from gson
            JsonElement root = jp.parse(new InputStreamReader((InputStream) request.getContent())); //Convert the input stream to a json element
            JsonObject rootobj = root.getAsJsonObject(); //May be an array, may be an object.

            JsonObject cityJson = rootobj.getAsJsonObject("city");
            JsonObject coords = cityJson.getAsJsonObject("coord");
            Double lon = coords.get("lon").getAsDouble();
            Double lat = coords.get("lat").getAsDouble();

            JsonArray list = rootobj.getAsJsonArray("list");

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

                ClientPoint cp = new ClientPoint(lon, lat, time, attrs, "3 Hour Incremental");
                pointList.add(cp);
            }

        } finally {
            is.close();
        }

        return pointList;
    }

}
