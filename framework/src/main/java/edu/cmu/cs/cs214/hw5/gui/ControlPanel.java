package edu.cmu.cs.cs214.hw5.gui;

import javax.swing.*;
import java.awt.*;
import edu.cmu.cs.cs214.hw5.core.PluginLoader;

public class ControlPanel extends JPanel{

    JFrame frame;

    public void launch(){
        JFrame frame = new JFrame();
        frame.setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
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
        panel.setPreferredSize(new Dimension(1000,600));
        panel.setBackground(Color.darkGray);

        panel.add(pluginWindow(),BorderLayout.WEST);
        panel.add(graphWindow(),BorderLayout.CENTER);
        panel.add(vizWindow(),BorderLayout.EAST);

        add(panel);
    }

    private JPanel pluginWindow(){
        JPanel panel = new JPanel(new GridLayout(0,1));
        panel.add(new JButton("Data Plugins"));

        for (String plugin : PluginLoader.listPlugins()){
            panel.add(new JButton(plugin));
        }

        panel.add(new JButton("Visual Plugins"));

        panel.setPreferredSize(new Dimension(200,600));
        return panel;
    }

    private JPanel graphWindow(){
        JPanel panel = new JPanel();
        JButton button = new JButton("Graph Window");
        button.setPreferredSize(new Dimension(200,600));
        panel.add(button);
        panel.setPreferredSize(new Dimension(200,600));
        return panel;
    }

    private JPanel vizWindow(){
        JPanel panel = new JPanel(new BorderLayout());
        JButton params = new JButton("Parameters");
        params.setPreferredSize(new Dimension(600,100));
        panel.add(params,BorderLayout.NORTH);
        panel.add(new JButton("Visual Window"),BorderLayout.CENTER);
        panel.setPreferredSize(new Dimension(600,600));
        return panel;
    }



}
