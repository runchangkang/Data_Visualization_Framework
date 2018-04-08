package edu.cmu.cs.cs214.hw5.gui;

import edu.cmu.cs.cs214.hw5.core.DataGraph;
import edu.cmu.cs.cs214.hw5.core.DataSet;

import javax.swing.BorderFactory;
import javax.swing.JButton;
import javax.swing.JComponent;
import javax.swing.JLabel;
import javax.swing.JPanel;
import java.awt.BasicStroke;
import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Component;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.GridLayout;
import java.awt.Rectangle;
import java.awt.geom.Line2D;
import java.awt.geom.Point2D;

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
     * Panel that draws the datagraph.
     * //Todo: Use GridBagLayout and draw to the actual relationships
     *
     * @param graph to draw
     * @param height allowed of graph
     * @param width allowed of graph
     * @return drawn panel w/ buttons & listeners
     */
    JPanel drawGraph(DataGraph graph, int height, int width){
        JPanel containerPanel = new JPanel(new BorderLayout());
        GridLayout padLayout = new GridLayout(0,1);
        padLayout.setVgap(20);
        GraphPanel gPanel = new GraphPanel(padLayout);

        int pHeight = Math.min(50,height / ((graph.getRelations().size() * 2 )+ 1));

        JComponent[] compSet = new JComponent[graph.getDataSets().size()];

        int j = 0;
        for (DataSet set : graph.getDataSets()) {
            JPanel relPanel = new JPanel(new GridLayout(0,1));

            JLabel name = new JLabel(set.getName());
            name.setHorizontalAlignment(JLabel.CENTER);
            name.setHorizontalTextPosition(JLabel.CENTER);

            //add main label
            JPanel sWrap = new JPanel();
            JLabel size = new JLabel(set.size() + " elements");
            size.setHorizontalAlignment(JLabel.CENTER);
            size.setHorizontalTextPosition(JLabel.CENTER);
            Font pf = size.getFont();
            size.setFont(new Font(pf.getFamily(),pf.getStyle(),12));
            sWrap.add(size);
            sWrap.setBorder(BorderFactory.createEmptyBorder(12,12,12,12));
            sWrap.setPreferredSize(new Dimension(width,pHeight/2));

            //filter/transform button
            JButton fbutton = new JButton("applyProcessing");
            fbutton.setSize(new Dimension(width,pHeight/4));
            fbutton.addActionListener( e -> cp.transDialog(set));

            //visualisation button
            JButton vbutton = new JButton("applyVisual");
            vbutton.setSize(new Dimension(width,pHeight/4));
            vbutton.addActionListener( e -> {
                cp.setSelectedVizSet(set);
                cp.getSelectedVizPlugin();
            });

            //graphics
            relPanel.add(name);
            relPanel.add(size);
            relPanel.add(fbutton);
            relPanel.add(vbutton);
            relPanel.setBorder(BorderFactory.createCompoundBorder(
                        BorderFactory.createLineBorder(Color.BLACK,2,true),
                        BorderFactory.createEmptyBorder(5,5,5,5)));

            gPanel.add(relPanel);
            compSet[j] = relPanel;
            j++;
        }
        gPanel.addCompSet(compSet); //gives the gPanel the graph components to draw //todo: add relationships

        //extra buffer space
        for (int i = 0; i < 4 - graph.getRelations().size(); i++){
            JPanel bufPanel = new JPanel();
            bufPanel.setSize(new Dimension(width, pHeight));
            gPanel.add(bufPanel);
        }
        gPanel.setSize(new Dimension(width,height));
        containerPanel.add(gPanel,BorderLayout.NORTH);
        return gPanel;
    }

    /**
     * GraphPanel will draw lines btwn all of the components (inside it) that it is given
     * This should hypothetically work with GridBag too if there aren't overlaps
     */
    class GraphPanel extends JPanel{

        private JComponent[] compSet; //components to draw

        GraphPanel(GridLayout layout){
            super(layout);
        }

        @Override
        protected void paintComponent(Graphics g) {
            super.paintComponent(g);

            Graphics2D g2d = (Graphics2D) g;

            Component[] c = compSet;
            for(int j = 0; j < c.length; j++) {
                Point2D.Double p1 = getCenter(c[j]);
                for(int k = j+1; k < c.length; k++) {
                    Point2D p2 = getCenter(c[k]);
                    System.out.println(p1 + " -> " + p2);
                    g2d.setStroke(new BasicStroke(2f));
                    g2d.draw(new Line2D.Double(p1, p2));
                }
            }
        }

        private Point2D.Double getCenter(Component c) {
            Point2D.Double p = new Point2D.Double();
            Rectangle r = c.getBounds();
            p.x = r.getCenterX();
            p.y = r.getCenterY();
            return p;
        }

        void addCompSet(JComponent[] compSet){
            this.compSet = compSet;
        }
    }


}
