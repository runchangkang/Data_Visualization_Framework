package edu.cmu.cs.cs214.hw5.plugin;

import edu.cmu.cs.cs214.hw5.core.ClientPoint;
import org.junit.Before;
import org.junit.Test;

import java.util.Collection;
import java.util.HashMap;
import java.util.Map;

public class AQIReaderTest {
    private AQIReader aqi = new AQIReader();

    @Before
    public void setup(){
    }

    @Test
    public void parsing() throws Exception {
        Map<String, String> city = new HashMap<>();
        city.put("City", "Beijing");
        Collection<ClientPoint> clientPoints = aqi.getCollection(city);
    }
}
