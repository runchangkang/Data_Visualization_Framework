package edu.cmu.cs.cs214.hw5.core;

import edu.wlu.cs.levy.CG.KDTree;
import edu.wlu.cs.levy.CG.KeySizeException;

import java.util.*;

/**
 * Library class for visualisation plugins that implements custom data structures to enhance querying for data
 * that is "nearby" other Geo-Tagged data. This is meant for visualisation plugins to gain additional information
 * about the source data and implement a flexible range of plug-ins.
 */
public class QueryableSet {
    private final static int NEAREST_POINTS_AMOUNT = 5;
    private final static int P_VALUE = 5;
    private DataSet dataSet;
    private Map<String,KDTree<DataPoint>> kdTrees = new HashMap<>();

    /**
     * Constructs a QueryableSet and initialises the kd structure.
     * @param set
     */
    QueryableSet(DataSet set){
        this.dataSet = set;
        for (String attribute : set.getAttributes()){
            AttributeGroup attributeGroup = dataSet.getAttributeGroup(attribute);
            KDTree<DataPoint> kdTree = attributeGroup.getKdTree();
            kdTrees.put(attribute,kdTree);
        }
    }

    /**
     * Method to query the dataset for a point. Given a spatial (geo-tag) location and an attribute to look for,
     * this method returns it's best guess (given the source data) at what the attribute value would be at this
     * geographical point, regardless of the presence of this point in the actual set. This is useful when trying to
     * generate a uniform map-like visualisation from a source set that contains only random or sparse data.
     *
     * @param x location coordinate to interpolate at
     * @param y location coordinate to interpolate at
     * @param t location coordinate to interpolate at
     * @param attribute to get the value of
     * @return interpolated attribute value for this point.
     */
    public double querySet (double x, double y, double t, String attribute){
        KDTree<DataPoint> queryTree = kdTrees.get(attribute);
        List<DataPoint> dataPoints = new ArrayList<>();
        try{
            double[] queryPt = {x,y,t};
            dataPoints = queryTree.nearest(queryPt,NEAREST_POINTS_AMOUNT);
        }
        catch (KeySizeException e1){
            e1.printStackTrace();
        }

        double[] distances = new double[dataPoints.size()];

        int i = 0;
        for(DataPoint point : dataPoints){
            double dist = getDistance(point,x,y,t);
            if (Math.abs(dist) < 0.000001){ //we've basically nailed it
                return point.getAttribute(attribute);
            }
            distances[i] = dist;
            i++;
        }

        double total1 = 0;
        double total2 = 0;
        int j = 0;
        //Inverse distance weighting (Shepard) : https://en.wikipedia.org/wiki/Inverse_distance_weighting
        for (DataPoint point : dataPoints){
            double attr = point.getAttribute(attribute);
            double metric = Math.pow(distances[j],P_VALUE);
            total1 += attr / metric;
            total2 += 1 / metric;
            j++;
        }
        return (total1/total2);
    }


    private double getDistance(DataPoint p, double x, double y, double t){
        double xDist = x - p.getX();
        double yDist = y - p.getY();
        double tDist = x - p.getT();
        return  (xDist * xDist) + (yDist*yDist) + (tDist*tDist);
    }

    /**
     * Fetches all of the attributes currently contained within any of the points in the set. Not all points
     * are guaranteed to have all attributes, but at least one point will have each attribute.
     *
     * @return set of attribute names
     */
    public Set<String> getAttributes(){
        return dataSet.getAttributes();
    }

    /**
     * Fetches all of the Data Points that have a specific attribute from the set. This enables the visualisation
     * to tailor to only the data that exists (which is necessary while implementing certain visualisations such
     * as bar graphs and histograms). Each DataPoint contains an interface to extract point-specific information.
     *
     * @param attribute to query for
     * @return all DataPoints that have that specific attribute
     */
    public List<DataPoint> getAttributeGroup(String attribute){
        return new ArrayList<>(dataSet.getAttributeGroup(attribute).getDataPoints());
    }

}
