package edu.cmu.cs.cs214.hw5.core.processors;

import edu.cmu.cs.cs214.hw5.core.DataPoint;
import org.junit.Before;
import org.junit.Test;

import java.util.HashMap;
import java.util.Map;

import static org.junit.Assert.assertEquals;

public class DataPointTest {
    private DataPoint point;

    @Before
    public void setup(){
        Map<String, Double> attrs = new HashMap<>();
        attrs.put("Weather", 34.0);
        attrs.put("Humidity", 30.0);
        attrs.put("AQI", 1.0);
        point = new DataPoint(1.001, 2.001, 3.001, attrs);
    }

    @Test
    public void getters(){
        assertEquals(1.001, point.getX(), 0.001);
        assertEquals(2.001, point.getY(), 0.001);
        assertEquals(3.001, point.getT(), 0.001);
        assertEquals(34.0, point.getAttribute("Weather"), 0.001);
        assertEquals(30.0, point.getAttribute("Humidity"),0.001);
        assertEquals(1.0, point.getAttribute("AQI"), 0.001);
    }

    @Test
    public void hasAttr(){
        assertEquals(true, point.hasAttr("Weather"));
        assertEquals(true, point.hasAttr("AQI"));
        assertEquals(false, point.hasAttr("Volcano"));
    }

    @Test (expected = IllegalArgumentException.class)
    public void getAttributeException(){
        point.getAttribute("Wasabi");
    }

}
