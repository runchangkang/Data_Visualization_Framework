package edu.cmu.cs.cs214.hw5.core;

import edu.cmu.cs.cs214.hw5.core.processors.Filter;

import java.util.*;

/**
 * Overall representation of all of the processing operations in one client session
 */
public class DataGraph {
    List<Relation> relations = new ArrayList<>();

    /**
     * Adds a new relationship into the graph
     * @param relation to add
     */
    public void addRelation(Relation relation){
        relations.add(relation);
    }

    public List<Relation> getRelations(){
        return relations;
    }

    public void addDataSet(Collection<ClientPoint> initialSet){
        DataSet gs = new GeoDataSet(new ArrayList<>(),"ds0");

        for (ClientPoint cp : initialSet){
            gs.makePoint(cp.getX(),cp.getY(),cp.getT(),cp.getAttr());
        }

        List<DataSet> setList = new ArrayList<>();
        setList.add(gs);
        this.relations.add(new Relation(setList,new Filter(new HashMap<>())));
    }
}
