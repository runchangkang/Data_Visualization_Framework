package edu.cmu.cs.cs214.hw5.gui;

import javax.swing.*;
import java.util.List;

import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;

import edu.cmu.cs.cs214.hw5.core.DataGraph;
import edu.cmu.cs.cs214.hw5.core.DataPlugin;
import edu.cmu.cs.cs214.hw5.core.PluginLoader;

import java.awt.*;


/**
 * Dummy GUI implementation to test layout and loading
 */
public class ControlPanel extends JPanel{

    private JFrame frame;
    private DataGraph graph;
    private String label;
    private String selectedDataPlugin;
    private String selectedVizPlugin;
    private List<String> dataPluginList;
    private List<String> vizPluginList;

    private static final int WINDOW_WIDTH = 1000;
    private static final int WINDOW_HEIGHT = 600;

    private static final int PLUGIN_WIDTH = 200;
    private static final int GRAPH_WIDTH = 200;

    private static final int VIZ_WIDTH = WINDOW_WIDTH - PLUGIN_WIDTH - GRAPH_WIDTH;

    public void launch(DataGraph graph){
        this.graph = graph;
        JFrame frame = new JFrame();
        frame.setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
        this.dataPluginList = PluginLoader.listDataPlugins();
        this.vizPluginList = PluginLoader.listVisualPlugins();


        addStartScreen();
        frame.setTitle("GeoFilter Framework");
        frame.add(this);
        frame.setVisible(true);
        frame.pack();
        frame.setLocationRelativeTo(null);
        this.frame = frame;
    }

    private void addStartScreen(){
        JPanel panel = new JPanel(new BorderLayout());
        panel.setPreferredSize(new Dimension(WINDOW_WIDTH,WINDOW_HEIGHT));
        panel.setBackground(Color.darkGray);

        panel.add(pluginWindow(),BorderLayout.WEST);
        panel.add(graphWindow(),BorderLayout.CENTER);
        panel.add(vizWindow(),BorderLayout.EAST);

        add(panel);
    }

    private JPanel pluginWindow(){
        JPanel panel = new JPanel(new GridLayout(0,1));
        JPanel dataPanel = new JPanel(new GridLayout(0,1));
        dataPanel.add(new JLabel("Data Plugins"));

        for (String plugin : dataPluginList){
            dataPanel.add(new JButton(plugin));
        }

        dataPanel.add(new JButton("LOAD"));
        dataPanel.setPreferredSize(new Dimension(PLUGIN_WIDTH,WINDOW_HEIGHT/2));

        JPanel vizPanel = new JPanel(new GridLayout(0,1));
        vizPanel.add(new JLabel("Visual Plugins"));

        for (String plugin : vizPluginList){
            vizPanel.add(new JButton(plugin));
        }

        vizPanel.add(new JButton("LOAD"));
        vizPanel.setPreferredSize(new Dimension(PLUGIN_WIDTH,WINDOW_HEIGHT/2));

        panel.add(dataPanel);
        panel.add(vizPanel);

        panel.setPreferredSize(new Dimension(PLUGIN_WIDTH,WINDOW_HEIGHT));
        return panel;
    }

    private JPanel graphWindow(){
        JPanel panel = new JPanel(new BorderLayout());
        JButton button = new JButton("Graph Window");
        button.setPreferredSize(new Dimension(GRAPH_WIDTH,WINDOW_HEIGHT-100));
        panel.add(button,BorderLayout.CENTER);
        panel.add(createButton(),BorderLayout.SOUTH);
        panel.setPreferredSize(new Dimension(GRAPH_WIDTH,WINDOW_HEIGHT));
        return panel;
    }

    private JPanel vizWindow(){
        JPanel panel = new JPanel(new BorderLayout());
        JButton params = new JButton("Parameters");
        params.setPreferredSize(new Dimension(VIZ_WIDTH,100));
        panel.add(params,BorderLayout.NORTH);
        panel.add(new JButton("Visual Window"),BorderLayout.CENTER);
        panel.setPreferredSize(new Dimension(VIZ_WIDTH,WINDOW_HEIGHT));
        return panel;
    }

    private JButton createButton(){
        JButton button = new JButton("Create");
        button.addActionListener(e -> dataSetDialog());
        return button;
    }

    private void setLabel(String s){
        System.out.println(s);
        this.label = s;
    }

    private void dataSetDialog(){
        final JDialog dialog = new JDialog(frame,
                "Select a DataSet",
                true);

        JPanel optionPanel = new JPanel(new GridLayout(0,1));

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
                dataPluginDialog();
            }
        });
        optionPanel.add(closeButton);

        dialog.setContentPane(optionPanel);
        dialog.pack();
        dialog.setVisible(true);
        dialog.setLocationRelativeTo(frame);

        /*
        final JOptionPane optionPane = new JOptionPane(
                "The only way to close this dialog is by\n"
                        + "pressing one of the following buttons.\n"
                        + "Do you understand?",
                JOptionPane.QUESTION_MESSAGE,
                JOptionPane.YES_NO_OPTION);

        optionPane.addPropertyChangeListener(
                new PropertyChangeListener() {
                    public void propertyChange(PropertyChangeEvent e) {
                        String prop = e.getPropertyName();

                        if (dialog.isVisible()
                                && (e.getSource() == optionPane)
                                && (prop.equals(JOptionPane.VALUE_PROPERTY))) {
                            //If you were going to check something
                            //before closing the window, you'd do
                            //it here.
                            dialog.setVisible(false);
                        }
                    }
                });*/


        /*int value = ((Integer)optionPane.getValue()).intValue();
        if (value == JOptionPane.YES_OPTION) {
            setLabel("Good.");
        } else if (value == JOptionPane.NO_OPTION) {
            setLabel("Try using the window decorations "
                    + "to close the non-auto-closing dialog. "
                    + "You can't!");
        }*/
    }

    private void dataPluginDialog(){
        DataPlugin dp = PluginLoader.getDataPlugin(this.selectedDataPlugin);
        List<String> options = dp.getPopupParameters();
    }

}
