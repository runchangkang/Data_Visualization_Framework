package edu.cmu.cs.cs214.hw5.core;

import java.util.*;

/**
 * The DataSet interface which the client and the framework can see.
 * This is the overall structure of the data in the GeoFilter framework.
 */
public abstract class DataSet {

    //protected Map<String,AttributeGroup> attributeGroups;
    protected Set<String> attributes = new HashSet<>();
    protected List<DataPoint> pointSet = new ArrayList<>();
    protected String name;

    /**
     */
    public DataSet(String name, List<DataPoint> existingPoints){
        this.pointSet.addAll(existingPoints);
        for (DataPoint pt: existingPoints) {
            attributes.addAll(pt.getAttributes());
        }
        this.name = name;
    }

    /**
     * Takes in the x, y, t, and other data to create and store the point in the framework.
     * @param x the x coordinate of the data
     * @param y the y coordinate of the data
     * @param t the t, or time, coordinate of the data
     * @param attributes a map of various attributes, with the name mapped to the value
     */
    public abstract void makePoint(double x, double y, double t, Map<String, Double> attributes);

    /**
     * @param attribute to get
     * @return this dataset's container associated with that attribute
     */
    public AttributeGroup getAttributeGroup(String attribute){
        AttributeGroup newGroup = new AttributeGroup(attribute);
        for (DataPoint pt : pointSet) {
            if (pt.hasAttr(attribute)) {
                newGroup.addDataPoint(pt);
            }
        }
        return newGroup;
    }

    /**
     * @return all of the attributes this set currently contains
     */
    public Set<String> getAttributes(){
        return new HashSet<>(attributes);
    }

    public String getName(){
        return this.name;
    }

    public Collection<DataPoint> getDataPoints(){
        return new ArrayList<>(this.pointSet);
    }

    public int size(){
        return pointSet.size();
    }

    public void printSet(){
        System.out.println(name + " : " + pointSet.size() + " values");
        for (DataPoint pt : pointSet){
            System.out.println(pt);
        }
        System.out.println("\n");
    }
}

