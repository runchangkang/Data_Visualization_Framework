package edu.cmu.cs.cs214.hw5.core;

import java.util.List;
import java.util.Map;

/**
 * Implementation of DataSet Abstract Class
 */
class GeoDataSet extends DataSet{

    /**
     * @param newSet new mapping to instantiate. possibly empty.
     */
    GeoDataSet (List<DataPoint> newSet, String name){
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
    void makePoint(double x, double y, double t, Map<String, Double> attributes, String label) {
        DataPoint newPoint = new DataPoint(x,y,t,attributes);
        newPoint.addLabel(label);
        this.attributes.addAll(attributes.keySet());
        this.pointSet.add(newPoint);
    }
}
