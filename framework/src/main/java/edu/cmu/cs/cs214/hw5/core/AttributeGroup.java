package edu.cmu.cs.cs214.hw5.core;

import java.util.ArrayList;
import java.util.Collection;
/**
 * AttributeGroup class that holds all datapoints with the
 * given specific attribute type.
 */
public class AttributeGroup {
    private final String type;
    private ArrayList<DataPoint> dataPoints = new ArrayList<>();

    /**
     * Initializes the AttributeGroup class with the String representation of type
     * @param type the type of the attribute
     */
    public AttributeGroup(String type){
        this.type = type;
    }

    /**
     * @param point to add
     */
    public void addDataPoint(DataPoint point){
        dataPoints.add(point);
    }

    /**
     * Adds multiple, already-instantiated DataPoints into an attribute group
     * @param points
     */
    public void addDataPoints(Collection<DataPoint> points){
        dataPoints.addAll(points);
    }

    /**
     * @return all the Data Points associated to the particular instance of AttributeGroup
     */
    public ArrayList<DataPoint> getDataPoints(){
        return new ArrayList<>(dataPoints);
    }

    /**
     * @return how many points are in the AttributeGroup
     */
    public int size(){
        return dataPoints.size();
    }
}
