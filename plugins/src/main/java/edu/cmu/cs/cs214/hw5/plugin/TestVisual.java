package edu.cmu.cs.cs214.hw5.plugin;

import edu.cmu.cs.cs214.hw5.core.ParameterList;
import edu.cmu.cs.cs214.hw5.core.QueryableSet;
import edu.cmu.cs.cs214.hw5.core.VisualPlugin;

import javax.swing.*;

/**
 * Dummy visual plugin impl. to test loading
 */
public class TestVisual implements VisualPlugin{
    @Override
    public String getName() {
        return "TestVisual Plugin";
    }

    @Override
    public ParameterList addInterfaceParameters() {
        return null;
    }

    @Override
    public JPanel drawSet(QueryableSet qSet, int x, int y) {
        return null;
    }
}
