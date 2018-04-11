package edu.cmu.cs.cs214.hw5.plugin;

import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import edu.cmu.cs.cs214.hw5.core.ClientPoint;
import edu.cmu.cs.cs214.hw5.core.DataPlugin;

import java.io.IOException;
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

public class AQIReader implements DataPlugin{
    private static final String NAME= "AQI Reader";
    private static final String TOKEN = "25627f7aa8a4c445f0b78b3bc5cdd3693596d8ff";
    private static final String ATTRIBUTE_LABEL = "City";
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
        return new ArrayList<>(Arrays.asList(ATTRIBUTE_LABEL));
    }

    /**
     * @param argumentMap map from field parameters to client-provided arguments
     * @return collection of Points ready to be parsed into a DataSet
     * @throws IOException if there was an I/O or parsing error (FileNotFound, I/O Exception, ParseException)
     */
    @Override
    public Collection<ClientPoint> getCollection(Map<String, String> argumentMap) throws IOException {
        List<ClientPoint> pointList = new ArrayList<>();
        String city = argumentMap.get("City");
        city = city.replace(" ", "");

        String requestURL = "https://api.waqi.info/search/?token=" + TOKEN + "&keyword=" + city;
        URL url = new URL(requestURL);
        InputStream is = url.openStream();

          /* Now read the retrieved document from the stream. */
        HttpURLConnection request = (HttpURLConnection) url.openConnection();
        request.connect();

        // Convert to a JSON object to print data
        JsonParser jp = new JsonParser(); //from gson
        JsonElement root = jp.parse(new InputStreamReader((InputStream) request.getContent())); //Convert the input stream to a json element
        JsonObject rootobj = root.getAsJsonObject(); //May be an array, may be an object.

        // Getting First Two Parts
        for(JsonElement elem : rootobj.getAsJsonArray("data")){

            JsonObject data = (JsonObject) elem;
            //System.out.println(elem);
            if(data.get("aqi").getAsString().matches(".*\\d+.*")) {
                // Getting AQI
                Double aqi = data.get("aqi").getAsDouble();

                // Getting Station
                JsonObject station = data.getAsJsonObject("station");
                String name = station.get("name").getAsString();

                // Getting Coordinates
                JsonArray coordinates = station.getAsJsonArray("geo");
                Double latitude = coordinates.get(0).getAsDouble();
                Double longitude = coordinates.get(1).getAsDouble();

                // Getting Time
                JsonObject time = data.getAsJsonObject("time");
                Double vtime = time.get("vtime").getAsDouble();

                // Forming Attribute Mapping
                Map<String, Double> attrMap = new HashMap<>();
                attrMap.put("AQI", aqi);

                pointList.add(new ClientPoint(longitude, latitude, vtime, attrMap, name));
            }
        }

        is.close();

        return pointList;
    }
}
