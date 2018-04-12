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
    private String label = "";

    /**
     * Takes x, y, t, and attr value to initialize the data point
     * @param x the x value of the data point
     * @param y the y value of the data point
     * @param t the time value of the data point
     * @param attributes keyMap
     */
    DataPoint(double x, double y, double t, Map<String,Double> attributes){
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
     * @return x coordinate
     */
    public double getX() {
        return x;
    }

    /**
     * @return y coordinate
     */
    public double getY() {
        return y;
    }

    /**
     * @return t value
     */
    public double getT() {
        return t;
    }

    /**
     * @return whether point has a meaningful label associated with it
     */
    private boolean hasLabel(){
        return !"".equals(this.label.replaceAll("\\s",""));
    }

    //Todo: implement visualisation plugin using this feature
    /**
     * @return the label associated with this point. Will return "" if there is none.
     */
    public String getLabel(){
        return this.label;
    }

    /**
     * Set a point's label.
     * @param s label to set
     */
    void addLabel(String s){
        this.label = s;
    }

    /**
     * Does a point have a specific attribute?
     *
     * @param name of the attribute in string format
     * @return whether the attribute exists in the point
     */
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

    /**
     * Get the value of a certain attribute at this geographical point.
     *
     * @param name attribute in string format
     * @return the actual attribute value
     * @throws IllegalArgumentException if the point does not have this attribute. Use the hasAttr() method to check!
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

    /**
     * Get all of the attributes that this point has.
     *
     * @return attributes as strings
     */
    public List<String> getAttributes(){
        return new ArrayList<>(Arrays.asList(attrNames));
    }

    /**
     * @return The string representation of the DataPoint
     */
    @Override
    public String toString() {
        String s;
        if (this.hasLabel()){
            s = this.label + " : ";
        }
        else{ s = ""; }
        String start = s + "X: " + new DecimalFormat("####.##").format(x) + "\t" +
                   "Y: " + new DecimalFormat("####.##").format(y) + "\t" +
                   "T: " + new DecimalFormat("####.##").format(t) + "\t";
        StringBuilder bldr = new StringBuilder(start);

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
