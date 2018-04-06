package edu.cmu.cs.cs214.hw5.core;
import java.util.ArrayList;
import java.util.List;
import java.util.ServiceLoader;

/**
 * Dynamically loads plugins using Java's built-in ServiceLoader.
 *
 * Requires:
 *      gradle run (from top level directory)
 *      relevant plugin names are in plugins/src/main/resources/META-INF/services/edu.cmu.cs.cs214.hw5.core.DataPlugin
 *                               OR  plugins/src/main/resources/META-INF/services/edu.cmu.cs.cs214.hw5.core.VisualPlugin
 *      depending on which plugin type is being implemented.
 *      See the existing dummy files for how to add more.
 */
public class PluginLoader {

    /**
     * @return List/Prints of all the currently loaded DataPlugin implementations
     */
    public static List<String> listDataPlugins() {

        List<String> pluginList = new ArrayList<>();

        for (DataPlugin p : ServiceLoader.load(DataPlugin.class)){
            pluginList.add(p.getName());
            System.out.println(p.getName());
        }

        return pluginList;
    }

    /**
     * @return List/Prints of all the currently loaded VisualPlugin implementations
     */
    public static List<String> listVisualPlugins() {

        List<String> pluginList = new ArrayList<>();

        for (VisualPlugin p : ServiceLoader.load(VisualPlugin.class)){
            pluginList.add(p.getName());
            System.out.println(p.getName());
        }

        return pluginList;
    }
}

