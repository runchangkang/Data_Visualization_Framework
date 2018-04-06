package edu.cmu.cs.cs214.hw5.core;

import java.util.HashMap;
import java.util.Map;

public class GeoDataSet implements DataSet{
    private Map<String, AttributeGroup> attributeGroups;

    /**
     * Takes in the x, y, t, and other data to create and store the point in the framework.
     *
     * @param x          the x coordinate of the data
     * @param y          the y coordinate of the data
     * @param t          the t, or time, coordinate of the data
     * @param attributes a map of various attributes, with the name mapped to the value
     */
    public void makePoint(double x, double y, double t, Map<String, Double> attributes) {
        attributeGroups = new HashMap<>();
        for(Map.Entry<String, Double> entry : attributes.entrySet()){
            String type = entry.getKey();
            Double attr = entry.getValue();
            // If attributeGroup exists, just add to the appropriate attributeGroup
            if(attributeGroups.containsKey(type)){
               attributeGroups.get(type).addDataPoint(x,y,t, attr);
            }
            // If attributeGroup does not exist, create new and add to the attributeGroups
            // and also add the new data point
            else{
                AttributeGroup attrGroup = new AttributeGroup(type);
                attributeGroups.put(type, attrGroup);
                attrGroup.addDataPoint(x,y,t,attr);
            }
        }
    }
}
