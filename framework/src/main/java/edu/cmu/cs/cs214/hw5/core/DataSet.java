package edu.cmu.cs.cs214.hw5.core;

import java.util.Map;

/**
 * The DataSet interface which the client and the framework can see.
 * This is the overall structure of the data in the GeoFilter framework.
 */
public interface DataSet {

    /**
     * Takes in the x, y, t, and other data to create and store the point in the framework.
     * @param x the x coordinate of the data
     * @param y the y coordinate of the data
     * @param t the t, or time, coordinate of the data
     * @param attributes a map of various attributes, with the name mapped to the value
     */
    public void makePoint(double x, double y, double t, Map<String, Double> attributes);


}
