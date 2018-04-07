package edu.cmu.cs.cs214.hw5.core;

import java.util.List;
import java.util.Map;

/**
 * Implementation of DataSet Abstract Class
 */
public class GeoDataSet extends DataSet{

    /**
     * @param newSet new mapping to instantiate. possibly empty.
     */
    public GeoDataSet (List<DataPoint> newSet, String name){
        super(name,newSet);
    }
    /**
     * Takes in the x, y, t, and other data to create and store the point in the framework.
     *
     * @param x          the x coordinate of the data
     * @param y          the y coordinate of the data
     * @param t          the t, or time, coordinate of the data
     * @param attributes a map of various attributes, with the name mapped to the value
     */
    @Override
    public void makePoint(double x, double y, double t, Map<String, Double> attributes) {
        DataPoint newPoint = new DataPoint(x,y,t,attributes);
        this.attributes.addAll(attributes.keySet());
        this.pointSet.add(newPoint);
    }
}
