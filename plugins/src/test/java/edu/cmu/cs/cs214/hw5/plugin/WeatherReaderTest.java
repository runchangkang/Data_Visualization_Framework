package edu.cmu.cs.cs214.hw5.plugin;

import edu.cmu.cs.cs214.hw5.core.DataPlugin;
import org.junit.Before;
import org.junit.Test;

import java.util.HashMap;
import java.util.Map;

public class WeatherReaderTest {
    private DataPlugin weather;
    private final String CITY_NAME = "City";
    private final String COUNTRY_LABEL = "Country Code (ISO 3166)";
    private final String NAME = "Weather Reader";

    @Before
    public void setup(){
        weather = new WeatherReader();
    }

    //Test no longer valid without API Token.
    /*
    @Test
    public void testInputData() throws Exception {
        Map<String, String> mapping = new HashMap<>();
        mapping.put(CITY_NAME, "Seoul");
        mapping.put(COUNTRY_LABEL, "kr");
        weather.getCollection(mapping);
    }*/
}
