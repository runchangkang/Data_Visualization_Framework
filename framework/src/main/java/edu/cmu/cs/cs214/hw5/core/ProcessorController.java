package edu.cmu.cs.cs214.hw5.core;

import javax.swing.BorderFactory;
import javax.swing.JButton;
import javax.swing.JDialog;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.border.EmptyBorder;
import java.awt.BorderLayout;
import java.awt.GridLayout;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Controller class for data processing dialogs and operations
 */
class ProcessorController {

    private ControlPanel cp;
    private JFrame frame;

    ProcessorController(ControlPanel cp, JFrame frame){
        this.cp = cp;
        this.frame = frame;
    }

    /**
     * Frames initial popup of join dialog
     * @param dataSet that triggered the join operation
     * @param graph being joined
     */
    void joinDialog(DataSet dataSet, DataGraph graph){
        final JDialog dialog = new JDialog(frame, "Join Selection", true);
        List<DataSet> joinList = new ArrayList<>(Collections.singletonList(dataSet));
        joinRefresh(dialog,joinList,dataSet,graph);
    }

    /**
     * Refreshable dialog of join operation (Recalled per joinList update)
     * @param dialog to refresh
     * @param joinList list of dataSets being joined
     * @param dataSet set that triggered the join operation
     * @param graph being joined
     */
    private void joinRefresh(JDialog dialog, List<DataSet> joinList, DataSet dataSet, DataGraph graph){
        dialog.repaint();

        JPanel optionPanel = new JPanel(new GridLayout(0,1));
        optionPanel.add(new JLabel("  Select DataSets to join with:  "));

        for (DataSet set : graph.getDataSets()){
            if (set != dataSet){
                JButton optionButton = new JButton(set.getName());
                optionButton.addActionListener(e ->{
                    if (joinList.contains(set)){
                        joinList.remove(set);
                        //System.out.println("REM");
                        //System.out.println(joinList);
                        joinRefresh(dialog,joinList,dataSet,graph);
                    }
                    else{
                        joinList.add(set);
                        //System.out.println("ADD");
                        //System.out.println(joinList);
                        joinRefresh(dialog,joinList,dataSet,graph);
                    }
                });
                optionPanel.add(optionButton);
            }
        }

        JPanel selectedOptions = new JPanel(new GridLayout(0,1));

        selectedOptions.add(new JLabel("SELECTED: "));
        for (DataSet set : joinList){
            if (set != dataSet) {
                selectedOptions.add(new JLabel(set.getName()));
            }
        }

        JButton closeButton = new JButton("JOIN");
        closeButton.addActionListener(e -> {
            if(joinList.size() > 1) {
                Join joiner = new Join();
                DataSet joined = joiner.apply(joinList);
                graph.addDataSet(joined);

                for (DataSet set : joinList) {
                    graph.addRelation(new Relation(set,joiner,joined));
                }

                dialog.setVisible(false);
                cp.addStartScreen();
            }
            else{
                JOptionPane.showMessageDialog(dialog, "Need at least one other set to join with.");
            }
        });

        selectedOptions.setBorder(new EmptyBorder(0,20,50,0));
        optionPanel.add(selectedOptions);
        optionPanel.add(closeButton);
        optionPanel.setBorder(BorderFactory.createEmptyBorder(20,20,20,20));
        ControlPanel.display(dialog,optionPanel,frame);
    }

    //Todo: extract these to an outer function
    /**
     * Filtering interface popup
     * @param dataSet being filtered
     * @param graph to create new set on
     */
    void filterDialog(DataSet dataSet, DataGraph graph){
        final JDialog dialog = new JDialog(frame, "Define Filter Expressions", true);
        JPanel optionPanel = new JPanel(new BorderLayout());

        Map<String,String> argMap = new HashMap<>();
        List<String> defaultSet = new ArrayList<>(Arrays.asList(DataPoint.X_ATTRIB,DataPoint.Y_ATTRIB,DataPoint.T_ATTRIB));
        defaultSet.addAll(dataSet.getAttributes());

        optionPanel.add(ControlPanel.paramFieldSet(defaultSet,argMap,dataSet),BorderLayout.CENTER);

        JButton closeButton = new JButton("APPLY");

        //todo: move the graph application logic entirely into datagraph class
        closeButton.addActionListener(e -> {
            try {
                Filter f = new Filter(argMap);
                List<DataSet> dsList = new ArrayList<>(Collections.singletonList(dataSet));
                DataSet filtered = f.apply(dsList);
                graph.addDataSet(filtered);
                graph.addRelation(new Relation(dataSet,f,filtered));
                dialog.setVisible(false);
                cp.addStartScreen();
            }
            catch (Exception e2){
                e2.printStackTrace();
                JOptionPane.showMessageDialog(dialog, "An error occurred while filtering data. Please try again.");
            }
        });

        optionPanel.add(closeButton,BorderLayout.SOUTH);
        optionPanel.setBorder(BorderFactory.createEmptyBorder(20,20,20,20));
        ControlPanel.display(dialog,optionPanel,frame);
    }

    /**
     * Transformation interface popup
     * @param dataSet to transform
     * @param graph to create new set on
     */
    void transformDialog(DataSet dataSet,DataGraph graph){
        final JDialog dialog = new JDialog(frame, "Define Transform Expressions", true);
        JPanel optionPanel = new JPanel(new BorderLayout());
        Map<String,String> argMap = new HashMap<>();
        optionPanel.add(ControlPanel.paramFieldSet(dataSet.getAttributes(),argMap,null),BorderLayout.CENTER);

        JButton closeButton = new JButton("APPLY");

        closeButton.addActionListener(e -> {
            try {
                Transform transform = new Transform(argMap);
                List<DataSet> dsList = new ArrayList<>(Collections.singletonList(dataSet));
                DataSet transformed = transform.apply(dsList);
                graph.addDataSet(transformed);
                graph.addRelation(new Relation(dataSet,transform,transformed));
                dialog.setVisible(false);
                cp.addStartScreen();
            }
            catch (Exception e2){
                e2.printStackTrace();
                JOptionPane.showMessageDialog(dialog, "An error occurred while applying the transform expression.");
            }
        });
        optionPanel.add(closeButton,BorderLayout.SOUTH);
        optionPanel.setBorder(BorderFactory.createEmptyBorder(20,20,20,20));
        ControlPanel.display(dialog,optionPanel,frame);
    }

}
