package edu.cmu.cs.cs214.hw5.core.processors;

import edu.cmu.cs.cs214.hw5.core.DataPoint;
import edu.cmu.cs.cs214.hw5.core.DataSet;
import edu.cmu.cs.cs214.hw5.core.GeoDataSet;

import java.util.ArrayList;
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
    public Transform (Map<String,String> transforms){
        Map<String,TransformExpression> parsedMap = new HashMap<>();

        for (String key: transforms.keySet()){
            if (!transforms.get(key).equals("") && !transforms.get(key).equals(" ")) {
                TransformExpression exp = ExpressionParser.parseTransformExpression(transforms.get(key));
                if (Double.isNaN(exp.apply(1))) {
                    throw new IllegalArgumentException("Invalid expression produced NaN!");
                }
                parsedMap.put(key, exp);
            }
        }

        this.transforms = new HashMap<>(parsedMap);
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

        List<DataPoint> newList = new ArrayList<>();

        for (DataPoint point : set.getDataPoints()) {

            Map<String,Double> attrMap = new HashMap<>();
            for (String ptAttribute : point.getAttributes()){
                double value = point.getAttribute(ptAttribute);
                if (transforms.containsKey(ptAttribute)){
                    value = transforms.get(ptAttribute).apply(value);
                }
                attrMap.put(ptAttribute,value);
            }
            newList.add(new DataPoint(point.getX(),point.getY(),point.getT(),attrMap));
        }

        return new GeoDataSet(newList,"t"+incrementNumber(set.getName()));
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
