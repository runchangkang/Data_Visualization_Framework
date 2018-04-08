package edu.cmu.cs.cs214.hw5.gui;

import javax.swing.JButton;
import javax.swing.JComponent;
import javax.swing.JDialog;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTextField;
import javax.swing.SwingConstants;
import javax.swing.WindowConstants;
import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.GridLayout;

import edu.cmu.cs.cs214.hw5.core.DataGraph;
import edu.cmu.cs.cs214.hw5.core.DataSet;
import edu.cmu.cs.cs214.hw5.core.PluginLoader;

import java.util.Collection;
import java.util.List;
import java.util.Map;


/**
 * GUI implementation to test layout and loading
 */
public class ControlPanel extends JPanel{

    private JFrame frame;
    private DataGraph graph;
    private GraphController gc;
    private ProcessorController pc;
    private List<String> dataPluginList;
    private List<String> vizPluginList;

    private static final int WINDOW_WIDTH = 1000;
    private static final int WINDOW_HEIGHT = 600;
    private static final int PLUGIN_WIDTH = 200;
    private static final int GRAPH_WIDTH = 200;
    private static final int VIZ_WIDTH = WINDOW_WIDTH - PLUGIN_WIDTH - GRAPH_WIDTH;

    /**
     * Sets up frame and initialises the controllers. Imports any found plugins.
     * @param graph existing datagraph to visualise
     */
    public void launch(DataGraph graph){
        JFrame frame = new JFrame();
        frame.setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);

        this.dataPluginList = PluginLoader.listDataPlugins();
        this.vizPluginList = PluginLoader.listVisualPlugins();
        this.gc = new GraphController(this);
        this.graph = graph;
        this.pc = new ProcessorController(this,frame);

