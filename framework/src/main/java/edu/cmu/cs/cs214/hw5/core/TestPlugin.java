package edu.cmu.cs.cs214.hw5.core;

import java.util.Collection;
import java.util.List;
import java.util.Map;

/**
 * Dummy plugin implementation to test Loader
 */
public class TestPlugin implements DataPlugin{
    @Override
    public String getName() {
        return "Hello from TestPlugin!";
    }


    @Override
    public List<String> getPopupParameters() {
        return null;
    }

    @Override
    public Collection<ClientPoint> getCollection(Map<String,String> argumentMap) throws Exception{
        return null;
    }
}
