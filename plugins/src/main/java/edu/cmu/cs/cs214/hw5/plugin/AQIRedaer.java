package edu.cmu.cs.cs214.hw5.plugin;

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

public class AQIRedaer implements DataPlugin{
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
     * @throws Exception if there was an I/O or parsing error (FileNotFound, I/O Exception, ParseException)
     */
    @Override
    public Collection<ClientPoint> getCollection(Map<String, String> argumentMap) throws Exception {
        List<ClientPoint> pointList = new ArrayList<>();

        String requestURL = "https://api.waqi.info/feed/" + argumentMap.get("City") + "/?token=" + TOKEN;
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
            System.out.println(rootobj); //just grab the zipcode
        } finally {
            is.close();
        }

        return pointList;
    }
}
