package edu.cmu.cs.cs214.hw5.plugin;

import edu.cmu.cs.cs214.hw5.core.DataPoint;
import edu.cmu.cs.cs214.hw5.core.ParameterList;
import edu.cmu.cs.cs214.hw5.core.QueryableSet;
import edu.cmu.cs.cs214.hw5.core.VisualPlugin;

import javax.swing.JPanel;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.util.Map;

public class PixelMap implements VisualPlugin {

    private static final String COLOR_PARAM = "Color";
    private static final String SPACING_PARAM = "Spacing";
    private static final String TIME_PARAM = "Time (%)";
    private static final String PARAM_PARAM = "Param";

    @Override
    public String getName() {
        return "PixelMap";
    }

    @Override
    public ParameterList addInterfaceParameters() {
        ParameterList paramList = new ParameterList();
        paramList.addParameter(COLOR_PARAM,0,360);
        paramList.addParameter(SPACING_PARAM,1,20);
        paramList.addParameter(TIME_PARAM,0,100);
        paramList.addParameter(PARAM_PARAM,0,100);
        return paramList;
    }

    @Override
    public JPanel drawSet(QueryableSet qSet, int x, int y, Map<String, Double> results) {
        double color = results.get(COLOR_PARAM);
        double time = results.get(TIME_PARAM);
        double spacing = results.get(SPACING_PARAM);
        double param = results.get(PARAM_PARAM);
        JPanel panel = new MapPanel(qSet, x, y, color, time, spacing, param);
        panel.setPreferredSize(new Dimension(x,y));

        return panel;
    }


    private class MapPanel extends JPanel{

        private QueryableSet qSet;
        private double width;
        private double height;
        private double spacing;
        private int intspacing;

        private double minx;
        private double maxx;
        private double xRange;
        private double miny;
        private double maxy;
        private double yRange;
        private float color;
        private double time;

        private String nextParam;
        private double paramMax;
        private double paramMin;
        private double paramRange;

        MapPanel(QueryableSet qSet, int x, int y, double color, double time, double spacing, double param){
            this.qSet = qSet;
            this.width = x;
            this.height = y;
            this.spacing = spacing;
            this.color =  (float) (color/360.0);
            this.intspacing = (int) spacing;
            this.time = time;
            param = param/100.0;

            if (qSet.getAttributes().size() > 0){
                int index = Math.max((int) (qSet.getAttributes().size() * param), qSet.getAttributes().size() -1);
                nextParam = qSet.getAttributes().toArray(new String[qSet.getAttributes().size()])[index];
                paramMin = getMin(qSet,nextParam);
                paramMax = getMax(qSet,nextParam);
                paramRange = paramMax - paramMin;
            }

            this.minx = getMin(qSet,DataPoint.X_ATTRIB);
            this.miny = getMin(qSet,DataPoint.Y_ATTRIB);
            this.maxx = getMax(qSet,DataPoint.X_ATTRIB);
            this.maxy = getMax(qSet,DataPoint.Y_ATTRIB);

            double minTime = getMin(qSet,DataPoint.T_ATTRIB);
            double maxTime = getMax(qSet,DataPoint.T_ATTRIB);
            this.time = minTime + ((maxTime - minTime) * (time/100.0));

            this.xRange = maxx - minx;
            this.yRange = maxy - miny;
        }

        @Override
        protected void paintComponent(Graphics g) {
            super.paintComponent(g);
            Graphics2D g2d = (Graphics2D) g;

            for (double i = 0; i < height; i += spacing){
                double percenti = i / height;
                double queryY = this.miny + (yRange * percenti);
                for (double j = 0; j < width; j += spacing){ //unroll this to a separate loop?
                    double percentj = j / width;
                    double queryX = this.minx + (xRange * percentj);
                    double result = this.qSet.querySet(queryX, queryY, this.time, nextParam);
                    double percentResult = (result - paramMin) / paramRange;
                    Color c = new Color(Color.HSBtoRGB(color,0.75f,(float) percentResult));

                    g2d.setColor(c);
                    g2d.fillRect((int) j, (int)i, intspacing, intspacing);
                }

            }

        }

    }

    /**
     * Minimum element with an attribute in a dataset
     * @param qSet set to query
     * @param attribute to find min of
     * @return minimum value
     */
    private double getMin (QueryableSet qSet, String attribute){
        double result = Double.MAX_VALUE;
        for (DataPoint point : qSet.getAttributeGroup(attribute)){
            result = Math.min(result,point.getAttribute(attribute));
        }
        return result;
    }

    /**
     * Maximum element with an attribute in a dataset
     * @param qSet set to query
     * @param attribute to find max of
     * @return max value
     */
    private double getMax (QueryableSet qSet, String attribute){
        double result = -Double.MAX_VALUE;
        for (DataPoint point : qSet.getAttributeGroup(attribute)){
            result = Math.max(result,point.getAttribute(attribute));
        }
        return result;
    }
}
