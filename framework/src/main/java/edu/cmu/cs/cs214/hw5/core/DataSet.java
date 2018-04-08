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
     *
     * @param name name of the Data Set
     * @param existingPoints the already existing datapoints to use for the dataset
     */
    public DataSet(String name, List<DataPoint> existingPoints){
        if(existingPoints != null) {
            this.pointSet.addAll(existingPoints);
            for (DataPoint pt : existingPoints) {
                attributes.addAll(pt.getAttributes());
            }
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
     * @param attribute the string attribute
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

    /**
     *
     * @return the name of the dataset in string
     */
    public String getName(){
        return this.name;
    }

    /**
     *
     * @return all datapoints in the dataset
     */
    public Collection<DataPoint> getDataPoints(){
        return new ArrayList<>(this.pointSet);
    }

    /**
     * Prints the pointset for debugging purpose
     */
    public void printSet(){
        System.out.println(name + " : " + pointSet.size() + " values");
        for (DataPoint pt : pointSet){
            System.out.println(pt);
        }
        System.out.println("\n");
    }
}

