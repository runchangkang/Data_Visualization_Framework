package edu.cmu.cs.cs214.hw5.core.processors;

import java.util.function.DoublePredicate;

/**
 * Implements double -> boolean filtering
 */
public class FilterExpression {

    private DoublePredicate filterFunc;

    /**
     * @param filterFunc function to filter with
     */
    FilterExpression(DoublePredicate filterFunc){
        this.filterFunc = filterFunc;
    }

    /**
     * @param x argument
     * @return value of filter's application to argument
     */
    boolean apply(double x){
        return filterFunc.test(x);
    }
}
