package edu.cmu.cs.cs214.hw5.core;

import edu.wlu.cs.levy.CG.KDTree;
import edu.wlu.cs.levy.CG.KeySizeException;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;

/**
 * Library class that implements custom data structures to enhance querying for data that is "nearby"
 * other Geo-Tagged data in the set.
 */
public class QueryableSet {
    private final static int NEAREST_POINTS_AMOUNT = 5;
    private DataSet dataSet;

    public QueryableSet(DataSet set){
        this.dataSet = set;
    }

    /**
     * The system will return a appox value of the attribute from the nearest 5 points existing in the dataSet
     *
     *
     * @param x coordinate to interpolate
     * @param y coordinate to interpolate
     * @param t coordinate to interpolate
     * @param attribute to interpolate for
     * @return interpolated attribute value for this point.
     */
    public double querySet (int x, int y, int t, String attribute){
        AttributeGroup attributeGroup = dataSet.getAttributeGroup(attribute);
        KDTree kdTree = attributeGroup.getKdTree();
        List<DataPoint> dataPoints = new ArrayList<>();
        try{dataPoints = kdTree.nearest(new double[]{(double)x,(double)y,(double)t},NEAREST_POINTS_AMOUNT);}
        catch (KeySizeException e1){}

        double sum = 0;
        for(DataPoint dataPoint : dataPoints){
            sum += dataPoint.getAttribute(attribute);
        }

        return sum/(double)NEAREST_POINTS_AMOUNT;
    }

    /**
     * @return all attributes that the set currently contains
     */
    public Set<String> getAttributes(){
        return dataSet.getAttributes();
    }

    /**
     * @param attribute to query for
     * @return all DataPoints that contain a specific attribute
     */
    public List<DataPoint> getAttributeGroup(String attribute){
        return new ArrayList<>(dataSet.getAttributeGroup(attribute).getDataPoints());
    }

}
