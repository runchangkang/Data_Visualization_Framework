package edu.cmu.cs.cs214.hw5.core;

import java.util.HashMap;
import java.util.Map;

/**
 * Library Point class set up for the Data Plugin interface to call - accessible to client.
 */
public class ClientPoint {

    private double x;
    private double y;
    private double t;
    private Map<String,Double> attributes;

    /**
     * Creates a new point
     * @param x val
     * @param y val
     * @param t val
     * @param attributes mapped strings to attribute values
     */
    public ClientPoint(double x, double y, double t, Map<String,Double> attributes){
        this.x = x;
        this.y = y;
        this.t = t;
        this.attributes = new HashMap<>(attributes);
    }

    /**
     * @return x
     */
    public double getX() {
        return x;
    }

    /**
     * @return y
     */
    public double getY() {
        return y;
    }

    /**
     * @return t
     */
    public double getT() {
        return t;
    }

    /**
     * @return attribute values
     */
    public Map<String, Double> getAttr() {
        return new HashMap<>(attributes);
    }

    @Override
    public String toString() {
        String start =  "< x: " + x + " y: " + y + " t: " + t;
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
