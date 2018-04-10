package edu.cmu.cs.cs214.hw5.gui;

import edu.cmu.cs.cs214.hw5.core.DataGraph;
import edu.cmu.cs.cs214.hw5.core.DataSet;

import javax.swing.BorderFactory;
import javax.swing.JButton;
import javax.swing.JComponent;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.border.LineBorder;
import java.awt.*;
import java.awt.geom.Line2D;
import java.awt.geom.Point2D;
import java.util.HashMap;
import java.util.Map;
import java.util.List;

/**
 * Controller for drawing and performing datagraph manipulations
 */
public class GraphController {

    private static final int PADDING = 10;
    private static final int MIN_X_COLS = 5;
    private static final int MIN_Y_ROWS = 5;
    private static final int START_COLUMN = 1;
    private ControlPanel cp;
    private Map<DataSet, Location> dataMap = new HashMap<>();
    private GridBagConstraints constraints= new GridBagConstraints();

    /**
     * Instantiates a new graph controller
     * @param cp controlPanel instance to draw on
     */
    GraphController(ControlPanel cp){
        this.cp = cp;
        constraints.insets = new Insets(PADDING,PADDING,PADDING,PADDING);
        constraints.weightx = 1.0;
        constraints.weighty = 1.0;
    }


    /**
     * Stolen straight out of my Carcassonne GUI
     * @param graphPanel
     * @param x
     * @param y
     * @param dataset
     */
    private void addToGraph(JPanel graphPanel, int x, int y, JPanel dataset){
        //JPanel panel = new JPanel(new BorderLayout());
        constraints.gridx = x;
        constraints.gridy = y;
        //panel.add(dataset,BorderLayout.CENTER);
        graphPanel.add(dataset, constraints);
    }

    //
    private int getNextStartPos(){
        int y = -1;

        for (DataSet set: dataMap.keySet()){
            if (dataMap.get(set).getX() == START_COLUMN){
                y = Math.max(y,dataMap.get(set).getY());
            }
        }

        return y + 2;
    }

    private void shiftMapDown(int y){
        Map<DataSet,Location> shiftedMap = new HashMap<>();

        for (DataSet set : dataMap.keySet()){
            if (dataMap.get(set).getY() >= y) {
                Location l = dataMap.get(set);
                shiftedMap.put(set,new Location(l.getX(),l.getY()+1));
            }
        }

        for (DataSet set : shiftedMap.keySet()){
            dataMap.remove(set);
        }

        for (DataSet set : shiftedMap.keySet()){
            dataMap.put(set,shiftedMap.get(set));
        }
    }

    private void updateLocations(DataGraph graph){
        for (DataSet set : graph.getDataSets()){
            if (!dataMap.keySet().contains(set)) {
                if (graph.numParents(set) == 1) {
                    DataSet parent = graph.getParent(set);
                    Location parentLocation = dataMap.get(parent);
                    dataMap.put(set,new Location(parentLocation.getX()+1,parentLocation.getY()));
                } else if (graph.numParents(set) > 1) {
                    List<DataSet> parents = graph.getAllParents(set);
                    int maxX = 0;
                    int maxY = 0;
                    for (DataSet p : parents){
                        Location l = dataMap.get(p);
                        maxX = Math.max(l.getX(),maxX);
                        maxY = Math.max(l.getY(),maxY);
                    }
                    Location target = new Location(maxX,maxY+1);
                    if (dataMap.values().contains(target)){
                        shiftMapDown(maxY+1);
                    }
                    dataMap.put(set,target);
                } else {    //graph is root
                    int y = getNextStartPos();
                    dataMap.put(set, new Location(START_COLUMN, y));
                }
            }
        }
    }

    private Location getMaxMap(){
        int x = 0;
        int y = 0;

        for (DataSet p : dataMap.keySet()){
            Location l = dataMap.get(p);
            x = Math.max(l.getX(),x);
            y = Math.max(l.getY(),y);
        }
        return new Location(x + 1,y + 1);
    }

    private JPanel setButton(DataSet set, int x, int y){
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

        //filter/transform button
        JButton fbutton = new JButton("applyProcessing");
        fbutton.addActionListener( e -> cp.transDialog(set));

        //visualisation button
        JButton vbutton = new JButton("applyVisual");
        vbutton.addActionListener( e -> {
            cp.setSelectedVizSet(set);
            cp.getSelectedVizPlugin();
            cp.vizWindow();
        });

        //graphics
        relPanel.add(name);
        relPanel.add(size);
        relPanel.add(fbutton);
        relPanel.add(vbutton);
        relPanel.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createLineBorder(Color.BLACK,2,true),
                BorderFactory.createEmptyBorder(5,5,5,5)));

        relPanel.setMinimumSize(new Dimension(x,y));
        return relPanel;
    }


    JPanel drawGraphWide(DataGraph graph, int width, int height){
        updateLocations(graph);
        Location l = getMaxMap();
        int xDim = Math.min(MIN_X_COLS,l.getX());
        int yDim = Math.min(MIN_Y_ROWS,l.getY());

        JPanel graphContainer = new JPanel(new GridBagLayout());

        for (int i = 0; i <= yDim; i++){
            for (int j = 0; j <= xDim; j++){
                if (!dataMap.values().contains(new Location(j,i))) {
                    JPanel panel = new JPanel();
                    //System.out.println("W: "+ width/xDim);
                    //System.out.println("H: "+ height/yDim);
                    panel.setMinimumSize(new Dimension(width/xDim,height/yDim));
                    panel.add(new JLabel("(" + j + "," + i + ")"));
                    //panel.setBorder(new LineBorder(Color.BLACK, 4));
                    addToGraph(graphContainer,j,i,panel);
                }
            }
        }

        for (DataSet set : dataMap.keySet()){
            Location loc = dataMap.get(set);
            addToGraph(graphContainer,loc.getX(),loc.getY(),setButton(set,width/xDim,height/yDim));
        }

        graphContainer.setMinimumSize(new Dimension(width,height));
        return graphContainer;
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
                cp.vizWindow();
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

    private class Location{
        private final int x;
        private final int y;

        Location(int x, int y){
            this.x = x;
            this.y = y;
        }

        int getX() {
            return x;
        }

        int getY() {
            return y;
        }

        @Override
        public boolean equals(Object obj) {
            if (!(obj instanceof Location)){
                return false;
            }
            else{
                Location l = (Location) obj;
                return l.getX() == this.x && l.getY() == this.y;
            }
        }

        @Override
        public int hashCode() {
            return ((31*x)+17)*y;
        }
    }

}