        addStartScreen();
        frame.setTitle("GeoFilter Framework");
        frame.add(this);
        frame.setVisible(true);
        frame.pack();
        frame.setLocationRelativeTo(null);
        this.frame = frame;
    }

    /**
     * Main window layout and setup
     */
    void addStartScreen(){
        this.removeAll();
        this.repaint();

        JPanel panel = new JPanel(new BorderLayout());
        panel.setPreferredSize(new Dimension(WINDOW_WIDTH,WINDOW_HEIGHT));
        panel.setBackground(Color.darkGray);

        panel.add(pluginWindow(),BorderLayout.WEST);
        panel.add(graphWindow(),BorderLayout.CENTER);
        panel.add(vizWindow(),BorderLayout.EAST);

        add(panel);
        this.revalidate();
    }

    /**
     * Plugin sidebar layout and setup. Displays loaded plugins from
     * @return JPanel with plugin window
     */
    private JPanel pluginWindow(){
        JPanel panel = new JPanel(new GridLayout(0,1));
        JPanel dataPanel = new JPanel(new GridLayout(0,1));
        dataPanel.add(new JLabel("Data Plugins"));

        for (String plugin : dataPluginList){ dataPanel.add(new JButton(plugin)); }

        dataPanel.setPreferredSize(new Dimension(PLUGIN_WIDTH,WINDOW_HEIGHT/2));

        JPanel vizPanel = new JPanel(new GridLayout(0,1));
        vizPanel.add(new JLabel("Visual Plugins"));

        for (String plugin : vizPluginList){ vizPanel.add(new JButton(plugin)); }

        JButton loadPluginButton = new JButton("Load Plugins");
        loadPluginButton.addActionListener(e -> {
            this.dataPluginList = PluginLoader.listDataPlugins();
            this.vizPluginList = PluginLoader.listVisualPlugins();
            addStartScreen();
        });

        vizPanel.add(loadPluginButton);
        vizPanel.setPreferredSize(new Dimension(PLUGIN_WIDTH,WINDOW_HEIGHT/2));

        panel.add(dataPanel);
        panel.add(vizPanel);

        panel.setPreferredSize(new Dimension(PLUGIN_WIDTH,WINDOW_HEIGHT));
        return panel;
    }

    /**
     * @return JPanel with the graph manipulation interface
     */
    private JPanel graphWindow(){
        JPanel panel = new JPanel(new BorderLayout());
        panel.add(gc.drawGraph(graph,WINDOW_HEIGHT-100,GRAPH_WIDTH));
        panel.add(createButton(),BorderLayout.SOUTH);
        panel.setPreferredSize(new Dimension(GRAPH_WIDTH,WINDOW_HEIGHT));
        return panel;
    }

    /**
     * @return Button to import a new dataset
     */
    private JButton createButton(){
        JButton button = new JButton("Create");
        ImportController ic = new ImportController(this,graph);
        button.addActionListener(e -> ic.dataSetDialog(dataPluginList,frame));
        return button;
    }

    /**
     * @return JPanel with the visualisation interface
     */
    private JPanel vizWindow(){
        JPanel panel = new JPanel(new BorderLayout());
        JButton params = new JButton("Parameters");
        params.setPreferredSize(new Dimension(VIZ_WIDTH,100));
        panel.add(params,BorderLayout.NORTH);
        panel.add(new JButton("Visual Window"),BorderLayout.CENTER);
        panel.setPreferredSize(new Dimension(VIZ_WIDTH,WINDOW_HEIGHT));
        return panel;
    }


    //todo: add clearer selections states to these button
    /**
     * Defines button to apply processing to a dataset
     * @param dataSet to apply transform to
     */
    void transDialog(DataSet dataSet){
        final JDialog dialog = new JDialog(frame, "Processing Data", true);

        JPanel optionPanel = new JPanel(new GridLayout(0,1));

        JButton fButton = new JButton("Filter");
        fButton.addActionListener(e -> {dialog.setVisible(false);pc.filterDialog(dataSet,graph);});
        optionPanel.add(fButton);

        JButton tButton = new JButton("Transform");
        tButton.addActionListener(e -> {dialog.setVisible(false);pc.transformDialog(dataSet,graph);});
        optionPanel.add(tButton);

        JButton jButton = new JButton("Join");
        jButton.addActionListener(e -> {dialog.setVisible(false);pc.joinDialog(dataSet,graph);});
        optionPanel.add(jButton);

        display(dialog,optionPanel,frame);
    }

    /**
     * Global dialog popup function
     * @param d dialog to popup
     * @param content to place in the dialog
     * @param frame to popup above
     */
    static void display(JDialog d,JComponent content, JFrame frame){
        d.setContentPane(content);
        d.pack();
        d.setLocationRelativeTo(frame);
        d.setVisible(true);
    }

    /**
     * Define a text-field popup to get multiple text parameters from the user
     * @param args labels to get
     * @param argMap returned arguments
     * @param container JPanel to be placed in
     */
    //todo: tweak to not be stupid (see container)
    static void paramFieldSet(Collection<String> args, Map<String,String> argMap, JPanel container){
        JPanel wrapper = new JPanel(new BorderLayout());
        JPanel labelPanel = new JPanel(new GridLayout(0,1));
        JPanel fieldPanel = new JPanel(new GridLayout(0,1));

        for (String parameter : args){
            JLabel paramLabel = new JLabel(parameter,SwingConstants.RIGHT);
            JTextField paramValue = new JTextField(25);
            paramValue.addFocusListener(new java.awt.event.FocusAdapter() {
                public void focusGained(java.awt.event.FocusEvent evt) { }
                public void focusLost(java.awt.event.FocusEvent evt) {
                    argMap.put(parameter,paramValue.getText());
                }
            });
            labelPanel.add(paramLabel);
            fieldPanel.add(paramValue);
        }

        wrapper.add(labelPanel,BorderLayout.WEST);
        wrapper.add(fieldPanel,BorderLayout.CENTER);
        container.add(wrapper);
    }
}
