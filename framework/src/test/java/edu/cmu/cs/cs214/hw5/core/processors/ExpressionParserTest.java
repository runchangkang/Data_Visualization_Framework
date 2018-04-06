package edu.cmu.cs.cs214.hw5.core.processors;

import org.junit.Assert;
import org.junit.Test;

/**
 * Tests that basic filter and transform expressions are being created and applied correctly
 */
public class ExpressionParserTest {

    @Test
    public void transformTest(){
        Assert.assertEquals(ExpressionParser.parseTransformExpression("2*x^2 + 2*x + 2").apply(2.0),14.0,1e-9);
        Assert.assertEquals(ExpressionParser.parseTransformExpression("3 * (x + 2*x)").apply(2.0),18.0,1e-9);
        Assert.assertEquals(ExpressionParser.parseTransformExpression("-x").apply(1.0),-1.0,1e-9);
    }

    @Test
    public void filterTest(){
        Assert.assertTrue(ExpressionParser.parseFilterExpression("x == 2").apply(2.0));
        Assert.assertTrue(ExpressionParser.parseFilterExpression("x < 3.0").apply(2.0));
        Assert.assertFalse(ExpressionParser.parseFilterExpression("x > 3.0").apply(2.0));
    }
}