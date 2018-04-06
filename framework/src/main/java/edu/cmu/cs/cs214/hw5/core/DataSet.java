package edu.cmu.cs.cs214.hw5.core;

import java.util.HashMap;
import java.util.Map;
import java.util.Set;

/**
 * The DataSet interface which the client and the framework can see.
 * This is the overall structure of the data in the GeoFilter framework.
 */
public abstract class DataSet {

    protected Map<String,AttributeGroup> attributeGroups;

    /**
     * @param setMap new DataSet with initial values
     */
    public DataSet(Map<String,AttributeGroup> setMap){
        this.attributeGroups = new HashMap<>(setMap);
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
        return attributeGroups.get(attribute);
    }

    /**
     * @return all of the attributes this set currently contains
     */
    public Set<String> getAttributes(){
        return attributeGroups.keySet();
    }

}
