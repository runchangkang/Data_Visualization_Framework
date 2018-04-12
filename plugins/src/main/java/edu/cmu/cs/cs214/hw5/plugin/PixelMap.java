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

/**
 * The PixelMap defines a brightness-based mapping (using filled Swing rectangles as pixels) over an entire geographic
 * region, showing how the value of one attribute changes over an area. This utilises the querySet function to generate
 * pixel values even given completely random data.
 *
 * It exposes sliders to control Color values, sizing of the drawn "Pixels", the time being displayed, and the
 * parameter being displayed. It can be used to find patterns within an attribute over a geographical region.
 */
public class PixelMap implements VisualPlugin {

    private static final String COLOR_PARAM = "Color";
    private static final String SPACING_PARAM = "Spacing";
    private static final String TIME_PARAM = "Time (%)";
    private static final String PARAM_PARAM = "Param";

    /**
     * The name this plugin will be displayed with in the framework for purposes of selection and loading.
     * @return name string
     */
    @Override
    public String getName() {
        return "PixelMap";
    }

    /**
     * The visualisation plugin can query the framework to provide a variety of parameters to tune the graphical
     * result of the visualisation -- for example adjusting stroke weights, colors, range of data displayed.
     * These will be provided as sliders to the user and expect a minimum/maximum value and a label (see: ParameterList)
     * Results will be returned to the client in the drawSet function.
     *
     * @return a list of parameters that contain useful information regarding the data visualisation
     */
    @Override
    public ParameterList addInterfaceParameters() {
        ParameterList paramList = new ParameterList();
        paramList.addParameter(COLOR_PARAM,0,360); //(Name, Min, Max) -- see ParameterList docs for more detail
        paramList.addParameter(SPACING_PARAM,3,20);
        paramList.addParameter(TIME_PARAM,0,100);
        paramList.addParameter(PARAM_PARAM,0,100);
        return paramList;
    }

    /**
     * The client must return a drawn JPanel, likely by implementing PaintComponent. This adds the flexibility to add
     * Swing components to the visualisation as well as to define and utilise external libraries as desired. The client
     * is given a QueryableSet which can be asked for 'interpolated' values -- points that represent a 'best guess' at
     * an attribute for a certain location, even when the data source doesn't necessarily contain that data point.
     * Arguments are provided from the parameters in the form of a mapping String (label) to Double (value).
     *
     * @param qSet set queryable for the data (see QueryableSet API)
     * @param x dimension that JPanel will be sized to in the framework display
     * @param y dimension that JPanel will be sized to in the framework display
     * @param results argument maps from the interface parameters. Client handles naming consistency.
     * @return fully drawn JPanel.
     */
    @Override
    public JPanel drawSet(QueryableSet qSet, int x, int y, Map<String, Double> results) {
        //Fetching parameters from the mapping and creating a new MapPanel
        double color = results.get(COLOR_PARAM);
        double time = results.get(TIME_PARAM);
        double spacing = results.get(SPACING_PARAM);
        double param = results.get(PARAM_PARAM);
        JPanel panel = new MapPanel(qSet, x, y, color, time, spacing, param);
        panel.setPreferredSize(new Dimension(x,y));

        return panel;
    }


    /**
     * Helper class that implements set drawing by using the parameters from the mapping to define a new
     * PaintComponent method, using Swing 'Rectangles' as pixels (and allowing user to change density). This
     * returns a brightness-based heat-map of a certain attribute over a certain slice in time (over whole data set),
     * and allows the user to control which attribute, time, and color are being displayed.
     */
    private class MapPanel extends JPanel{

        //Basic info
        private QueryableSet qSet;
        private double width;
        private double height;
        private double spacing;
        private int intspacing;

        //Caches a bunch of double values to speed up the inner draw loop
        private double minx;
        private double maxx;
        private double xRange;
        private double miny;
        private double maxy;
        private double yRange;
        private float color;
        private double time;

        //Parameter Selection
        private String nextParam;
        private double paramMax;
        private double paramMin;
        private double paramRange;

        /**
         * Initialising a MapPanel - This caches a bunch of relevant ranges and parameters for the PaintComponent.
         * @param qSet set to query information from
         * @param x dimension of final display
         * @param y dimension of final display
         * @param color (input from user - 0 -> 360 -- use as HUE value in HSB Color calculation)
         * @param time (input from user -> 0% to 100% -- use to select 'time' to search from in dataset)
         * @param spacing (input from user -> 3 -> 20 -- size of drawn rectangle in pixels)
         * @param param (input from user -> parameter index, selects from dataset parameters)
         */
        MapPanel(QueryableSet qSet, int x, int y, double color, double time, double spacing, double param){
            this.qSet = qSet;
            this.width = x;
            this.height = y;
            this.spacing = spacing;
            this.color =  (float) (color/360.0);
            this.intspacing = (int) spacing;
            this.time = time;
            param = param/100.0;

            //Parameter selection - get the param we're looking for
            if (qSet.getAttributes().size() > 0){
                int index = Math.min((int) (qSet.getAttributes().size() * param), qSet.getAttributes().size() -1);
                nextParam = qSet.getAttributes().toArray(new String[qSet.getAttributes().size()])[index];
                paramMin = getMin(qSet,nextParam);
                paramMax = getMax(qSet,nextParam);
                paramRange = paramMax - paramMin;
            }

            //Cache some ranges to not re-compute
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

        /**
         * Loop to calculate each pixel by spacing and drawing a Swing FillRect
         */
        @Override
        protected void paintComponent(Graphics g) {
            super.paintComponent(g);
            Graphics2D g2d = (Graphics2D) g;

            for (double i = 0; i < height; i += spacing){
                double percenti = i / height;
                double queryY = this.miny + (yRange * percenti);
                for (double j = 0; j < width; j += spacing){
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
