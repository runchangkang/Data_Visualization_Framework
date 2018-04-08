package edu.cmu.cs.cs214.hw5.plugin;

import edu.cmu.cs.cs214.hw5.core.ParameterList;
import edu.cmu.cs.cs214.hw5.core.QueryableSet;
import edu.cmu.cs.cs214.hw5.core.VisualPlugin;

import javax.swing.*;
import java.awt.*;
import java.awt.geom.Ellipse2D;
import java.awt.geom.Point2D;
import java.util.Map;

/**
 * Dummy visual plugin impl. to test loading
 */
public class TestVisual implements VisualPlugin{

    private static final String THICKNESS = "Thickness";
    private static final String LENGTH = "Length";

    @Override
    public String getName() {
        return "TestVisual Plugin";
    }

    @Override
    public ParameterList addInterfaceParameters() {
        ParameterList paramList = new ParameterList();

        paramList.addParameter(THICKNESS,0.0,10.0);
        paramList.addParameter(LENGTH,1.0,100.0);

        return paramList;
    }

    @Override
    public JPanel drawSet(QueryableSet qSet, int x, int y, Map<String,Double> results) {
        JPanel panel = new DrawPanel((int)(double) results.get(LENGTH));
        int thk = (int) (double) results.get(THICKNESS);
        panel.setBorder(BorderFactory.createLineBorder(Color.BLACK,thk));
        panel.setPreferredSize(new Dimension(x,y));
        return panel;
    }


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
