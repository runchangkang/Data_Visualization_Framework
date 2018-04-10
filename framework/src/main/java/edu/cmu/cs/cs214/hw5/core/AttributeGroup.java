package edu.cmu.cs.cs214.hw5.core;

import edu.wlu.cs.levy.CG.KDTree;
import edu.wlu.cs.levy.CG.KeyDuplicateException;
import edu.wlu.cs.levy.CG.KeySizeException;

import java.util.ArrayList;
import java.util.Collection;
/**
 * AttributeGroup class that holds all datapoints with the
 * given specific attribute type.
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
     * @param point to add
     * @exception IllegalArgumentException in case the point is not compatible with the attrgroup
     */
    void addDataPoint(DataPoint point){
        if(checkCompatibility(point)){
            dataPoints.add(point);
            try{
                this.kdTree.insert(new double[]{point.getX(),point.getY(),point.getT()},point);
            }
            catch (KeySizeException e1){}
            catch (KeyDuplicateException e2){};
        }
        else{
            throw new IllegalArgumentException("Point not compatible with AttrGroup type");
        }
    }

    /**
     * kdTree Getter function
     * @return
     */
    public KDTree getKdTree(){return this.kdTree;}
    /**
     * Adds multiple, already-instantiated DataPoints into an attribute group
     * @param points collection of points to be added to the attribute group
     * @exception IllegalArgumentException in case the point is not compatible with the attrgroup
     */
    void addDataPoints(Collection<DataPoint> points){
        for(DataPoint point : points){
            if(!checkCompatibility(point)){
                throw new IllegalArgumentException("Point not compatible with AttrGroup type");
            }
            try{
                this.kdTree.insert(new double[]{point.getX(),point.getY(),point.getT()},point);
            }
            catch (KeySizeException e1){}
            catch (KeyDuplicateException e2){};
        }
        dataPoints.addAll(points);

    }

    /**
     *
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
