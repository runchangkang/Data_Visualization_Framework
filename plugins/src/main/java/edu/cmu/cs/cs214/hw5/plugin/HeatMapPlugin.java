package edu.cmu.cs.cs214.hw5.plugin;

import edu.cmu.cs.cs214.hw5.core.DataPoint;
import edu.cmu.cs.cs214.hw5.core.ParameterList;
import edu.cmu.cs.cs214.hw5.core.QueryableSet;
import edu.cmu.cs.cs214.hw5.core.VisualPlugin;
import org.knowm.xchart.XChartPanel;
import org.knowm.xchart.XYChart;
import org.knowm.xchart.XYChartBuilder;
import org.knowm.xchart.XYSeries;
import org.knowm.xchart.style.markers.SeriesMarkers;

import javax.swing.*;
import java.awt.*;
import java.util.*;
import java.util.List;

/**
 * This plugin will create a heat map base of the datapoints,
 * The datapoints within same attribute will have the same series of color
 * The larger the point's attribute value, the darker of the color will be
 */
public class HeatMapPlugin implements VisualPlugin{

    private static final String ALPHA = "Alpha";
    private static final int LABEL_DISPLAY_THESHOLD = 200;
    /**
     * get the name of the vis plugin
     * @return
     */
    @Override
    public String getName() {

        return "Heat Map Plugin";
    }

    /**
     * get the alpha value of the marker point
     * @return
     */
    @Override
    public ParameterList addInterfaceParameters() {
        ParameterList parameterList = new ParameterList();
        parameterList.addParameter(ALPHA,0.0,255.0);
        return parameterList;
    }

    /**
     *
     * @param qSet set queryable for the data (see QueryableSet API)
     * @param x dimension that JPanel will be sized to
     * @param y dimension that JPanel will be sized to
     * @param results
     * @return
     */
    @Override
    public JPanel drawSet(QueryableSet qSet, int x, int y, Map<String, Double> results) {
        return new DrawPanel(qSet,x,y,results.get(ALPHA));
    }
    class DrawPanel extends  JPanel {
        double alpha;
        QueryableSet queryableSet;
        DrawPanel(QueryableSet queryableSet,int x, int y,double alpha){
            super();
            this.alpha = alpha;
            this.queryableSet = queryableSet;
            this.setPreferredSize(new Dimension(x, y));
            this.setBorder(BorderFactory.createCompoundBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10),
                    BorderFactory.createLineBorder(Color.BLACK, 1)));
            this.setLayout(new BorderLayout());
            this.drawTheChart(alpha);


        }

        /**
         * draw the heat map chart
         * @param
         */
        private void drawTheChart(double alpha) {
            XYChart chart = new XYChartBuilder().width(this.getWidth()).height(this.getHeight()).title("Heat Map Chart").xAxisTitle("x").yAxisTitle("y").build();
            chart.getStyler().setDefaultSeriesRenderStyle(XYSeries.XYSeriesRenderStyle.Scatter);
            chart.getStyler().setLegendVisible(false);
            chart = this.addData(chart,alpha);
            this.add(new XChartPanel<>(chart), BorderLayout.CENTER);
        }
        /**
         * add the data into the chart object
         * @param xyChart the basic chart
         * @return a chart after insertion of data
         */
        private XYChart addData(XYChart xyChart,double alpha) {
            Map<String,Color>colorMap = new HashMap<>();
            Color[] colors = new Color[]{Color.BLUE,Color.RED,Color.GREEN,Color.YELLOW,Color.orange,Color.cyan,Color.pink};
            Color WHITE = Color.white;
            Random random = new Random();
            for (String attr : queryableSet.getAttributes()) {
                List<DataPoint> dataPoints = queryableSet.getAttributeGroup(attr);
                colorMap.put(attr,colors[random.nextInt(colors.length-1)]);
                double[] minMax = this.getMinMax(dataPoints,attr);
                for (DataPoint dataPoint : dataPoints) {
                    String name = attr+dataPoint.getX()+dataPoint.getY()+dataPoint.getT()+random.nextInt(100000);
                    XYSeries series = xyChart.addSeries(name,new double[]{dataPoint.getX()},new double[]{dataPoint.getY()});
                    if(dataPoints.size()<=LABEL_DISPLAY_THESHOLD){ series.setLabel(dataPoint.getLabel());}
                    series.setMarker(SeriesMarkers.CIRCLE);

                    Color thisColor = this.getColor(minMax,dataPoint.getAttribute(attr),colorMap.get(attr),WHITE,alpha);
                    series.setMarkerColor(thisColor);

                }

            }
            return xyChart;


        }

        /**
         * create the gradient color based on the value of the attribute
         * the darker the larger
         * @param minMax
         * @param value
         * @param maxColor
         * @param minColor
         * @param alpha
         * @return
         */
        private Color getColor(double[]minMax,double value,Color maxColor, Color minColor,double alpha){
            float ratio = (float)value/(float) minMax[0];
            int red = (int)(maxColor.getRed() * ratio + minColor.getRed() * (1 - ratio));
            int green = (int)(maxColor.getGreen() * ratio + minColor.getGreen() * (1 - ratio));
            int blue = (int)(maxColor.getBlue() * ratio + minColor.getBlue() * (1 - ratio));
            return new Color(red, green, blue,(int)(double)alpha);

        }

        /**
         * helper function that return the min and max value of datapoints
         * @param dataPoints
         * @param attr
         * @return
         */
        private double[] getMinMax(List<DataPoint>dataPoints,String attr){
            Set<Double> doubles = new HashSet<>();
            for (DataPoint dataPoint : dataPoints) {
                doubles.add(dataPoint.getAttribute(attr));
            }
            return new double[]{Collections.max(doubles),Collections.min(doubles)};
        }

    }
}
