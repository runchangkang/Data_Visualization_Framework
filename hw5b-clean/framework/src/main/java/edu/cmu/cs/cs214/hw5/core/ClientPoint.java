package edu.cmu.cs.cs214.hw5.core;

import java.util.HashMap;
import java.util.Map;

/**
 * Point class set up for the Data Source Plugin. Provides a constructor for clients implementing source plugins.
 */
public class ClientPoint {

    private double x;
    private double y;
    private double t;
    private Map<String,Double> attributes;
    private String label;

    /**
     * Creates a new point.
     * @param x val
     * @param y val
     * @param t val
     * @param attributes mapped strings to attribute values
     * @param label additional label to mark point with
     */
    public ClientPoint(double x, double y, double t, Map<String,Double> attributes, String label){
        this.x = x;
        this.y = y;
        this.t = t;
        this.attributes = new HashMap<>(attributes);
        this.label = label;
    }

    /**
     * @return x
     */
    double getX() {
        return x;
    }

    /**
     * @return y
     */
    double getY() {
        return y;
    }

    /**
     * @return t
     */
    double getT() {
        return t;
    }

    /**
     * @return attribute values
     */
    Map<String, Double> getAttr() {
        return new HashMap<>(attributes);
    }


    String getLabel(){
        return this.label;
    }

    /**
     * @return convenient string output
     */
    @Override
    public String toString() {
        String start =  this.label + " : < x: " + x + " y: " + y + " t: " + t;
        StringBuilder build = new StringBuilder(start);
        for (String attr : attributes.keySet()){
            build.append(" ");
            build.append(attr);
            build.append(": ");
            build.append(attributes.get(attr));
        }
        build.append(" >");
        return build.toString();
    }
}
