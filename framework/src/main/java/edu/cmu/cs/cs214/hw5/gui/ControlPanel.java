package edu.cmu.cs.cs214.hw5.gui;

import javax.swing.*;
import java.util.*;

import edu.cmu.cs.cs214.hw5.core.*;
import edu.cmu.cs.cs214.hw5.core.processors.Filter;
import edu.cmu.cs.cs214.hw5.core.processors.Transform;

import java.awt.*;
import java.util.List;


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

    private JPanel drawGraph(DataGraph graph, int height, int width){
        JPanel gPanel = new JPanel(new GridLayout(0, 1));

        int pHeight = Math.min(50,height / ((graph.getRelations().size() * 2 )+ 1));
        System.out.println(pHeight);

        for (Relation rel : graph.getRelations()) {
            JPanel relPanel = new JPanel(new GridLayout(0, 1));
            JLabel name = new JLabel(rel.getSources().get(0).getName());
            name.setHorizontalAlignment(JLabel.CENTER);
            name.setHorizontalTextPosition(JLabel.CENTER);

            JButton button = new JButton("applyFilter");
            button.setSize(new Dimension(width,pHeight/4));
            button.addActionListener( e -> transDialog(rel.getResult()));


            relPanel.add(name);
            relPanel.add(button);
            relPanel.setSize(new Dimension(width, pHeight));
            relPanel.setBackground(Color.gray);
            relPanel.setBorder(BorderFactory.createLineBorder(Color.WHITE,10,true));

            gPanel.add(relPanel);

            JPanel bufPanel = new JPanel();
            bufPanel.setSize(new Dimension(width, pHeight));
            gPanel.add(bufPanel);
            gPanel.setBackground(Color.gray);
        }

        for (int i = 0; i < 6 - graph.getRelations().size(); i++){
            JPanel bufPanel = new JPanel();
            bufPanel.setSize(new Dimension(width, pHeight));
            gPanel.add(bufPanel);
        }


        gPanel.setSize(new Dimension(width,height));
        gPanel.setBorder(BorderFactory.createLineBorder(Color.BLACK,4));
        return gPanel;
    }

    private void transDialog(DataSet dataSet){
        final JDialog dialog = new JDialog(frame,
                "Processing Data",
                true);

        JPanel optionPanel = new JPanel(new GridLayout(0,1));

        JButton fButton = new JButton("Filter");
        fButton.addActionListener(e -> {dialog.setVisible(false);filterDialog(dataSet);});
        optionPanel.add(fButton);

        JButton tButton = new JButton("Transform");
        tButton.addActionListener(e -> {dialog.setVisible(false);transformDialog(dataSet);});
        optionPanel.add(tButton);

        JButton jButton = new JButton("Join");
        jButton.addActionListener(e -> {dialog.setVisible(false);joinDialog(dataSet);});
        optionPanel.add(jButton);

        dialog.setContentPane(optionPanel);
        dialog.pack();
        dialog.setLocationRelativeTo(frame);
        dialog.setVisible(true);
    }

