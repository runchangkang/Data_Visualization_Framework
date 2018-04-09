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

import javax.swing.*;
import java.awt.*;
import java.util.*;


public class ScatterPlugin implements VisualPlugin {
    private static final String COLOR = "Color";


    @Override
    public String getName() {
        return "Scatter Plugin";
    }

    /**
     * user can change the background color of the Area chart
     * @return
     */
    @Override
    public ParameterList addInterfaceParameters() {
        ParameterList paramList = new ParameterList();

        paramList.addParameter(COLOR,0.0,255.0);


        return paramList;
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
    public JPanel drawSet(QueryableSet qSet, int x, int y, Map<String,Double> results) {
        JPanel panel = new DrawPanel(qSet,(int)(double)results.get(COLOR),x,y);

        return panel;
    }

    class DrawPanel extends  JPanel {
        int alpha;
        int baseColorIndex;
        QueryableSet queryableSet;

        DrawPanel(QueryableSet queryableSet, int baseColorIndex, int x, int y) {
            super();
            this.queryableSet = queryableSet;
            this.alpha = alpha;
            this.baseColorIndex = baseColorIndex;
            this.setPreferredSize(new Dimension(x, y));
            this.setBorder(BorderFactory.createCompoundBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10),
                    BorderFactory.createLineBorder(Color.BLACK, 1)));
            this.setLayout(new BorderLayout());
            this.drawTheChart( baseColorIndex);

        }

        /**
         * draw the area scatter chart
         * @param baseColorIndex
         */
        private void drawTheChart(int baseColorIndex) {
            XYChart chart = new XYChartBuilder().width(this.getWidth()).height(this.getHeight()).title("Area Chart").xAxisTitle("time").yAxisTitle("value").build();

            chart.getStyler().setLegendPosition(Styler.LegendPosition.InsideNE);
            chart.getStyler().setDefaultSeriesRenderStyle(XYSeries.XYSeriesRenderStyle.Area);

            chart = this.addData(chart);
            chart.getStyler().setChartBackgroundColor(new Color(255 - baseColorIndex, 255 - baseColorIndex, 255 - baseColorIndex));

            this.add(new XChartPanel<XYChart>(chart), BorderLayout.CENTER);


        }

        /**
         * add the data into the chart object
         * @param xyChart the basic chart
         * @return a chart after insertion of data
         */
        private XYChart addData(XYChart xyChart) {

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
                xyChart.addSeries(attr, todoublePrimitive(timeValue), todoublePrimitive(value));
            }
            return xyChart;


        }

        /**
         * helper function that convert an Double ArrayList to a double primitive array
         * @param list
         * @return a double array
         */
        private double[] todoublePrimitive(ArrayList<Double> list) {
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
