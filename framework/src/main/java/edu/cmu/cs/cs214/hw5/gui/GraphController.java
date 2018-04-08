package edu.cmu.cs.cs214.hw5.gui;

import edu.cmu.cs.cs214.hw5.core.DataGraph;
import edu.cmu.cs.cs214.hw5.core.DataSet;
import edu.cmu.cs.cs214.hw5.core.Relation;

import javax.swing.*;
import javax.xml.crypto.Data;
import java.awt.*;
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
     * Panel that draws the datagraph
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

            JPanel sWrap = new JPanel();
            JLabel size = new JLabel(set.size() + " elements");
            size.setHorizontalAlignment(JLabel.CENTER);
            size.setHorizontalTextPosition(JLabel.CENTER);
            Font pf = size.getFont();
            size.setFont(new Font(pf.getFamily(),pf.getStyle(),12));
            sWrap.add(size);
            sWrap.setBorder(BorderFactory.createEmptyBorder(12,12,12,12));
            sWrap.setPreferredSize(new Dimension(width,pHeight/2));

            JButton button = new JButton("applyFilter");
            button.setSize(new Dimension(width,pHeight/4));
            button.addActionListener( e -> cp.transDialog(set));

            relPanel.add(name);
            relPanel.add(size);
            relPanel.add(button);
            //relPanel.setMaximumSize(new Dimension(width, pHeight));
            relPanel.setBorder(BorderFactory.createCompoundBorder(
                        BorderFactory.createLineBorder(Color.BLACK,2,true),
                        BorderFactory.createEmptyBorder(5,5,5,5)));
            //relPanel.setBorder(BorderFactory.createLineBorder(Color.WHITE,10,true));

            gPanel.add(relPanel);
            compSet[j] = relPanel;
            j++;
        }
        gPanel.addCompSet(compSet);

        //extra buffer space

        for (int i = 0; i < 4 - graph.getRelations().size(); i++){
            JPanel bufPanel = new JPanel();
            bufPanel.setSize(new Dimension(width, pHeight));
            gPanel.add(bufPanel);
        }

        gPanel.setSize(new Dimension(width,height));
        //JPanel bufPanel = new JPanel();
        //bufPanel.setPreferredSize(new Dimension(width,height));

        containerPanel.add(gPanel,BorderLayout.NORTH);
        //containerPanel.add(bufPanel,BorderLayout.CENTER);

        //gPanel.setBorder(BorderFactory.createLineBorder(Color.BLACK,2));
        return gPanel;
    }

    class GraphPanel extends JPanel{

        JComponent[] compSet;

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
