package edu.cmu.cs.cs214.hw5.core;

import edu.wlu.cs.levy.CG.KDTree;

import java.util.ArrayList;
import java.util.Collection;

/**
 * AttributeGroup class that holds all DataPoints with a given specific attribute type. This
 * provides a specialised data structure upon construction that enables querying.
 *
 */
class AttributeGroup {
    private final String type;
    private ArrayList<DataPoint> dataPoints = new ArrayList<>();
    private KDTree<DataPoint> kdTree = new KDTree<>(3);
    /**
     * Initializes the AttributeGroup class with the String representation of type
     * @param type the type of the attribute
     */
    AttributeGroup(String type){
        this.type = type;
    }

    /**
     * Inserts a dataPoint and attempts to place it in the group's KD-Tree.
     *
     * @param point to add
     * @exception IllegalArgumentException in case the point is not compatible with the AttributeGroup
     */
    void addDataPoint(DataPoint point){
        if(checkCompatibility(point)){
            dataPoints.add(point);
            try{
                this.kdTree.insert(new double[]{point.getX(),point.getY(),point.getT()},point);
            }
            catch (Exception e1){
                e1.printStackTrace();
            }
        }
        else{
            throw new IllegalArgumentException("Point not compatible with AttrGroup type");
        }
    }

    /**
     * @return the complete KD-Tree of this AttributeGroup
     */
    KDTree<DataPoint> getKdTree(){return this.kdTree;}

    /**
     * Adds multiple, already-instantiated DataPoints into an attribute group and internal data structure
     * @param points collection of points to be added to the attribute group
     * @exception IllegalArgumentException in case the point is not compatible with the AttributeGroup
     */
    void addDataPoints(Collection<DataPoint> points){
        for(DataPoint point : points){
            if(!checkCompatibility(point)){
                throw new IllegalArgumentException("Point not compatible with AttrGroup type");
            }
            try{
                this.kdTree.insert(new double[]{point.getX(),point.getY(),point.getT()},point);
            }
            catch (Exception e1){
                e1.printStackTrace();
            }
        }
        dataPoints.addAll(points);
    }

    /**
     * Wrapper function for compatibility checking
     * @param point takes in a datapoint
     * @return boolean value indicating whether the input point is compatible
     */
    private boolean checkCompatibility(DataPoint point){
        return point.hasAttr(this.type);
    }

    /**
     * @return all the Data Points associated to the particular instance of AttributeGroup
     */
    ArrayList<DataPoint> getDataPoints(){
        return new ArrayList<>(dataPoints);
    }

    /**
     * @return how many points are in the AttributeGroup
     */
    int size(){
        return dataPoints.size();
    }
}
