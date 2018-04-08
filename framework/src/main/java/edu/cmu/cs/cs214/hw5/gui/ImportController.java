package edu.cmu.cs.cs214.hw5.gui;

import edu.cmu.cs.cs214.hw5.core.ClientPoint;
import edu.cmu.cs.cs214.hw5.core.DataGraph;
import edu.cmu.cs.cs214.hw5.core.DataPlugin;
import edu.cmu.cs.cs214.hw5.core.PluginLoader;

import javax.swing.*;
import java.awt.*;
import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class ImportController {

    private String selectedDataPlugin;
    private DataGraph graph;
    private ControlPanel cp;

    ImportController(ControlPanel cp, DataGraph graph){
        this.cp = cp;
        this.graph = graph;
    }

    void dataSetDialog(List<String> dataPluginList, JFrame frame){
        final JDialog dialog = new JDialog(frame, "Select a DataSet Plugin", true);
        selectedDataPlugin = null; //reset

        JPanel optionPanel = new JPanel(new GridLayout(0,1));
        optionPanel.add(new JLabel("  Select a DataSet Plugin from the loaded list.  "));

        for (String pluginName : dataPluginList){
            JButton button = new JButton(pluginName);
            button.addActionListener(e -> {
                this.selectedDataPlugin = pluginName;
                System.out.println(this.selectedDataPlugin);});
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

    private void dataPluginDialog(String selectedDataPlugin, JFrame frame){
        DataPlugin dp = PluginLoader.getDataPlugin(selectedDataPlugin);
        List<String> options = dp.getPopupParameters();
        final JDialog dialog = new JDialog(frame, "Select a DataSet Plugin", true);

        JPanel optionPanel = new JPanel(new GridLayout(0,1));

        //todo: have argmap add in a name option for the client to name their dataset
        Map<String,String> argMap = new HashMap<>();

        ControlPanel.paramFieldSet(options,argMap,optionPanel);

        JPanel wrapper = new JPanel();
        JButton closeButton = new JButton("CREATE");
        closeButton.addActionListener(e -> {
            if(verifyMap(argMap,options)){
                try {
                    Collection<ClientPoint> dSet = dp.getCollection(argMap);
                    graph.addClientSet(dSet);
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
