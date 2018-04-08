package edu.cmu.cs.cs214.hw5.gui;

import edu.cmu.cs.cs214.hw5.core.DataGraph;
import edu.cmu.cs.cs214.hw5.core.DataSet;

import javax.swing.*;
import java.awt.*;

/**
 * Controller for drawing and performing datagraph manipulations
 */
public class GraphController {

    private ControlPanel cp;

    /**
     * Instantiates a new graph controller
     * @param cp controlPanel instance to draw on
     */
    GraphController(ControlPanel cp){
        this.cp = cp;
    }

    /**
     * Panel that draws the datagraph
     * @param graph to draw
     * @param height allowed of graph
     * @param width allowed of graph
     * @return drawn panel w/ buttons & listeners
     */
    JPanel drawGraph(DataGraph graph, int height, int width){
        JPanel gPanel = new JPanel(new GridLayout(0, 1));

        int pHeight = Math.min(50,height / ((graph.getRelations().size() * 2 )+ 1));

        for (DataSet set : graph.getDataSets()) {
            JPanel relPanel = new JPanel(new GridLayout(0, 1));
            JLabel name = new JLabel(set.getName());
            name.setHorizontalAlignment(JLabel.CENTER);
            name.setHorizontalTextPosition(JLabel.CENTER);

            JButton button = new JButton("applyFilter");
            button.setSize(new Dimension(width,pHeight/4));
            button.addActionListener( e -> cp.transDialog(set));

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

        //extra buffer space
        for (int i = 0; i < 6 - graph.getRelations().size(); i++){
            JPanel bufPanel = new JPanel();
            bufPanel.setSize(new Dimension(width, pHeight));
            gPanel.add(bufPanel);
        }

        gPanel.setSize(new Dimension(width,height));
        gPanel.setBorder(BorderFactory.createLineBorder(Color.BLACK,4));
        return gPanel;
    }
}
