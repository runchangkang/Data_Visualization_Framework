package edu.cmu.cs.cs214.hw5.gui;

import edu.cmu.cs.cs214.hw5.core.DataGraph;
import edu.cmu.cs.cs214.hw5.core.DataSet;
import edu.cmu.cs.cs214.hw5.core.Relation;

import javax.swing.BorderFactory;
import javax.swing.JButton;
import javax.swing.JComponent;
import javax.swing.JLabel;
import javax.swing.JPanel;
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
    private static final int ARC = 15;
    private ControlPanel cp;
    private Map<DataSet, Location> dataMap = new HashMap<>();
    private GridBagConstraints constraints = new GridBagConstraints();

    private JComponent[] components;
    private int counter;
    private Map<DataSet,Integer> componentIndex;

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
     * GridBagLayout insertion wrapper
     * @param graphPanel container to add to
     * @param x grid position to add in (NOT pixel)
     * @param y grid position to add in (NOT pixel)
     * @param setButton button being added
     */
    private void addToGraph(JPanel graphPanel, int x, int y, JPanel setButton){
        constraints.gridx = x;
        constraints.gridy = y;
        graphPanel.add(setButton, constraints);
    }


    private int getNextStartPos(){
        int y = -1;

        for (DataSet set: dataMap.keySet()){
            if (dataMap.get(set).getX() == START_COLUMN){
                y = Math.max(y,dataMap.get(set).getY());
            }
        }
        return y + 2;
    }

    /**
     * Adds one row of vertical space into the gridMap at any given y Coordinate
     * @param y index to insert row at
     */
    private void shiftMapDown(int y){
        Map<DataSet,Location> shiftedMap = new HashMap<>();

        //compute shift
        for (DataSet set : dataMap.keySet()){
            if (dataMap.get(set).getY() >= y) {
                Location l = dataMap.get(set);
                shiftedMap.put(set,new Location(l.getX(),l.getY()+1));
            }
        }

        //out with the old
        for (DataSet set : shiftedMap.keySet()){ dataMap.remove(set); }

        //in with the new
        for (DataSet set : shiftedMap.keySet()){ dataMap.put(set,shiftedMap.get(set)); }
    }

    //todo: add double-maxX counter to determine further join offset
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

    /**
     * @return location representing the largest index present in the dataMap
     */
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

    /**
     * Makes a new DataSet button in the graph.
     * @param set to enbuttonate
     * @param x sizing
     * @param y sizing
     * @return JPanel with button representation
     */
    private JPanel setButton(DataSet set, int x, int y){
        JPanel relPanel = new RoundedPanel(new GridLayout(0,1),Color.BLACK);

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
        //relPanel.setBorder(BorderFactory.createCompoundBorder(
                //BorderFactory.createLineBorder(Color.BLACK,2,true),
                //BorderFactory.createEmptyBorder(5,5,5,5)));

        relPanel.setMinimumSize(new Dimension(x,y));

        componentIndex.put(set,counter);
        components[counter] = relPanel;
        counter++;

        return relPanel;
    }


    JPanel drawGraphWide(DataGraph graph, int width, int height){
        updateLocations(graph);
        Location l = getMaxMap();
        int xDim = Math.min(MIN_X_COLS,l.getX());
        int yDim = Math.min(MIN_Y_ROWS,l.getY());

        componentIndex = new HashMap<>();
        components = new JComponent[(xDim+1)*(yDim+1)]; //reset states
        counter = 0;

        GraphPanel graphContainer = new GraphPanel(new GridBagLayout(),graph);

        for (int i = 0; i <= yDim; i++){
            for (int j = 0; j <= xDim; j++){
                if (!dataMap.values().contains(new Location(j,i)) && (i == 0 || j == 0 || i == yDim || j == xDim)){
                    JPanel panel = new JPanel();
                    panel.setMinimumSize(new Dimension(width/xDim,height/yDim));
                    //panel.add(new JLabel("(" + j + "," + i + ")"));
                    addToGraph(graphContainer,j,i,panel);
                }
            }
        }

        for (DataSet set : dataMap.keySet()){
            Location loc = dataMap.get(set);
            addToGraph(graphContainer,loc.getX(),loc.getY(),setButton(set,width/xDim,height/yDim));
        }

        graphContainer.setComponents(components);
        graphContainer.setMapping(componentIndex);
        graphContainer.setMinimumSize(new Dimension(width,height));
        return graphContainer;
    }

    /**
     * GraphPanel will draw lines btwn all of the components (inside it) that it is given
     * This should hypothetically work with GridBag too if there aren't overlaps
     */
    class GraphPanel extends JPanel{

        private List<Relation> relations;
        private JComponent[] components;
        private Map<DataSet,Integer> mapping;

        /**
         * New GraphPanel instance
         * @param layout to pass to super
         * @param graph to get relations to draw
         */
        GraphPanel(GridBagLayout layout, DataGraph graph){
            super(layout);
            this.relations = graph.getRelations();
        }

        /**
         * Determine parent-child relationships defined in the graph and draw the relevant
         * connections. Note that the components and mapping field MUST be set before
         * this is actually called, or we'll get a NullPointerException.
         *
         * @param g graphics context
         */
        @Override
        protected void paintComponent(Graphics g) {
            super.paintComponent(g);

            Graphics2D g2d = (Graphics2D) g;

            for (Relation r : relations){
                JComponent parent = this.components[mapping.get(r.getSource())];
                JComponent child = this.components[mapping.get(r.getResult())];

                Point2D.Double p1 = getCenter(parent);
                Point2D p2 = getCenter(child);
                g2d.setStroke(new BasicStroke(2f));
                g2d.draw(new Line2D.Double(p1, p2));
            }
        }

        /**
         * Return the center of a swing component
         * @param c component to find
         * @return center
         */
        private Point2D.Double getCenter(Component c) {
            Point2D.Double p = new Point2D.Double();
            Rectangle r = c.getBounds();
            p.x = r.getCenterX();
            p.y = r.getCenterY();
            return p;
        }

        /**
         * @param components JComponents to draw relationships between
         */
        private void setComponents(JComponent[] components){
            this.components = components;
        }

        /**
         * @param mapping of DataSets to their index in the JComponent array
         */
        private void setMapping(Map<DataSet,Integer> mapping){
            this.mapping = mapping;
        }
    }

    /**
     * Wrapper class used to denote locations in the graph map and as a general-purpose tuple. Immutable.
     */
    private class Location{
        private final int x;
        private final int y;

        /**
         * @param x position
         * @param y position
         */
        Location(int x, int y){
            this.x = x;
            this.y = y;
        }

        /**
         * @return x position
         */
        int getX() {
            return x;
        }

        /**
         * @return y position
         */
        int getY() {
            return y;
        }

        /**
         * Equality is determined by X and Y components
         * @param obj to test for equality
         * @return isEqual (value)
         */
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

        /**
         * @return hashCode dependent on values
         */
        @Override
        public int hashCode() {
            return ((31*x)+17)*y;
        }
    }


    private class RoundedPanel extends JPanel{

        private Color borderColor;

        RoundedPanel(GridLayout layout,Color borderColor){
            super(layout);
            this.borderColor = borderColor;
        }

        @Override
        protected void paintComponent(Graphics g) {
            super.paintComponent(g);
            Dimension arcs = new Dimension(ARC,ARC); //Border corners arcs {width,height}, change this to whatever you want
            int width = getWidth();
            int height = getHeight();
            Graphics2D graphics = (Graphics2D) g;
            graphics.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);


            //Draws the rounded panel with borders.
            graphics.setColor(getBackground());
            graphics.fillRoundRect(0, 0, width-1, height-1, arcs.width, arcs.height);//paint background
            graphics.setColor(borderColor);
            graphics.setStroke(new BasicStroke(1.0f));
            graphics.drawRoundRect(0, 0, width-1, height-1, arcs.width, arcs.height);//paint border
        }
    }

}
