package edu.cmu.cs.cs214.hw5.gui;

import javax.swing.SwingUtilities;

/**
 * Wrapper to dispatch the GUI
 */
public class FrameworkGUI {

    /**
     * Invokes the GUI application on the EDT
     */
    public FrameworkGUI() {
        SwingUtilities.invokeLater(() -> createAndShowSetupScreen());
    }

    /**
     * Creates a new GUI controller and launches it
     */
    private void createAndShowSetupScreen() {
        ControlPanel graph = new ControlPanel();
        graph.launch();
    }

}
