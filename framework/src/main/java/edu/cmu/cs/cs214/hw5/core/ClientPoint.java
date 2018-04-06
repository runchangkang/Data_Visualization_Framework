package edu.cmu.cs.cs214.hw5.core;

import java.util.HashMap;
import java.util.Map;

/**
 * Library Point class set up for the Data Plugin interface to call - accessible to client.
 */
public class ClientPoint {

    private int x;
    private int y;
    private int t;
    private Map<String,Double> attributes;

    /**
     * Creates a new point
     * @param x val
     * @param y val
     * @param t val
     * @param attributes mapped strings to attribute values
     */
    public ClientPoint(int x, int y, int t, Map<String,Double> attributes){
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
}
