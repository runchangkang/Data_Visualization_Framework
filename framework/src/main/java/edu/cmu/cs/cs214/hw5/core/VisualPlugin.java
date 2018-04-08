package edu.cmu.cs.cs214.hw5.core;

import javax.swing.*;


/**
 * The Visual Plugins should not be dependent on the presence of any specific field name or attribute in a dataSet.
 * They should be arbitrarily applicable - namely, they'll take some parameters and draw whatever they find,
 * and label it accordingly. It is on the application user (not the client or the framework) to apply visual plugins
 * that make sense in the context of their Data Set.
 */
public interface VisualPlugin {

    /**
     * @return the plugin's label on the selection screen
     */
    String getName();

    //todo: integrate these with the GUI
    /**
     * @return a list of parameters that contain useful information regarding the data visualisation
     */
    ParameterList addInterfaceParameters();

    /**
     * Draw the JPanel. This gives the client flexibility to use whatever library they desire
     * @param qSet set queryable for the data (see QueryableSet API)
     * @param x dimension that JPanel will be sized to
     * @param y dimension that JPanel will be sized to
     * @return fully drawn JPanel
     */
    JPanel drawSet(QueryableSet qSet, int x, int y);
}
