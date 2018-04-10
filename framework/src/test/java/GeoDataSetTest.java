import edu.cmu.cs.cs214.hw5.core.AttributeGroup;
import edu.cmu.cs.cs214.hw5.core.DataPoint;
import edu.cmu.cs.cs214.hw5.core.DataSet;
import edu.cmu.cs.cs214.hw5.core.GeoDataSet;
import org.junit.Before;
import org.junit.Test;

import java.util.*;

import static org.junit.Assert.assertEquals;

public class GeoDataSetTest {
    private DataSet geo;
    private DataPoint point;

    @Before
    public void setup() {
        geo = new GeoDataSet(null, "Geo");
        Map<String, Double> attrs = new HashMap<>();
        attrs.put("Weather", 34.0);
        attrs.put("Humidity", 30.0);
        attrs.put("AQI", 1.0);
        point = new DataPoint(1.001, 2.001, 3.001, attrs);

    }

    @Test
    public void name() {
        assertEquals("Geo", geo.getName());
    }

    @Test
    public void constructor(){
        List<DataPoint> dpList = new ArrayList<>();
        dpList.add(point);
        GeoDataSet test = new GeoDataSet(dpList, "Tester");
        Set<String> geoset = new HashSet<>();
        geoset.add("Weather");
        geoset.add("AQI");
        geoset.add("Humidity");
        assertEquals(geoset, test.getAttributes());
    }

    @Test
    public void makePoint() {
        Map<String, Double> geomap = new HashMap<>();
        geomap.put("Weather", 4.001);
        geomap.put("AQI", 5.001);
        geo.makePoint(1.001, 2.001, 3.001, geomap,"");
        Set<String> geoset = new HashSet<>();
        geoset.add("Weather");
        geoset.add("AQI");
        assertEquals(geoset, geo.getAttributes());
    }

    @Test
    public void getAttributeGroups(){
        Map<String, Double> geomap = new HashMap<>();
        geomap.put("Weather", 4.001);
        geomap.put("AQI", 5.001);
        DataPoint point2 = new DataPoint(1.005, 2.005, 3.005, geomap);
        ArrayList<DataPoint> dpList = new ArrayList<>();
        dpList.add(point);
        dpList.add(point2);
        GeoDataSet geoNew = new GeoDataSet(dpList, "tester");
        AttributeGroup weather = geoNew.getAttributeGroup("Weather");
        assertEquals(dpList, weather.getDataPoints());
    }

    @Test
    public void getDataPoints(){
        Map<String, Double> geomap = new HashMap<>();
        geomap.put("Weather", 4.001);
        geomap.put("AQI", 5.001);
        DataPoint point2 = new DataPoint(1.005, 2.005, 3.005, geomap);
        ArrayList<DataPoint> dpList = new ArrayList<>();
        dpList.add(point);
        dpList.add(point2);
        GeoDataSet geoNew = new GeoDataSet(dpList, "tester");
        assertEquals(dpList, geoNew.getDataPoints());
    }





}

