package edu.cmu.cs.cs214.hw5.plugin;

import edu.cmu.cs.cs214.hw5.core.DataPoint;
import edu.cmu.cs.cs214.hw5.core.ParameterList;
import edu.cmu.cs.cs214.hw5.core.QueryableSet;
import edu.cmu.cs.cs214.hw5.core.VisualPlugin;
import org.knowm.xchart.XChartPanel;
import org.knowm.xchart.XYChart;
import org.knowm.xchart.XYChartBuilder;
import org.knowm.xchart.XYSeries;
import org.knowm.xchart.style.Styler;

import javax.swing.BorderFactory;
import javax.swing.JPanel;
import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.util.ArrayList;
import java.util.Map;
import java.util.SortedMap;
import java.util.TreeMap;


/**
 * The ScatterPlugin is a more classic, non-geo-based visualization that is still implementable - where we are showing
 * the general distribution of data with respect to an attribute. This is especially potent when combined with a
 * geographic filter.
 */
public class ScatterPlugin implements VisualPlugin {
    private static final String COLOR = "Color";


    /**
     * The name this plugin will be displayed with in the framework for purposes of selection and loading.
     * @return name string
     */
    @Override
    public String getName() {
        return "Scatter Plugin";
    }

    /**
     * Example of using the Parameter interface: the plugin creates a name, minimum, and maximum for each parameter
     * and allows the user to adjust.
     */
    @Override
    public ParameterList addInterfaceParameters() {
        ParameterList paramList = new ParameterList();

        paramList.addParameter(COLOR,0.0,255.0);

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
    public JPanel drawSet(QueryableSet qSet, int x, int y, Map<String,Double> results) {
        //fetch user parameter from map and call constructor
        return new DrawPanel(qSet,(int)(double)results.get(COLOR),x,y);
    }

    /**
     * This Panel uses the XChart Library to define a chart - see the documentation for the library here:
     * https://knowm.org/open-source/xchart/. This uses the fact that the return value is a JPanel to add
     * the additional XChart as an internal component
     *
     * Simple steps for getting a parameter and querying a set are included.
     */
    class DrawPanel extends JPanel {
        int baseColorIndex;
        QueryableSet queryableSet;

        //Controls setup and feeds parameters to generate a new XChart object and adds it to the panel
        DrawPanel(QueryableSet queryableSet, int baseColorIndex, int x, int y) {
            super();
            this.queryableSet = queryableSet;
            this.baseColorIndex = baseColorIndex;
            this.setPreferredSize(new Dimension(x, y));
            this.setBorder(BorderFactory.createCompoundBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10),
                    BorderFactory.createLineBorder(Color.BLACK, 1)));
            this.setLayout(new BorderLayout());
            this.drawTheChart(baseColorIndex);
        }

        /**
         * Draws the area scatter chart with XChart library
         * @param baseColorIndex color input from user to style with
         */
        private void drawTheChart(int baseColorIndex) {
            XYChart chart = new XYChartBuilder().width(this.getWidth()).height(this.getHeight())
                            .title("Area Chart").xAxisTitle("time").yAxisTitle("value").build();

            chart.getStyler().setLegendPosition(Styler.LegendPosition.InsideNE);
            chart.getStyler().setDefaultSeriesRenderStyle(XYSeries.XYSeriesRenderStyle.Area);

            int colorIndex = 255 - baseColorIndex;
            chart = this.addData(chart);
            chart.getStyler().setChartBackgroundColor(new Color(colorIndex,colorIndex,colorIndex));
            chart.getStyler().setToolTipType(Styler.ToolTipType.yLabels);
            chart.getStyler().setToolTipsEnabled(true);
            this.add(new XChartPanel<>(chart), BorderLayout.CENTER);
        }

        /**
         * Adds the data into the chart object by using the QueryableSet
         * @param xyChart the basic chart
         * @return a chart after data has been inserted
         */
        private XYChart addData(XYChart xyChart) {

            //Query the set for the data by fetching attribute groups and getting the existing values
            for (String attr : queryableSet.getAttributes()) {
                SortedMap<Double,Double> dataMap= new TreeMap<>();
                ArrayList<Double> timeValue = new ArrayList<>();
                ArrayList<Double> value = new ArrayList<>();
                for (DataPoint dataPoint : queryableSet.getAttributeGroup(attr)) {
                    dataMap.put(dataPoint.getT(),dataPoint.getAttribute(attr));
                }
                for(Double key:dataMap.keySet()){
                    timeValue.add(key);
                    value.add(dataMap.get(key));
                }

                xyChart.addSeries(attr, toDoublePrimitive(timeValue), toDoublePrimitive(value));
            }
            return xyChart;
        }

        /**
         * helper function that convert an Double ArrayList to a double primitive array
         * @param list contents to turn primitive
         * @return a double array with same contents
         */
        private double[] toDoublePrimitive(ArrayList<Double> list) {
            double[] arr = new double[list.size()];
            for (int i = 0; i < list.size(); i++) {
                if (list.get(i) != null) {
                    arr[i] = list.get(i);
                }
            }
            return arr;
        }
    }

}
