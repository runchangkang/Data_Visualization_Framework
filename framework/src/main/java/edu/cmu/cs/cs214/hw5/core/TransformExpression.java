package edu.cmu.cs.cs214.hw5.core;

import org.mariuszgromada.math.mxparser.Expression;

/**
 * Implements double -> double transformations
 */
class TransformExpression {

    private Expression transformFunc;

    /**
     * @param transformFunc function to perform transform with
     */
    TransformExpression(Expression transformFunc){
        if (transformFunc.getArgumentsNumber() > 1){
            throw new IllegalArgumentException("Multivariable functions not yet supported.");
        }
        this.transformFunc = transformFunc;
    }

    /**
     * @param x argument
     * @return transformation applied to argument
     */
    double apply(double x){
        transformFunc.getArgument(0).setArgumentValue(x);
        return transformFunc.calculate();
    }
}
