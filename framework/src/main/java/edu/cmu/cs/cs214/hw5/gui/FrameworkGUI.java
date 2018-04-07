package edu.cmu.cs.cs214.hw5.gui;

import edu.cmu.cs.cs214.hw5.core.DataGraph;

import javax.swing.SwingUtilities;

/**
 * Wrapper to dispatch the GUI
 */
public class FrameworkGUI {

    /**
     * Invokes the GUI application on the EDT
     */
    public FrameworkGUI(DataGraph graph) {
        SwingUtilities.invokeLater(() -> createAndShowSetupScreen(graph));
    }

    /**
     * Creates a new GUI controller and launches it
     */
    private void createAndShowSetupScreen(DataGraph graph) {
        ControlPanel controller = new ControlPanel();
        controller.launch(graph);
    }

}
