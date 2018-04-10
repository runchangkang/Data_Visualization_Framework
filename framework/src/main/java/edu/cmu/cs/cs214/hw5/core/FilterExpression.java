package edu.cmu.cs.cs214.hw5.core;

import org.mariuszgromada.math.mxparser.Expression;

/**
 * Implements double -> boolean filtering
 */
class FilterExpression {

    private Expression filterFunc;

    /**
     * @param filterFunc function to filter with
     */
    FilterExpression(Expression filterFunc){
        if (filterFunc.getArgumentsNumber() > 1){
            throw new IllegalArgumentException("Multivariable functions not yet supported.");
        }
        this.filterFunc = filterFunc;
    }

    /**
     * @param x argument
     * @return value of filter's application to argument (0.0 == false)
     */
    boolean apply(double x){
        filterFunc.getArgument(0).setArgumentValue(x);
        return !almostZero(filterFunc.calculate());
    }

    /**
     * Double comparison method
     */
    private static boolean almostZero(double a){
        return Math.abs(a) < 1e-9;
    }
}
