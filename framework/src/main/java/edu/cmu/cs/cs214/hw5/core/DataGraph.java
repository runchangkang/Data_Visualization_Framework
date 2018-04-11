package edu.cmu.cs.cs214.hw5.core;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

/**
 * Overall representation of all of the processing operations in one client session
 */
class DataGraph {

    private List<DataSet> dataSets = new ArrayList<>();
    private List<Relation> relations = new ArrayList<>();

    /**
     * Adds a new relationship between two dataSets into the graph
     * @param relation to add
     */
    void addRelation(Relation relation){
        relations.add(relation);
    }

    /**
     * @return all of the relations in the DataGraph
     */
    List<Relation> getRelations(){
        return new ArrayList<>(relations);
    }

    /**
     * @return all of the datasets current in the datagraph.
     */
    List<DataSet> getDataSets() {
        return new ArrayList<>(dataSets);
    }

    /**
     * Constructs a DataSet object from an incoming collection of Client Points as provided by the plugin.
     * Subsequently adds the set to the graph, but it is not yet part of any relations.
     *
     * @param initialSet collection of client points to initialise into a set
     * @param name of the set, if any.
     */
    void addClientSet(Collection<ClientPoint> initialSet, String name){
        DataSet gs = new GeoDataSet(new ArrayList<>(),name);

        for (ClientPoint cp : initialSet){
            gs.makePoint(cp.getX(),cp.getY(),cp.getT(),cp.getAttr(),cp.getLabel());
        }
        dataSets.add(gs);
    }

    /**
     * Adds a DataSet to the graph manually.
     * @param set to add
     */
    void addDataSet(DataSet set){
        this.dataSets.add(set);
    }

    /**
     * Number of all of the relations that have this dataset as a 'child' (result of the relation's processing) on
     * some other data set.
     * @param set to find parents of
     * @return number of parents
     */
    int numParents(DataSet set){
        int i = 0;
        for (Relation r : relations){
            if (set.equals(r.getResult())){
                i++;
            }
        }
        return i;
    }

    /**
     * Returns an arbitrary parent of a DataSet (a set that has a relation to this set through a processing operation
     * performed on it). This is best used when there is only one parent.
     * @param set to get parent of
     * @return parent
     */
    DataSet getParent(DataSet set){
        for (Relation r : relations){
            if (set.equals(r.getResult())){
                return r.getSource();
            }
        }
        throw new IllegalArgumentException("Set doesn't have a parent in this graph!");
    }

    /**
     * Return all parents of the DataSet in question (best used when there are multiple parents).
     * @param set to get the parents of
     * @return collection of parents
     */
    List<DataSet> getAllParents(DataSet set){
        List<DataSet> parents = new ArrayList<>();

        for (Relation r : relations){
            if (set.equals(r.getResult())){
                parents.add(r.getSource());
            }
        }
        return parents;
    }
}
