import edu.cmu.cs.cs214.hw5.core.GeoDataSet;
import org.junit.Before;
import org.junit.Test;

import java.util.HashMap;
import java.util.Map;

public class DataGraphTest {
    

    @Before
    public void setup(){
        geo = new GeoDataSet(null, "Geo");
        Map<String, Double> attrs = new HashMap<>();
        attrs.put("Weather", 34.0);
        attrs.put("Humidity", 30.0);
        attrs.put("AQI", 1.0);
        point = new DataPoint(1.001, 2.001, 3.001, attrs);
    }

    @Test
    public void

}
