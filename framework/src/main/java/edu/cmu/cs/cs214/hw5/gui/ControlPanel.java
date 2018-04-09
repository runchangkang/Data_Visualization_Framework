package edu.cmu.cs.cs214.hw5.gui;

import edu.cmu.cs.cs214.hw5.core.*;

import javax.swing.*;
import java.awt.*;
import java.util.Collection;
import java.util.HashMap;
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
    private String selectedVizPlugin;
    private DataSet selectedVizSet;

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
     * MAIN METHOD FOR VISUALISATION PLUGIN IMPLEMENTATION
     * //Todo: Only works with one visualisation right now. hack it to work with multiple? // overlay?
     *          -> swing has something called glasspanel which might allow call paintComponent of diff panel over other
     * //Todo: MOVE INTO VIZCONTROLLER CLASS
     *
     * @return JPanel with the visualisation interface
     */
    private JPanel vizWindow(){
        JPanel panel = new JPanel(new BorderLayout());

        //Default case: a visualisation has not yet been initialised
        if (selectedVizSet == null || selectedVizPlugin == null) {
            JButton params = new JButton("Parameters");
            params.setPreferredSize(new Dimension(VIZ_WIDTH, 100));
            panel.add(params, BorderLayout.NORTH);
            panel.add(new JButton("Visual Window"), BorderLayout.CENTER);
            panel.setPreferredSize(new Dimension(VIZ_WIDTH, WINDOW_HEIGHT));
        }
        else{  //okok Let's draw it!
            VisualPlugin plugin = PluginLoader.getVizPlugin(this.selectedVizPlugin);

            Map<String,Double> argMap = new HashMap<>();


            JPanel container = new JPanel();

            /* Logic: The visualisation is contained within the container panel.
             *     -> A reference is kept to the container panel by the slider listener
             *     -> argMap is initialised to standard but can be changed (just like the text popups)
             *     -> The container is reset and fully redrawn with new argMap on slider change
             */

            JPanel params = new JPanel(new GridLayout(0,2));
            for (Parameter p : plugin.addInterfaceParameters().getParameters()){
                argMap.put(p.getName(), (p.getMin() + p.getMax()) / 2);
                JLabel label = new JLabel(p.getName());
                JSlider slider = new JSlider((int) p.getMin(), (int) p.getMax());
                slider.addChangeListener( e ->{
                    container.removeAll();
                    argMap.put(p.getName(),(double) slider.getValue());
                    container.add(drawViz(plugin,argMap));
                    container.revalidate();
                    container.repaint();
                });
                params.add(label);
                params.add(slider);
            }
            panel.add(params,BorderLayout.NORTH);


            JPanel drawnViz = drawViz(plugin,argMap);
            container.add(drawnViz);
            panel.add(container,BorderLayout.CENTER);
            panel.setPreferredSize(new Dimension(VIZ_WIDTH,WINDOW_HEIGHT));
        }

        return panel;
    }

    /**
     * Redraw the visualisation the plugin's method / reparameterized argmap
     * @param plugin to visualise with
     * @param argMap parameters to use
     * @return drawn JPanel
     */
    private JPanel drawViz(VisualPlugin plugin,Map<String,Double> argMap){
        return plugin.drawSet(new QueryableSet(selectedVizSet),
                VIZ_WIDTH,WINDOW_HEIGHT-100,argMap);
    }

    //todo: add clearer selections states to these buttons (also applx in selectedVizPlugin
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
     * Sets the current dataSet to visualise. Perhaps use multiple?
     * @param ds
     */
    public void setSelectedVizSet(DataSet ds){
        this.selectedVizSet = ds;
    }

    /**
     * Popup dialog to set controlPanel's visualization plugin from the available options
     */
    public void getSelectedVizPlugin(){
        final JDialog dialog = new JDialog(frame, "Select a Visualisation Plugin", true);
        selectedVizPlugin = null; //reset

        JPanel optionPanel = new JPanel(new GridLayout(0,1));
        optionPanel.add(new JLabel("  Select a Visual Plugin from the loaded list.  "));

        for (String pluginName : vizPluginList){
            JButton button = new JButton(pluginName);
            button.addActionListener(e -> {
                this.selectedVizPlugin = pluginName;
                System.out.println(this.selectedVizPlugin);});
            optionPanel.add(button);
        }

        JButton closeButton = new JButton("VISUALIZE");
        closeButton.addActionListener(e -> {
            if(selectedVizPlugin != null) {
                dialog.setVisible(false);
                addStartScreen();
            }
            else{
                JOptionPane.showMessageDialog(dialog, "Please select a visual plugin.");
            }
        });
        optionPanel.add(closeButton);

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

        labelPanel.setBorder(BorderFactory.createEmptyBorder(10,10,10,10));
        fieldPanel.setBorder(BorderFactory.createEmptyBorder(10,10,10,10));
        wrapper.add(labelPanel,BorderLayout.WEST);
        wrapper.add(fieldPanel,BorderLayout.CENTER);
        container.add(wrapper);
    }
}
