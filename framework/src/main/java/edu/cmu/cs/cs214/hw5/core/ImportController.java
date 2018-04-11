package edu.cmu.cs.cs214.hw5.core;

import javax.swing.JButton;
import javax.swing.JDialog;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import java.awt.GridLayout;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Controls importing DataSets with Data Plugins
 */
class ImportController {

    private String selectedDataPlugin;
    private DataGraph graph;
    private ControlPanel cp;

    private static final String NAME = "Name";

    /**
     * Defines a new import controller
     * @param cp controlPanel instance to work with
     * @param graph datagraph to import to
     */
    ImportController(ControlPanel cp, DataGraph graph){
        this.cp = cp;
        this.graph = graph;
    }

    /**
     * Pops up a dialog with the data plugin selection interface
     * @param dataPluginList list of data plugins to select from
     * @param frame to pop above
     */
    void dataSetDialog(List<String> dataPluginList, JFrame frame){
        final JDialog dialog = new JDialog(frame, "Select a DataSet Plugin", true);
        selectedDataPlugin = null; //reset

        JPanel optionPanel = new JPanel(new GridLayout(0,1));
        optionPanel.add(new JLabel("  Select a DataSet Plugin from the loaded list.  "));

        for (String pluginName : dataPluginList){
            JButton button = new JButton(pluginName);
            button.addActionListener(e -> {
                this.selectedDataPlugin = pluginName;
                //System.out.println(this.selectedDataPlugin);
            });
            optionPanel.add(button);
        }

        JButton closeButton = new JButton("NEXT");
        closeButton.addActionListener(e -> {
            if(selectedDataPlugin != null) {
                dialog.setVisible(false);
                dataPluginDialog(selectedDataPlugin,frame);
            }
            else{
                JOptionPane.showMessageDialog(dialog, "Please select an import plugin.");
            }
        });
        optionPanel.add(closeButton);

        ControlPanel.display(dialog,optionPanel,frame);
    }

    /**
     * Pops up a dialog with the parameters requested by a particular data plugin
     * @param selectedDataPlugin (Name of) plugin
     * @param frame to pop above
     */
    private void dataPluginDialog(String selectedDataPlugin, JFrame frame){
        DataPlugin dp = PluginLoader.getDataPlugin(selectedDataPlugin);
        if (dp == null) return;

        List<String> options = new ArrayList<>(Collections.singletonList(NAME));
        options.addAll(dp.getPopupParameters());

        final JDialog dialog = new JDialog(frame, "Select a DataSet Plugin", true);
        JPanel optionPanel = new JPanel(new GridLayout(0,1));

        Map<String,String> argMap = new HashMap<>();
        optionPanel.add(ControlPanel.paramFieldSet(options,argMap,null));

        JPanel wrapper = new JPanel();
        JButton closeButton = new JButton("CREATE");
        closeButton.addActionListener(e -> {
            if(verifyMap(argMap,options)){
                try {
                    Collection<ClientPoint> dSet = dp.getCollection(argMap);
                    graph.addClientSet(dSet,argMap.get(NAME));
                    dialog.setVisible(false);
                    cp.addStartScreen();
                }
                catch (Exception e2){
                    e2.printStackTrace();
                    JOptionPane.showMessageDialog(dialog, "An error occurred while processing data. Please try again.");
                }
            }
            else{
                JOptionPane.showMessageDialog(dialog, "All fields must be filled for import.");
            }
        });
        wrapper.add(closeButton);
        optionPanel.add(wrapper);

        ControlPanel.display(dialog,optionPanel,frame);
    }

    /**
     * Checks that an argument map at least contains something. Completion, not correctness.
     * @param argMap map to check
     * @param options required contents
     * @return correctly filled map?
     */
    private static boolean verifyMap(Map<String,String> argMap, List<String> options){
        for (String option : options){
            if(!argMap.containsKey(option)){
                return false;
            }
            if ("".equals(argMap.get(option).replaceAll("\\s",""))){
                return false;
            }
        }
        return true;
    }

}
