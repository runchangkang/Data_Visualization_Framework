package edu.cmu.cs.cs214.hw5.core.processors;

import edu.cmu.cs.cs214.hw5.core.AttributeGroup;
import edu.cmu.cs.cs214.hw5.core.DataPoint;
import edu.cmu.cs.cs214.hw5.core.DataSet;
import edu.cmu.cs.cs214.hw5.core.GeoDataSet;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Applies a numerical processing to one DataSet based off of a set of client-defined transformations for each attribute.
 */
public class Transform implements Processor{
    private Map<String,TransformExpression> transforms;

    /**
     * Instantiate a new transform with a set of client-defined attribute transform expressions
     * @param transforms
     */
    Transform (Map<String,TransformExpression> transforms){
        this.transforms = new HashMap<>(transforms);
    }

    /**
     * Apply this transform to a given DataSet. This will only apply the transform to the first DataSet in the
     * specified input list. Attributes in the transform set that the DataSet does not contain are not affected.
     * Attributes in the DataSet that are not transformed are also unaffected.
     *
     * @param sources DataSet to apply transformation
     * @return newly transformed DataSet
     */
    @Override
    public DataSet apply(List<DataSet> sources) {
        DataSet set = sources.get(0);                   //Transforms only apply to one source
        Map<String, AttributeGroup> newSet = new HashMap<>();

        for (String attr : set.getAttributes())
            if (transforms.containsKey(attr)) {         //Transform the data the client has specified
                TransformExpression transform = transforms.get(attr);
                AttributeGroup groupToTransform = set.getAttributeGroup(attr);
                AttributeGroup newGroup = new AttributeGroup(attr);

                for (DataPoint point : groupToTransform.getDataPoints()) {
                    newGroup.addDataPoint(point.getX(), point.getY(), point.getT(), transform.apply(point.getAttr()));
                }

                newSet.put(attr, newGroup);
            } else {                                    //Don't affect or remove the untransformed data in the set
                newSet.put(attr, set.getAttributeGroup(attr));
            }

        return new GeoDataSet(newSet);
    }
}
