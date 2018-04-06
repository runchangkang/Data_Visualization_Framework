package edu.cmu.cs.cs214.hw5.core;
import java.util.ArrayList;
import java.util.List;
import java.util.ServiceLoader;

public class PluginLoader {
    public static List<String> listPlugins() {

        List<String> pluginList = new ArrayList<>();

        for (DataPlugin p : ServiceLoader.load(DataPlugin.class)){
            pluginList.add(p.getName());
            System.out.println(p.getName());
        }

        return pluginList;
    }
}

