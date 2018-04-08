package edu.cmu.cs.cs214.hw5.plugin;

import edu.cmu.cs.cs214.hw5.core.ParameterList;
import edu.cmu.cs.cs214.hw5.core.QueryableSet;
import edu.cmu.cs.cs214.hw5.core.VisualPlugin;

import javax.swing.BorderFactory;
import javax.swing.JPanel;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Rectangle;
import java.awt.geom.Ellipse2D;
import java.awt.geom.Point2D;
import java.util.Map;

/**
 * Dummy visual plugin impl. to test loading. Draws a variable border and circle based off of parameters
 */
public class TestVisual implements VisualPlugin{

    private static final String THICKNESS = "Thickness";
    private static final String RADIUS = "Radius";

    /**
     * @return plugin name
     */
    @Override
    public String getName() {
        return "TestVisual Plugin";
    }

    /**
     * @return list of slider-parameters to initialise
     * todo: figure out a general way to handle these from multiple plugins? if we can do simultaneous viz. should.
     */
    @Override
    public ParameterList addInterfaceParameters() {
        ParameterList paramList = new ParameterList();

        paramList.addParameter(THICKNESS,0.0,10.0);
        paramList.addParameter(RADIUS,1.0,100.0);

        return paramList;
    }

    /**
     * ignores the queryable-set in this case and draws a border and circle
     *
     * @param qSet set queryable for the data (see QueryableSet API)
     * @param x dimension that JPanel will be sized to
     * @param y dimension that JPanel will be sized to
     * @param results
     * @return
     */
    @Override
    public JPanel drawSet(QueryableSet qSet, int x, int y, Map<String,Double> results) {
        JPanel panel = new DrawPanel((int)(double) results.get(RADIUS));
        int thk = (int) (double) results.get(THICKNESS);
        panel.setBorder(BorderFactory.createCompoundBorder(BorderFactory.createEmptyBorder(10,10,10,10),
                                                           BorderFactory.createLineBorder(Color.BLACK,thk)));
        panel.setPreferredSize(new Dimension(x,y));
        return panel;
    }


    /**
     * Class to draw circle via paint-component override that takes in the RADIUS argument from the paramlist
     */
    class DrawPanel extends JPanel{
        int radius;

        DrawPanel(int radius){
            super();
            this.radius = radius;
        }

        @Override
        protected void paintComponent(Graphics g) {
            super.paintComponent(g);
            Graphics2D g2d = (Graphics2D) g;

            Point2D.Double p = new Point2D.Double();
            Rectangle r = this.getBounds();
            p.x = r.getCenterX();
            p.y = r.getCenterY();

            g2d.draw(new Ellipse2D.Double(r.getCenterX() - radius/2,r.getCenterY()-radius/2,radius,radius));

        }
    }
}
