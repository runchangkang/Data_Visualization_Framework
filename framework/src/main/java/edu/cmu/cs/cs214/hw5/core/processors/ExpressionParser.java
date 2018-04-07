package edu.cmu.cs.cs214.hw5.core.processors;

import java.util.ArrayList;
import java.util.List;

import org.mariuszgromada.math.mxparser.Argument;
import org.mariuszgromada.math.mxparser.Expression;

/**
 * Defines expression parsing using Mariusz Gromada's amazing mXparser library.
 * Takes appropriate expressions and inserts them into the framework's expression types.
 */
public class ExpressionParser {

    /**
     * Defines a new expression with a double return type and a single argument
     * @param expression string to parse
     * @return mathematical expression
     */
    private static Expression getExpression(String expression){
        List<Character> variables = new ArrayList<>();

        //Todo: this doesn't support some things the expression library does (e.g sin/cos type functions.)
        //Workaround 1: add recognised keyboard list
        //Workaround 2: ask client to define their specific variable
        for (Character c : expression.toCharArray()){
            if (Character.isLetter(c) && !variables.contains(c)){
                variables.add(c);
            }
        }

        if (variables.size() > 1){
            throw new IllegalArgumentException("Multivariable expressions not yet defined");
            /* Todo: this would require actually instantiating a new parser interface with the relevant
               attribute -> variable index and getting that information explicitly defined from the client.
             */
        }

        Argument x = new Argument("x",0);
        if (variables.size() > 0) {
            x = new Argument(variables.get(0).toString(), -1); //Instantiate a base argument (never called)
        }
        return new Expression(expression,x);
    }

    /**
     * Defines the mathematical expression as a double -> double transform and types it as such
     * @param expression to parse
     * @return transform represented by this expression
     */
    public static TransformExpression parseTransformExpression(String expression){
        return new TransformExpression(getExpression(expression));
    }

    /**
     * Defines the mathematical expression as a double -> boolean predicate and types it as such
     * @param expression to parse
     * @return filter represented by this expression
     */
    public static FilterExpression parseFilterExpression(String expression){
        return new FilterExpression(getExpression(expression));
    }

}
