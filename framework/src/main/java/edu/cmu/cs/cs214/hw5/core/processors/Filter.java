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
 * Applies a boolean filtering to one DataSet based off of a set of client-defined filters for each attribute.
 */
public class Filter implements Processor {

    private Map<String,FilterExpression> filters;

    /**
     * Instantiate a new filter with a set of client-defined attribute filter expressions
     * @param filters argument map
     */
    public Filter (Map<String,String> filters){
        Map<String,FilterExpression> parsedMap = new HashMap<>();

        for (String key: filters.keySet()){
            if (!filters.get(key).equals("") && !filters.get(key).equals(" ")) {
                FilterExpression exp = ExpressionParser.parseFilterExpression(filters.get(key));
                parsedMap.put(key, exp);
            }
        }

        this.filters = parsedMap;
    }

    /**
     * Apply this filter to a given DataSet. This will only apply the filter to the first DataSet in the
     * specified input list. Attributes in the filter set that the DataSet does not contain are not affected.
     * Attributes in the DataSet that are not filtered are also unaffected.
     *
     * @param sources DataSet to apply filtering
     * @return newly filtered DataSet
     */

    //apply all filters to all attributeSets


    @Override
    public DataSet apply(List<DataSet> sources) {
        DataSet set = sources.get(0);                   //Filters only apply to one source

        List<DataPoint> newList = new ArrayList<>();

        for (DataPoint point : set.getDataPoints()) {
            boolean keep = true;
            for (String key : filters.keySet()) {
                if (point.hasAttr(key)) {
                    if (!filters.get(key).apply(point.getAttribute(key))) {
                        keep = false;
                        break;
                    }
                }
            }
            if (keep) {
                newList.add(point);
            }
        }

        return new GeoDataSet(newList,"f" + incrementNumber(set.getName()));
    }

    private static String incrementNumber(String s){
        char[] arr = s.toCharArray();
        if (Character.isDigit(arr[arr.length -1])){
            int x = Integer.parseInt(String.valueOf(arr[arr.length -1]));
            x++;
            String result = String.valueOf(arr,0,arr.length-1);
            return result + x;
        }
        return s + "1";
    }
}
