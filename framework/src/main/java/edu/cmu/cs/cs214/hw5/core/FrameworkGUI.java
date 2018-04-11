package edu.cmu.cs.cs214.hw5.core;

import javax.swing.SwingUtilities;

/**
 * Wrapper to dispatch the GUI
 */
class FrameworkGUI {

    /**
     * Invokes the GUI application on the EDT
     */
    FrameworkGUI(DataGraph graph) {
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
