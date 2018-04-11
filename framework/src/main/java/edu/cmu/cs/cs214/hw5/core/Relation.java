package edu.cmu.cs.cs214.hw5.core;

/**
 * Establishes a relationship between one (or more) DataSets, a data processing operation, and its result.
 */
class Relation {
    private DataSet source;
    private DataSet target;

    /**
     * Instantiates the relationship within the DataGraph model.
     * @param source DataSets to apply processing to
     * @param target Set with processing applied
     */
    Relation(DataSet source, DataSet target){
        this.source = source;
        this.target = target;
    }

    /**
     * @return the result of applying the data processing operation
     */
    DataSet getResult() {
        return target;
    }

    /**
     * @return the sources used in the data processing operation
     */
    DataSet getSource() {
        return source;
    }
}
