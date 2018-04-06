package edu.cmu.cs.cs214.hw5.core.processors;

import java.util.function.DoubleFunction;

/**
 * Implements double -> double transformations
 */
public class TransformExpression {

    private DoubleFunction<Double> transformFunc;

    /**
     * @param transformFunc function to perform transform with
     */
    TransformExpression(DoubleFunction<Double> transformFunc){
        this.transformFunc = transformFunc;
    }

    /**
     * @param x argument
     * @return transformation applied to argument
     */
    public double apply(double x){
        return transformFunc.apply(x);
    }
}
