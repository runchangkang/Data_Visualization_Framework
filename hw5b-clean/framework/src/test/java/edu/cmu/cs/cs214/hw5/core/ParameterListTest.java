package edu.cmu.cs.cs214.hw5.core;

import org.junit.Before;
import org.junit.Test;

import java.util.ArrayList;

import static org.junit.Assert.assertEquals;

public class ParameterListTest {
    private ParameterList pList;

    @Before
    public void setup(){
        pList = new ParameterList();
    }

    @Test
    public void addParameter(){
        pList.addParameter("p1", 1.0, 100.0);
        pList.addParameter("p2", 2.0, 10.0);
        ArrayList<Parameter> pArray = (ArrayList) pList.getParameters();
        assertEquals(2, pArray.size());
        Parameter p1 = null;
        Parameter p2 = null;
        for(Parameter p : pArray){
            if(p.getName() == "p1"){
                p1 = p;
            }
            else{
                p2 = p;
            }
        }
        assertEquals(100.0, p1.getMax(), 0.01);
        assertEquals(1.0, p1.getMin(), 0.01);
        assertEquals(10.0, p2.getMax(), 0.01);
        assertEquals(2.0, p2.getMin(), 0.01);
    }

}
