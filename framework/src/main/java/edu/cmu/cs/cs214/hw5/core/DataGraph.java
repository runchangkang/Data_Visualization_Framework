package edu.cmu.cs.cs214.hw5.core;

import javax.xml.crypto.Data;
import java.util.*;

/**
 * Overall representation of all of the processing operations in one client session
 */
public class DataGraph {

    private List<DataSet> dataSets = new ArrayList<>();
    private List<Relation> relations = new ArrayList<>();

    private int counter = 0;

    /**
     * Adds a new relationship into the graph
     * @param relation to add
     */
    public void addRelation(Relation relation){
        relations.add(relation);
    }

    public List<Relation> getRelations(){
        return new ArrayList<>(relations);
    }

    public List<DataSet> getDataSets() {
        return new ArrayList<>(dataSets);
    }

    public void addClientSet(Collection<ClientPoint> initialSet){
        DataSet gs = new GeoDataSet(new ArrayList<>(),"dataset" + counter);
        counter++;

        for (ClientPoint cp : initialSet){
            gs.makePoint(cp.getX(),cp.getY(),cp.getT(),cp.getAttr(),cp.getLabel());
        }
        dataSets.add(gs);
    }

    public void addDataSet(DataSet set){
        this.dataSets.add(set);
        counter++;
    }

    public int numParents(DataSet set){
        int i = 0;
        for (Relation r : relations){
            if (set.equals(r.getResult())){
                i++;
            }
        }
        return i;
    }

    public DataSet getParent(DataSet set){
        for (Relation r : relations){
            if (set.equals(r.getResult())){
                return r.getSource();
            }
        }
        throw new IllegalArgumentException("Set doesn't have a parent in this graph!");
    }

    public List<DataSet> getAllParents(DataSet set){
        List<DataSet> parents = new ArrayList<>();

        for (Relation r : relations){
            if (set.equals(r.getResult())){
                parents.add(r.getSource());
            }
        }

        return parents;
    }
}
