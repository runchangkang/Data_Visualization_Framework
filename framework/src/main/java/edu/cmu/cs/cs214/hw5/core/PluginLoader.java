package edu.cmu.cs.cs214.hw5.core;
import java.util.ServiceLoader;

public class PluginLoader {
    public static void listPlugins() {
        /*java.lang.Thread.currentThread().setContextClassLoader(
                java.lang.ClassLoader.getSystemClassLoader()
        );*/

        for (DataPlugin p : ServiceLoader.load(DataPlugin.class)){
            System.out.println(p.getName());
        }
    }
}

