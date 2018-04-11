package edu.cmu.cs.cs214.hw5.core;

import org.junit.Assert;
import org.junit.Test;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;

/**
 * Tests that basic filter and transform expressions are being created and applied correctly
 */
public class ExpressionParserTest {

    @Test
    public void transformTest(){
        Assert.assertEquals(ExpressionParser.parseTransformExpression("2*x^2 + 2*x + 2").apply(2.0),14.0,1e-9);
        Assert.assertEquals(ExpressionParser.parseTransformExpression("3 * (x + 2*x)").apply(2.0),18.0,1e-9);
        Assert.assertEquals(ExpressionParser.parseTransformExpression("-x").apply(1.0),-1.0,1e-9);
    }

    @Test
    public void transformSetTest(){
        Map <String,Double> attrMap = new HashMap<>();
        attrMap.put("Test1",2.0);
        attrMap.put("Test2",-2.0);

        DataPoint point0 = new DataPoint(0,0,0,attrMap);
        DataPoint point1 = new DataPoint(-1,-1,-1,attrMap);
        DataPoint point2 = new DataPoint(5,5,5,attrMap);
        DataSet set = new GeoDataSet(new ArrayList<>(Arrays.asList(point0,point1,point2))," ");

        Map<String,String> transformMap = new HashMap<>();
        transformMap.put("Test1","x - 20");
        transformMap.put("Test2","x^2");

        Transform t1 = new Transform(transformMap);
        DataSet transformSet = t1.apply(new ArrayList<>(Arrays.asList(set)));

        for (DataPoint p : transformSet.getDataPoints()){
            Assert.assertEquals(p.getAttribute("Test1"),-18.0,1e-9);
            Assert.assertEquals(p.getAttribute("Test2"),4.0,1e-9);
        }
    }


    //todo: verify filters work on full set (negation & double containment)
    @Test
    public void filterTest(){
        Assert.assertTrue(ExpressionParser.parseFilterExpression("x == 2").apply(2.0));
        Assert.assertTrue(ExpressionParser.parseFilterExpression("x < 3.0").apply(2.0));
        Assert.assertFalse(ExpressionParser.parseFilterExpression("x > 3.0").apply(2.0));
        Assert.assertFalse(ExpressionParser.parseFilterExpression("x < 3.0 & x > 2.5").apply(2.0));
        Assert.assertFalse(ExpressionParser.parseFilterExpression("x < 3.0 & x > 2.5").apply(0.0));
        Assert.assertTrue(ExpressionParser.parseFilterExpression("x < 2.5 | x > 3.0").apply(0.0));
        Assert.assertTrue(ExpressionParser.parseFilterExpression("!(x < 2.5 | x > 3.0)").apply(0.0));
    }

    @Test
    public void filterSetTest(){
        Map <String,Double> attrMap = new HashMap<>();
        attrMap.put("Test1",2.0);
        attrMap.put("Test2",-2.0);

        DataPoint point0 = new DataPoint(0,0,0,attrMap);
        DataPoint point1 = new DataPoint(-1,-1,-1,attrMap);
        DataPoint point2 = new DataPoint(5,5,5,attrMap);
        DataSet set = new GeoDataSet(new ArrayList<>(Arrays.asList(point0,point1,point2))," ");

        Map<String,String> filterMap = new HashMap<>();
        filterMap.put(DataPoint.X_ATTRIB,"x > 0");

        Filter f1 = new Filter(filterMap);
        DataSet filterSet = f1.apply(new ArrayList<>(Arrays.asList(set)));

        Assert.assertEquals(filterSet.size(),1);
        for (DataPoint p : filterSet.getDataPoints()){
            Assert.assertTrue(p.getX() > 0);
        }

        Map<String,String> filterMap2 = new HashMap<>();
        filterMap2.put(DataPoint.X_ATTRIB,"x < 0 | x == 0");
        Filter f2 = new Filter(filterMap2);
        DataSet filterSet2 = f2.apply(new ArrayList<>(Arrays.asList(set)));

        Assert.assertEquals(2,filterSet2.size());
        for (DataPoint p : filterSet2.getDataPoints()){
            Assert.assertTrue(p.getX() <= 0);
        }
    }


    @Test
    public void joinSetTest(){

        Map <String,Double> attrMap = new HashMap<>();
        attrMap.put("Test1",2.0);
        attrMap.put("Test2",-2.0);

        DataPoint point0 = new DataPoint(0,0,0,attrMap);
        DataPoint point1 = new DataPoint(-1,-1,-1,attrMap);
        DataPoint point2 = new DataPoint(5,5,5,attrMap);

        DataSet set1 = new GeoDataSet(new ArrayList<>(Arrays.asList(point0,point1,point2)),"set1");

        Map <String,Double> attrMap2 = new HashMap<>();
        attrMap2.put("Test3",2.0);
        attrMap2.put("Test4",-2.0);

        DataPoint point3 = new DataPoint(0,0,0,attrMap2);
        DataPoint point4 = new DataPoint(-1,-1,-1,attrMap2);
        DataPoint point5 = new DataPoint(5,5,5,attrMap2);

        DataSet set2 = new GeoDataSet(new ArrayList<>(Arrays.asList(point3,point4,point5)),"set2");

        Join j1 = new Join();
        DataSet joinSet = j1.apply(new ArrayList<>(Arrays.asList(set1,set2)));

        Assert.assertEquals(6,joinSet.size());
        Assert.assertEquals(4,joinSet.getAttributes().size());
        Assert.assertEquals(3,joinSet.getAttributeGroup("Test1").size());
        Assert.assertEquals(3,joinSet.getAttributeGroup("Test3").size());

    }
}