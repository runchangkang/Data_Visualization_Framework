package edu.cmu.cs.cs214.hw5.core;

import org.junit.Before;
import org.junit.Test;

import java.util.HashMap;
import java.util.Map;

import static org.junit.Assert.assertEquals;

public class ClientPointTest {
    private ClientPoint pt;
    private Map<String, Double> attrs;

    @Before
    public void setup(){
        attrs = new HashMap<>();
        attrs.put("Weather", 34.0);
        attrs.put("Humidity", 30.0);
        attrs.put("AQI", 1.0);
        pt = new ClientPoint(1.001,2.001,3.001, attrs, "Test");
    }

    @Test
    public void getters(){
        assertEquals(1.001, pt.getX(), 0.0001);
        assertEquals(2.001, pt.getY(), 0.0001);
        assertEquals(3.001, pt.getT(), 0.0001);
        assertEquals(attrs, pt.getAttr());
    }
}
