package edu.cmu.cs.cs214.hw5.plugin;

import edu.cmu.cs.cs214.hw5.core.DataPoint;
import edu.cmu.cs.cs214.hw5.core.ParameterList;
import edu.cmu.cs.cs214.hw5.core.QueryableSet;
import edu.cmu.cs.cs214.hw5.core.VisualPlugin;
import org.jfree.chart.ChartFactory;
import org.jfree.chart.ChartPanel;
import org.jfree.chart.JFreeChart;
import org.jfree.chart.axis.CategoryAxis;
import org.jfree.chart.axis.CategoryLabelPositions;
import org.jfree.chart.axis.NumberAxis;
import org.jfree.chart.plot.CategoryPlot;
import org.jfree.chart.plot.PlotOrientation;
import org.jfree.chart.renderer.category.LineAndShapeRenderer;
import org.jfree.data.category.CategoryDataset;
import org.jfree.data.category.DefaultCategoryDataset;

import javax.swing.*;
import java.awt.*;
import java.awt.geom.Ellipse2D;
import java.util.ArrayList;

public class BarGraphPlugin implements VisualPlugin{
    private ArrayList<DataPoint> dataPoints = new ArrayList<>();

    @Override
    public String getName() {
        return "BarGraph Plugin";
    }

    @Override
    public ParameterList addInterfaceParameters() {

        return null;

    }

    @Override
    public JPanel drawSet(QueryableSet qSet, int x, int y) {

        for(String atrString: qSet.getAttributes()){
            for (DataPoint dp : qSet.getAttributeGroup(atrString)){
                this.dataPoints.add(dp);
            }
        }
        CategoryDataset categoryDataset = this.creatDataSet();
        JFreeChart jFreeChart = createChart(categoryDataset);
        return new ChartPanel(jFreeChart);
    }

    /**
     * create the CategoryDataSet object for the jFreeChart
     * @return a CategoryDataSet that contains all of the attribute values for each dataPoint
     */
    private  CategoryDataset creatDataSet(){
        DefaultCategoryDataset defaultCategoryDataset = new DefaultCategoryDataset();
        for(DataPoint dp : this.dataPoints){
            for(String attr: dp.getAttributes()){
                defaultCategoryDataset.addValue(dp.getAttribute(attr),attr,Double.toString(dp.getT()));
            }
        }
        return defaultCategoryDataset;
    }

    private JFreeChart createChart(CategoryDataset categoryDataset){
        JFreeChart jFreeChart = ChartFactory.createLineChart(
                "Scatter Graph",
                "Time",
                "Value",
                categoryDataset,
                PlotOrientation.VERTICAL,
                false,
                true,false);
        jFreeChart.setBackgroundPaint(Color.white);
        CategoryPlot categoryPlot = (CategoryPlot) jFreeChart.getPlot();
        categoryPlot.setBackgroundPaint(Color.lightGray);
        categoryPlot.setRangeGridlinePaint(Color.white);
        categoryPlot.setRangeGridlinesVisible(false);

        NumberAxis numberaxis = (NumberAxis) categoryPlot.getRangeAxis();
        numberaxis.setStandardTickUnits(NumberAxis.createIntegerTickUnits());
        CategoryAxis domainAxis = categoryPlot.getDomainAxis();
        domainAxis.setCategoryLabelPositions(CategoryLabelPositions.UP_45);
        domainAxis.setLowerMargin(0.0);
        domainAxis.setUpperMargin(0.0);



        LineAndShapeRenderer lineandshaperenderer = (LineAndShapeRenderer) categoryPlot.getRenderer();
        lineandshaperenderer.setShapesVisible(true);
        lineandshaperenderer.setBaseFillPaint(Color.white);
        lineandshaperenderer.setSeriesStroke(0, new BasicStroke(3.0F));
        lineandshaperenderer.setSeriesOutlineStroke(0, new BasicStroke(2.0F));
        lineandshaperenderer.setSeriesShape(0, new Ellipse2D.Double(-5.0, -5.0,
                10.0, 10.0));



        return jFreeChart;
    }


}
