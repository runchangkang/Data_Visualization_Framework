package edu.cmu.cs.cs214.hw5.core;

import org.junit.Before;
import org.junit.Test;

import java.util.HashMap;
import java.util.Map;

public class DataGraphTest {
    private DataGraph dg;

    @Before
    public void setup() throws Exception {
        dg = new DataGraph();
        MockDataPlugin mock = new MockDataPlugin();
        Map<String, String> map = new HashMap<>();
        dg.addClientSet(mock.getCollection(map), mock.getName());



    }

    @Test
    public void

}
