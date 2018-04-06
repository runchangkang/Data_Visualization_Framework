package edu.cmu.cs.cs214.hw5.core.processors;

import edu.cmu.cs.cs214.hw5.core.AttributeGroup;
import edu.cmu.cs.cs214.hw5.core.DataSet;
import edu.cmu.cs.cs214.hw5.core.GeoDataSet;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Joins two (or more) DataSets together into one
 */
public class Join implements Processor {

    /**
     * Applies a standard join operation. In the case that two sets both contain the same attribute,
     * a join operation will concatenate their respective attribute groups.
     *
     * @param sources DataSets to join together
     * @return single joined DataSet
     */
    @Override
    public DataSet apply(List<DataSet> sources) {
        Map<String, AttributeGroup > newSet = new HashMap<>();

        for (DataSet set : sources){
            for (String attr : set.getAttributes()){
                if (newSet.containsKey(attr)){
                    newSet.get(attr).addDataPoints(set.getAttributeGroup(attr).getDataPoints());
                }
                else{
                    newSet.put(attr,set.getAttributeGroup(attr));
                }
            }
        }

        return new GeoDataSet(newSet);
    }
}
