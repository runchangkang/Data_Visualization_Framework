package edu.cmu.cs.cs214.hw5.core;

import edu.cmu.cs.cs214.hw5.gui.FrameworkGUI;

import java.util.Collection;
import java.util.HashMap;
import java.util.Map;

/**
 * Instantiates a new GUI.
 */
public class Main {

    private static final String FILE_PATH_LABEL = "File Path";
    private static final String X_LABEL = "X";
    private static final String Y_LABEL = "Y";
    private static final String T_LABEL = "T";
    private static final String ATTRIBUTE_LABEL = "Attributes";

    public static void main(String[] args) {
        //testResourceSet();
        new FrameworkGUI(new DataGraph());
    }

    public static void testResourceSet(){
        DataPlugin CSV = PluginLoader.getDataPlugin("CSVReader");
        DataGraph dg = new DataGraph();
        Map<String,String> argMap = new HashMap<>();
        argMap.put(FILE_PATH_LABEL,"resources/test.csv");
        argMap.put(X_LABEL,"X");
        argMap.put(Y_LABEL,"Y");
        argMap.put(T_LABEL,"Time");
        argMap.put(ATTRIBUTE_LABEL,"Wind,Light");
        try {
            Collection<ClientPoint> cx = CSV.getCollection(argMap);
            dg.addDataSet(cx);
            new FrameworkGUI(dg);
        }
        catch (Exception e){
            e.printStackTrace();
            System.out.println("Error");
        }
    }
}

