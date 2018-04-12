package edu.cmu.cs.cs214.hw5.core;

import java.util.*;

public class MockDataPlugin implements DataPlugin{
    /**
     * @return name of this data plugin in the selection screen
     */
    @Override
    public String getName() {
        return "TEST";
    }

    /**
     * @return list of field labels that the framework will prompt the client to provide
     */
    @Override
    public List<String> getPopupParameters() {
        return new ArrayList<>(Arrays.asList("Test Param"));
    }

    /**
     * @param argumentMap map from field parameters to client-provided arguments
     * @return collection of Points ready to be parsed into a DataSet
     * @throws Exception if there was an I/O or parsing error (FileNotFound, I/O Exception, ParseException)
     */
    @Override
    public Collection<ClientPoint> getCollection(Map<String, String> argumentMap) throws Exception {
        ArrayList<ClientPoint> cpList = new ArrayList<>();
        Map<String, Double> attrs1 = new HashMap<>();
        attrs1.put("attr1", 10.0);
        attrs1.put("attr2", 11.0);
        attrs1.put("attr3", 12.0);
        Map<String, Double> attrs2 = new HashMap<>(attrs1);
        ClientPoint cp1 = new ClientPoint(1.001,2.001,3.001, attrs1,"CP1");
        ClientPoint cp2 = new ClientPoint(1.001,2.001,3.001, attrs2,"CP2");
        cpList.add(cp1);
        cpList.add(cp2);
        return cpList;
    }
}
