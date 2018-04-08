package edu.cmu.cs.cs214.hw5.core;

import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Map;

/**
 * The DataPoint representation.
 * Has x, y, time, and attribute value.
 */
public class DataPoint {

    public static final String X_ATTRIB = "X";
    public static final String Y_ATTRIB = "Y";
    public static final String T_ATTRIB = "T";

    private double x;
    private double y;
    private double t;
    private String[] attrNames;
    private double[] attr;

    /**
     * Takes x, y, t, and attr value to initialize the data point
     * @param x the x value of the data point
     * @param y the y value of the data point
     * @param t the time value of the data point
     * @param attributes keyMap
     */
    public DataPoint(double x, double y, double t, Map<String,Double> attributes){
        this.x = x;
        this.y = y;
        this.t = t;
        int size = attributes.size();
        this.attrNames = new String[size];
        this.attr = new double[size];

        int i = 0;
        for (String key : attributes.keySet()){
            attrNames[i] = key;
            attr[i] = attributes.get(key);
            i++;
        }
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
     * @return attribute value
     */
    public double getAttribute(String name) {
        if(name.equals(X_ATTRIB)){
            return x;
        }
        else if(name.equals(Y_ATTRIB)){
            return y;
        }
        else if(name.equals(T_ATTRIB)){
            return t;
        }
        else {
            for (int i = 0; i < attrNames.length; i++) {
                if (attrNames[i].equals(name)) {
                    return attr[i];
                }
            }
        }

        throw new IllegalArgumentException("Point does not have this attribute.");
    }

    public boolean hasAttr(String name){
        if (name.equals(X_ATTRIB) || name.equals(Y_ATTRIB) || name.equals(T_ATTRIB)){
            return true;
        }
        for (String attribute : attrNames){
            if (attribute.equals(name)){
                return true;
            }
        }
        return false;
    }

    public List<String> getAttributes(){
        return new ArrayList<>(Arrays.asList(attrNames));
    }

    @Override
    public String toString() {
        String s = "X: " + new DecimalFormat("####.##").format(x) + "\t" +
                   "Y: " + new DecimalFormat("####.##").format(y) + "\t" +
                   "T: " + new DecimalFormat("####.##").format(t) + "\t";
        StringBuilder bldr = new StringBuilder(s);
        bldr.append(" [ ");
        for (int i = 0; i < attrNames.length; i++){
            bldr.append(attrNames[i]);
            bldr.append(" : ");
            bldr.append(new DecimalFormat("####.####").format(attr[i]));
            if (i != attrNames.length -1 ) {
                bldr.append(" \t ");
            }
        }
        bldr.append(" ] ");
        return bldr.toString();
    }
}
