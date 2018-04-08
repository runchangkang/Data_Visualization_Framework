package edu.cmu.cs.cs214.hw5.core.processors;

import edu.cmu.cs.cs214.hw5.core.AttributeGroup;
import edu.cmu.cs.cs214.hw5.core.DataPoint;
import edu.cmu.cs.cs214.hw5.core.DataSet;
import edu.cmu.cs.cs214.hw5.core.GeoDataSet;

import java.util.ArrayList;
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
        List<DataPoint> newList = new ArrayList<>();
        StringBuilder names = new StringBuilder();

        for (DataSet set : sources){
            newList.addAll(set.getDataPoints());
            names.append(set.getName());
            names.append(" & ");
        }
        names.deleteCharAt(names.length()-2);
        names.deleteCharAt(names.length()-1);

        return new GeoDataSet(newList,names.toString());
    }
}