//Todo: extract these to an outer function
    private void filterDialog(DataSet dataSet){
        final JDialog dialog = new JDialog(frame,
                "Define filter expressions",
                true);

        JPanel optionPanel = new JPanel(new GridLayout(0,1));

        Map<String,String> argMap = new HashMap<>();

        for (String parameter : dataSet.getAttributes()){
            JPanel paramPanel = new JPanel();
            JLabel paramLabel = new JLabel(parameter);
            JTextField paramValue = new JTextField(30);
            paramValue.addFocusListener(new java.awt.event.FocusAdapter() {
                public void focusGained(java.awt.event.FocusEvent evt) { }
                public void focusLost(java.awt.event.FocusEvent evt) {
                    argMap.put(parameter,paramValue.getText());
                }
            });
            paramPanel.add(paramLabel);
            paramPanel.add(paramValue);
            optionPanel.add(paramPanel);
        }

        JButton closeButton = new JButton("APPLY");

        closeButton.addActionListener(e -> {
            try {
                Filter f = new Filter(argMap);
                List<DataSet> dsList = new ArrayList<>(Collections.singletonList(dataSet));
                graph.addRelation(new Relation(dsList,f));
                dialog.setVisible(false);
                addStartScreen();
            }
            catch (Exception e2){
                e2.printStackTrace();
                JOptionPane.showMessageDialog(frame, "An error occurred while processing data. Please try again.");
            }
        });


        optionPanel.add(closeButton);
        dialog.setContentPane(optionPanel);
        dialog.pack();
        dialog.setLocationRelativeTo(frame);
        dialog.setVisible(true);
    }

    private void transformDialog(DataSet dataSet){
        final JDialog dialog = new JDialog(frame,
                "Define transform expressions",
                true);

        JPanel optionPanel = new JPanel(new GridLayout(0,1));

        Map<String,String> argMap = new HashMap<>();

        for (String parameter : dataSet.getAttributes()){
            JPanel paramPanel = new JPanel();
            JLabel paramLabel = new JLabel(parameter);
            JTextField paramValue = new JTextField(30);
            paramValue.addFocusListener(new java.awt.event.FocusAdapter() {
                public void focusGained(java.awt.event.FocusEvent evt) { }
                public void focusLost(java.awt.event.FocusEvent evt) {
                    argMap.put(parameter,paramValue.getText());
                }
            });
            paramPanel.add(paramLabel);
            paramPanel.add(paramValue);
            optionPanel.add(paramPanel);
        }

        JButton closeButton = new JButton("APPLY");

        closeButton.addActionListener(e -> {
            try {
                Transform f = new Transform(argMap);
                List<DataSet> dsList = new ArrayList<>(Collections.singletonList(dataSet));
                graph.addRelation(new Relation(dsList,f));
                dialog.setVisible(false);
                addStartScreen();
            }
            catch (Exception e2){
                e2.printStackTrace();
                JOptionPane.showMessageDialog(frame, "An error occurred while processing data. Please try again.");
            }
        });


        optionPanel.add(closeButton);
        dialog.setContentPane(optionPanel);
        dialog.pack();
        dialog.setLocationRelativeTo(frame);
        dialog.setVisible(true);
    }

    private void joinDialog(DataSet dataSet){

    }




    private void addStartScreen(){
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
        /*
        JButton button = new JButton("Graph Window");
        button.setPreferredSize(new Dimension(GRAPH_WIDTH,WINDOW_HEIGHT-100)); */
        //panel.add(button,BorderLayout.CENTER);
        panel.add(drawGraph(graph,WINDOW_HEIGHT-100,GRAPH_WIDTH));
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
                "Select a DataSet Plugin",
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
        dialog.setLocationRelativeTo(frame);
        dialog.setVisible(true);
    }

    private void dataPluginDialog(){
        DataPlugin dp = PluginLoader.getDataPlugin(this.selectedDataPlugin);
        List<String> options = dp.getPopupParameters();

        final JDialog dialog = new JDialog(frame,
                "Select a DataSet Plugin",
                true);

        JPanel optionPanel = new JPanel(new GridLayout(0,1));

        Map<String,String> argMap = new HashMap<>();

        for (String parameter : options){
            JPanel paramPanel = new JPanel();
            JLabel paramLabel = new JLabel(parameter);
            JTextField paramValue = new JTextField(30);
            paramValue.addFocusListener(new java.awt.event.FocusAdapter() {
                public void focusGained(java.awt.event.FocusEvent evt) { }
                public void focusLost(java.awt.event.FocusEvent evt) {
                    argMap.put(parameter,paramValue.getText());
                }
            });
            paramPanel.add(paramLabel);
            paramPanel.add(paramValue);
            optionPanel.add(paramPanel);
        }


        JButton closeButton = new JButton("CREATE");

        closeButton.addActionListener(e -> {
            if(verifyMap(argMap,options)){
                try {
                    Collection<ClientPoint> dSet = dp.getCollection(argMap);
                    System.out.println("gotcollection");
                    graph.addDataSet(dSet);
                    System.out.println("finished adding dataset");
                    dialog.setVisible(false);
                    addStartScreen();
                }
                catch (Exception e2){
                    e2.printStackTrace();
                    JOptionPane.showMessageDialog(frame, "An error occurred while processing data. Please try again.");
                    //System.out.println("Wasn't able to parse data.");
                }
            }
        });
        optionPanel.add(closeButton);

        dialog.setContentPane(optionPanel);
        dialog.pack();
        dialog.setLocationRelativeTo(frame);
        dialog.setVisible(true);
    }

    private boolean verifyMap(Map<String,String> argMap, List<String> options){
        for (String option : options){
            if(!argMap.containsKey(option)){
                return false;
            }
            if ("".equals(argMap.get(option))){
                return false;
            }
        }
        return true;
    }

}
