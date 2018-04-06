package edu.cmu.cs.cs214.hw5.core.processors;

import edu.cmu.cs.cs214.hw5.core.AttributeGroup;
import edu.cmu.cs.cs214.hw5.core.DataPoint;
import edu.cmu.cs.cs214.hw5.core.DataSet;
import edu.cmu.cs.cs214.hw5.core.GeoDataSet;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Applies a boolean filtering to one DataSet based off of a set of client-defined filters for each attribute.
 */
public class Filter implements Processor {

    private Map<String,FilterExpression> filters;

    /**
     * Instantiate a new filter with a set of client-defined attribute filter expressions
     * @param filters
     */
    Filter (Map<String,FilterExpression> filters){
        this.filters = new HashMap<>(filters);
    }

    /**
     * Apply this filter to a given DataSet. This will only apply the filter to the first DataSet in the
     * specified input list. Attributes in the filter set that the DataSet does not contain are not affected.
     * Attributes in the DataSet that are not filtered are also unaffected.
     *
     * @param sources DataSet to apply filtering
     * @return newly filtered DataSet
     */
    @Override
    public DataSet apply(List<DataSet> sources) {
        DataSet set = sources.get(0);                   //Filters only apply to one source
        Map<String, AttributeGroup> newSet = new HashMap<>();

        for (String attr : set.getAttributes())
            if (filters.containsKey(attr)) {            //Filter the data the client has specified
                FilterExpression filter = filters.get(attr);
                AttributeGroup groupToTransform = set.getAttributeGroup(attr);
                AttributeGroup newGroup = new AttributeGroup(attr);

                for (DataPoint point : groupToTransform.getDataPoints()) {
                    if (filter.apply(point.getAttr())){
                        newGroup.addDataPoint(point.getX(), point.getY(), point.getT(), point.getAttr());
                    }
                }

                if(newGroup.size() > 0) {
                    newSet.put(attr, newGroup);
                }
            } else {                                    //Don't affect or remove the unfiltered attributes in the set
                newSet.put(attr, set.getAttributeGroup(attr));
            }

        return new GeoDataSet(newSet);
    }
}
