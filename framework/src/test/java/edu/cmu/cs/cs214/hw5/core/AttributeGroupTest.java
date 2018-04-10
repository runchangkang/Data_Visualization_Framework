package edu.cmu.cs.cs214.hw5.core;

import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.ExpectedException;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static org.junit.Assert.assertEquals;

public class AttributeGroupTest {
    private AttributeGroup weather;
    private AttributeGroup magnitude;
    private DataPoint point1;
    private DataPoint point2;
    private DataPoint point3;
    private DataPoint point4;
    private DataPoint point5;

    @Rule
    public ExpectedException expectedEx = ExpectedException.none();

    @Before
    public void setup(){
        weather = new AttributeGroup("Weather");
        magnitude = new AttributeGroup("Magnitude");

        Map<String, Double> attrs1 = new HashMap<>();
        attrs1.put("Weather", 34.0);
        attrs1.put("AQI", 1.0);
        point1 = new DataPoint(1.001, 2.001, 3.001, attrs1);

        Map<String, Double> attrs2 = new HashMap<>(attrs1);
        point2 = new DataPoint(1.002, 2.002, 3.002, attrs2);

        Map<String, Double> attrs3 = new HashMap<>(attrs2);
        point3 = new DataPoint(1.003, 2.003, 3.003, attrs3);

        Map<String, Double> attrs4 = new HashMap<>();
        attrs4.put("Weather", 34.0);
        attrs4.put("Magnitude", 1.0);
        point4 = new DataPoint(1.004, 2.004, 3.004, attrs4);

        Map<String, Double> attrs5 = new HashMap<>(attrs4);
        point5 = new DataPoint(1.005, 2.005, 3.005, attrs5);

    }

    @Test
    public void addDataPoint(){
        weather.addDataPoint(point1);
        weather.addDataPoint(point2);
        magnitude.addDataPoint(point4);
        assertEquals(2, weather.size());
        assertEquals(1, magnitude.size());
    }

    @Test
    public void addDataPointError(){
        expectedEx.expect(IllegalArgumentException.class);
        expectedEx.expectMessage("Point not compatible with AttrGroup type");
        magnitude.addDataPoint(point3);
    }

    @Test
    public void addDataPoints(){
        List<DataPoint> list = new ArrayList<DataPoint>();
        list.add(point1);
        list.add(point2);
        list.add(point3);
        weather.addDataPoints(list);
        assertEquals(list, weather.getDataPoints());
        assertEquals(3, weather.size());
    }

    @Test
    public void addDataPointsError() {
        expectedEx.expect(IllegalArgumentException.class);
        expectedEx.expectMessage("Point not compatible with AttrGroup type");
        List<DataPoint> list = new ArrayList<DataPoint>();
        list.add(point5);
        list.add(point1);
        list.add(point2);
        magnitude.addDataPoints(list);
    }

}
