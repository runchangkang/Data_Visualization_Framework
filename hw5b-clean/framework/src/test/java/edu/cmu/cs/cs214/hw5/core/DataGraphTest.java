package edu.cmu.cs.cs214.hw5.core;

import org.junit.Before;
import org.junit.Test;

import java.util.*;

import static org.junit.Assert.assertEquals;

public class DataGraphTest {
    private DataGraph dg;
    private DataSet d1 = null;
    private DataSet d2 = null;
    private ArrayList<DataSet> dList;
    private Relation rl;
    private Processor p;

    @Before
    public void setup() throws Exception {
        dg = new DataGraph();

        MockDataPlugin mock = new MockDataPlugin();
        Map<String, String> map = new HashMap<>();
        dg.addClientSet(mock.getCollection(map), mock.getName());
        dg.addClientSet(mock.getCollection(map), "Target");
        dList = (ArrayList) dg.getDataSets();
        for(DataSet ds : dList){
            if(ds.name.equals("Target")){
                d2 = ds;
            }
            else{
                d1 = ds;
            }
        }
        p = new Join();
        rl = new Relation(d1, d2);

        dg.addRelation(rl);

    }

    @Test
    public void getDataSet(){
       assertEquals(dList, dg.getDataSets());
    }

    @Test
    public void getRelations(){
        assertEquals(new ArrayList<Relation>(Arrays.asList(rl)), dg.getRelations());
    }

    @Test
    public void addDataSet(){
        GeoDataSet geo = new GeoDataSet(null, "Geo");
        Map<String, Double> attrs = new HashMap<>();
        attrs.put("Weather", 34.0);
        attrs.put("Humidity", 30.0);
        attrs.put("AQI", 1.0);
        DataPoint point = new DataPoint(1.001, 2.001, 3.001, attrs);
        dg.addDataSet(geo);
        assertEquals(3, dg.getDataSets().size());
    }

    @Test
    public void numParents(){
        assertEquals(1, dg.numParents(d2));
        GeoDataSet geo = new GeoDataSet(null, "Geo");
        Map<String, Double> attrs = new HashMap<>();
        attrs.put("Weather", 34.0);
        attrs.put("Humidity", 30.0);
        attrs.put("AQI", 1.0);
        DataPoint point = new DataPoint(1.001, 2.001, 3.001, attrs);
        dg.addDataSet(geo);
        Relation rl2 = new Relation(geo, d2);
        dg.addRelation(rl2);
        assertEquals(2, dg.numParents(d2));
    }

    @Test
    public void getParents(){
        assertEquals(d1, dg.getParent(d2));
    }

    @Test
    public void getAllParnets(){
        GeoDataSet geo = new GeoDataSet(null, "Geo");
        Map<String, Double> attrs = new HashMap<>();
        attrs.put("Weather", 34.0);
        attrs.put("Humidity", 30.0);
        attrs.put("AQI", 1.0);
        DataPoint point = new DataPoint(1.001, 2.001, 3.001, attrs);
        dg.addDataSet(geo);
        Relation rl2 = new Relation(geo, d2);
        dg.addRelation(rl2);
        assertEquals(new ArrayList<>(Arrays.asList(d1, geo)), dg.getAllParents(d2));
    }


}
