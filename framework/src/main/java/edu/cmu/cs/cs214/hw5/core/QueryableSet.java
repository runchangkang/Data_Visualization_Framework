package edu.cmu.cs.cs214.hw5.core;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;

public class QueryableSet {

    private DataSet dataSet;

    QueryableSet(DataSet set){
        this.dataSet = set;
    }

    /**
     * Dummy method for interpolations.
     * Todo: Set up K-D Tree for attributeGroup to enable fast NN query and interpolating
     *
     * @param x coordinate to interpolate
     * @param y coordinate to interpolate
     * @param t coordinate to interpolate
     * @param attribute to interpolate for
     * @return interpolated attribute value for this point.
     */
    public double querySet (int x, int y, int t, String attribute){
        return dataSet.getAttributeGroup(attribute).getDataPoints().get(0).getAttr();
    }

    public Set<String> getAttributes(){
        return dataSet.getAttributes();
    }

    public List<DataPoint> getAttributeGroup(String attribute){
        return new ArrayList<>(dataSet.getAttributeGroup(attribute).getDataPoints());
    }

}
