package edu.cmu.cs.cs214.hw5.core;

import javax.swing.*;
import java.util.Map;

public class MockVisualPlugin implements VisualPlugin{
    /**
     * The name this plugin will be displayed with in the framework for purposes of selection and loading.
     *
     * @return name string
     */
    @Override
    public String getName() {
        return "Test Visual";
    }

    /**
     * The visualisation plugin can query the framework to provide a variety of parameters to tune the graphical
     * result of the visualisation -- for example adjusting stroke weights, colors, range of data displayed.
     * These will be provided as sliders to the user and expect a minimum/maximum value and a label (see: ParameterList)
     * Results will be returned to the client in the drawSet function.
     *
     * @return a list of parameters that contain useful information regarding the data visualisation
     */
    @Override
    public ParameterList addInterfaceParameters() {
        return new ParameterList();
    }

    /**
     * The client must return a drawn JPanel, likely by implementing PaintComponent. This adds the flexibility to add
     * Swing components to the visualisation as well as to define and utilise external libraries as desired. The client
     * is given a QueryableSet which can be asked for 'interpolated' values -- points that represent a 'best guess' at
     * an attribute for a certain location, even when the data source doesn't necessarily contain that data point.
     * Arguments are provided from the parameters in the form of a mapping String (label) to Double (value).
     *
     * @param qSet    set queryable for the data (see QueryableSet API)
     * @param x       dimension that JPanel will be sized to in the framework display
     * @param y       dimension that JPanel will be sized to in the framework display
     * @param results argument maps from the interface parameters. Client handles naming consistency.
     * @return fully drawn JPanel.
     */
    @Override
    public JPanel drawSet(QueryableSet qSet, int x, int y, Map<String, Double> results) {
        return new JPanel();
    }
}
