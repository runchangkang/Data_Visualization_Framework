package edu.cmu.cs.cs214.hw5.core;

import javax.swing.BorderFactory;
import javax.swing.JButton;
import javax.swing.JComponent;
import javax.swing.JDialog;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JSlider;
import javax.swing.JTextField;
import javax.swing.SwingConstants;
import javax.swing.WindowConstants;
import javax.swing.border.CompoundBorder;
import javax.swing.border.EmptyBorder;
import javax.swing.border.LineBorder;
import javax.swing.border.MatteBorder;
import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.GridLayout;
import java.text.DecimalFormat;
import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.Map;


/**
 * GUI implementation to test layout and loading
 */
class ControlPanel extends JPanel{

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
    private static final int GRAPH_WIDTH = WINDOW_WIDTH - PLUGIN_WIDTH;
    private static final int VIZ_WIDTH = WINDOW_WIDTH - PLUGIN_WIDTH;
    private static final int PANEL_WIDTH = 300;
    private static final int PANEL_HEIGHT = 200;

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

        add(panel);
        this.revalidate();
    }

    /**
     * Plugin sidebar layout and setup. Displays loaded plugins of both types from files.
     * @return JPanel with plugin window
     */
    private JPanel pluginWindow(){
        JPanel wrapper = new JPanel(new BorderLayout());
        JPanel panel = new JPanel(new GridLayout(0,1,0,50));
        JPanel dataPanel = new JPanel(new BorderLayout());
        JPanel vizPanel = new JPanel(new BorderLayout());

        dataPanel.add(new JLabel("Data Plugins"),BorderLayout.NORTH);
        JPanel dataPluginPanel = new JPanel(new GridLayout(0,1));
        for (String plugin : dataPluginList){ dataPluginPanel.add(new JLabel(plugin)); }
        dataPluginPanel.setBorder(new EmptyBorder(0,20,0,0));
        dataPanel.add(dataPluginPanel,BorderLayout.CENTER);
        dataPanel.setBorder(new CompoundBorder(new LineBorder(Color.BLACK,1),new EmptyBorder(10,10,0,0)));
        dataPanel.setPreferredSize(new Dimension(PLUGIN_WIDTH,WINDOW_HEIGHT/2));

        vizPanel.add(new JLabel("Visual Plugins"),BorderLayout.NORTH);
        JPanel vizPluginPanel = new JPanel(new GridLayout(0,1));
        for (String plugin : vizPluginList){ vizPluginPanel.add(new JLabel(plugin)); }
        vizPluginPanel.setBorder(new EmptyBorder(0,20,0,0));
        vizPanel.add(vizPluginPanel,BorderLayout.CENTER);
        vizPanel.setBorder(new CompoundBorder(new LineBorder(Color.BLACK,1),new EmptyBorder(10,10,0,0)));
        vizPanel.setPreferredSize(new Dimension(PLUGIN_WIDTH,WINDOW_HEIGHT/2));

        JButton loadPluginButton = new JButton("AutoLoad Plugins");
        loadPluginButton.addActionListener(e -> {
            this.dataPluginList = PluginLoader.listDataPlugins();
            this.vizPluginList = PluginLoader.listVisualPlugins();
            addStartScreen();
        });
        loadPluginButton.setPreferredSize(new Dimension(PLUGIN_WIDTH,50));

        panel.add(dataPanel);
        panel.add(vizPanel);
        panel.setBorder(new EmptyBorder(0,0,20,0));
        wrapper.add(panel,BorderLayout.CENTER);
        wrapper.add(loadPluginButton,BorderLayout.SOUTH);

        wrapper.setBorder(new CompoundBorder(new MatteBorder(0,0,0,1,Color.BLACK),new EmptyBorder(20,20,20,20)));
        return wrapper;
    }

    /**
     * @return JPanel with the graph manipulation interface
     */
    private JPanel graphWindow(){
        JPanel panel = new JPanel(new BorderLayout());
        panel.add(gc.drawGraphWide(graph,GRAPH_WIDTH,WINDOW_HEIGHT),BorderLayout.CENTER);
        panel.add(createButton(),BorderLayout.SOUTH);
        panel.setBorder(new EmptyBorder(20,20,20,20));
        return panel;
    }

    /**
     * @return Button to import a new dataset
     */
    private JButton createButton(){
        JButton button = new JButton("Import New DataSet");
        ImportController ic = new ImportController(this,graph);
        button.addActionListener(e -> ic.dataSetDialog(dataPluginList,frame));
        button.setPreferredSize(new Dimension(GRAPH_WIDTH,50));
        return button;
    }

    /**
     * MAIN METHOD FOR VISUALISATION PLUGIN IMPLEMENTATION -- CREATES A POPUP.
     * This enables as many separate views as your computer can handle!
     * //todo: vizcontroller?
     */
     void vizWindow(){
        JPanel panel = new JPanel(new BorderLayout());

        //Default case: a visualisation has not yet been initialised
        if (selectedVizSet == null || selectedVizPlugin == null) {
            //System.out.println("Something is null");
            JButton params = new JButton("Parameters");
            params.setPreferredSize(new Dimension(VIZ_WIDTH, 100));
            panel.add(params, BorderLayout.NORTH);
            panel.add(new JButton("Visual Window"), BorderLayout.CENTER);
            panel.setPreferredSize(new Dimension(VIZ_WIDTH, WINDOW_HEIGHT));
        }
        else{  //okok Let's draw it!
            JDialog visualDisplay = new JDialog(frame, "Displaying " +
                    this.selectedVizSet.getName() + " with " + this.selectedVizPlugin, false);

            VisualPlugin plugin = PluginLoader.getVizPlugin(this.selectedVizPlugin);
            if (plugin == null){ //something really really bad happened
                return;
            }

            Map<String,Double> argMap = new HashMap<>();


            JPanel container = new JPanel();

            /* Logic: The visualisation is contained within the container panel.
             *     -> A reference is kept to the container panel by the slider listener
             *     -> argMap is initialised to standard but can be changed (just like the text popups)
             *     -> The container is reset and fully redrawn with new argMap on slider change
             */

            JPanel paramWrapper = new JPanel(new GridLayout(0,1));

            for (Parameter p : plugin.addInterfaceParameters().getParameters()){
                JPanel params = new JPanel(new BorderLayout());
                System.out.println(p.getName());
                argMap.put(p.getName(), (p.getMin() + p.getMax()) / 2);
                JLabel label = new JLabel(p.getName() + "    " + new DecimalFormat("####.##").format(p.getMin()));
                JSlider slider = new JSlider((int) p.getMin(), (int) p.getMax());
                JLabel max = new JLabel(new DecimalFormat("####.##").format(p.getMax()));
                slider.addChangeListener( e ->{
                    container.removeAll();
                    argMap.put(p.getName(),(double) slider.getValue());
                    container.add(drawViz(plugin,argMap));
                    container.revalidate();
                    container.repaint();
                });
                params.add(label,BorderLayout.WEST);
                params.add(slider,BorderLayout.CENTER);
                params.add(max,BorderLayout.EAST);
                paramWrapper.add(params);
            }
            panel.add(paramWrapper,BorderLayout.NORTH);


            JPanel drawnViz = drawViz(plugin,argMap);
            container.add(drawnViz);
            panel.add(container,BorderLayout.CENTER);
            panel.setBorder(new EmptyBorder(20,50,20,50));
            panel.setPreferredSize(new Dimension(VIZ_WIDTH + 100,WINDOW_HEIGHT));

            display(visualDisplay,panel,frame);
        }
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

    /**
     * Defines button to apply processing to a dataset
     * @param dataSet to apply transform to
     */
    void transDialog(DataSet dataSet){
        final JDialog dialog = new JDialog(frame, "Select Processing Operation", true);

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

        optionPanel.setPreferredSize(new Dimension(PANEL_WIDTH,PANEL_HEIGHT));

        display(dialog,optionPanel,frame);
    }

    /**
     * Sets the current vizSet state to be called by other function to make popup
     * @param ds the current DataSet to visualise.
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
                //System.out.println(this.selectedVizPlugin);
            });
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
        optionPanel.setPreferredSize(new Dimension(PANEL_WIDTH,PANEL_HEIGHT));

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
     * @param set dataSet to query for mins/maxes if desired
     */
    static JPanel paramFieldSet(Collection<String> args, Map<String,String> argMap, DataSet set){
        JPanel wrapper = new JPanel(new BorderLayout());
        JPanel labelPanel = new JPanel(new GridLayout(0,1));
        JPanel fieldPanel = new JPanel(new GridLayout(0,1));

        for (String parameter : args){
            JLabel paramLabel;

            if (set != null){
                double min = set.getMin(parameter);
                double max = set.getMax(parameter);
                String rng = parameter + "\t (min:" + new DecimalFormat("####.##").format(min) +
                                         " max:"+ new DecimalFormat("####.##").format(max) + ")";
                paramLabel = new JLabel(rng,SwingConstants.RIGHT);
            }
            else {
                paramLabel = new JLabel(parameter,SwingConstants.RIGHT);
            }

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
        return wrapper;
    }
}
