package edu.cmu.cs.cs214.hw5.core;

import java.util.ArrayList;

/**
 * AttributeGroup class that holds all datapoints with the
 * given specific attribute type.
 */
public class AttributeGroup {
    private String type;
    private ArrayList<DataPoint> dataPoints;

    /**
     * Initializes the AttributeGroup class with the String representation of type
     * @param type the type of the attribute
     */
    public AttributeGroup(String type){
        this.type = type;
    }

    /**
     * Takes x, y, t, and attr value and adds it to the AttributeGroup instance
     * @param x the x value of the data point
     * @param y the y value of the data point
     * @param t the time value of the data point
     * @param attr the attribute associated with the given x y t
     */
    public void addDataPoint(double x, double y, double t, double attr){
        DataPoint dp = new DataPoint(x, y, t, attr);
        dataPoints.add(dp);
    }

    /**
     *
     * @return all the Data Points associated to the particular instance of AttributeGroup
     */
    public ArrayList<DataPoint> getDataPoints(){
        return dataPoints;
    }

}
