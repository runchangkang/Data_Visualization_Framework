import edu.cmu.cs.cs214.hw5.core.*;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.ExpectedException;

import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static org.junit.Assert.assertEquals;

public class QueryableSetTest {


    private AttributeGroup weather;
    private AttributeGroup magnitude;
    private DataPoint point1;
    private DataPoint point2;
    private DataPoint point3;
    private DataPoint point4;
    private DataPoint point5;
    private DataPoint point6;
    private DataPoint point7;
    private DataPoint point8;
    private DataPoint point9;
    private QueryableSet queryableSet;

    @Rule
    public ExpectedException expectedEx = ExpectedException.none();

    @Before
    public void setup(){

        Map<String, Double> attrs1 = new HashMap<>();
        attrs1.put("Weather", 1.0);
        point1 = new DataPoint(1.001, 2.001, 3.001, attrs1);
        Map<String, Double> attrs2 = new HashMap<>();
        attrs2.put("Weather", 2.0);
        point2 = new DataPoint(1.002, 2.002, 3.002, attrs2);
        Map<String, Double> attrs3 = new HashMap<>();
        attrs3.put("Weather", 3.0);
        point3 = new DataPoint(1.003, 2.003, 3.003, attrs3);
        Map<String, Double> attrs4 = new HashMap<>();
        attrs4.put("Weather", 4.0);
        point4 = new DataPoint(1.004, 2.004, 3.004, attrs4);
        Map<String, Double> attrs5 = new HashMap<>();
        attrs5.put("Weather", 5.0);
        point5 = new DataPoint(1.005, 2.005, 3.005, attrs5);
        Map<String, Double> attrs6 = new HashMap<>();
        attrs6.put("Weather", 6.0);
        point6 = new DataPoint(3.001, 2.001, 3.001, attrs6);
        Map<String, Double> attrs7 = new HashMap<>();
        attrs7.put("Weather", 7.0);
        point7 = new DataPoint(3.002, 2.002, 3.002, attrs7);
        Map<String, Double> attrs8 = new HashMap<>();
        attrs8.put("Weather", 8.0);
        point8 = new DataPoint(3.003, 2.003, 3.003, attrs8);
        Map<String, Double> attrs9 = new HashMap<>();
        attrs9.put("Weather", 9.0);
        point9 = new DataPoint(3.004, 2.004, 3.004, attrs9);
        List<DataPoint> dataPoints = Arrays.asList(point1,point2,point3,point4,point5,point6,point7,point8,point9);
        DataSet dataSet = new GeoDataSet(dataPoints,"test");
        this.queryableSet = new QueryableSet(dataSet);

    }

    @Test
    public void testKDTree(){
        assertEquals((double)7,this.queryableSet.querySet(3,2,3,"Weather"),1e-9);
    }
}
