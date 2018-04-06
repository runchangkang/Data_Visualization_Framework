package edu.cmu.cs.cs214.hw5.core;

import java.util.List;

/**
 * Overall representation of all of the processing operations in one client session
 */
public class DataGraph {
    List<Relation> relations;

    /**
     * Adds a new relationship into the graph
     * @param relation to add
     */
    public void addRelation(Relation relation){
        relations.add(relation);
    }
}
