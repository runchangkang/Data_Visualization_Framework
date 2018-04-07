package edu.cmu.cs.cs214.hw5.core;

import edu.cmu.cs.cs214.hw5.core.processors.Processor;

import java.util.ArrayList;
import java.util.List;

/**
 * Establishes a relationship between one (or more) DataSets, a data processing operation, and its result.
 */
public class Relation {
    private List<DataSet> sources;
    private DataSet target;
    private Processor processor;

    /**
     * Instantiates the relationship and applies the data processing.
     * @param sources DataSets to apply processing to
     * @param processor to apply processing with
     */
    public Relation(List<DataSet> sources, Processor processor){
        this.sources = new ArrayList<>(sources);
        this.processor = processor;
        this.target = processor.apply(this.sources);
        target.printSet();
    }

    /**
     * @return the result of applying the data processing operation
     */
    public DataSet getResult() {
        return target;
    }

    /**
     * @return the sources used in the data processing operation
     */
    public List<DataSet> getSources() {
        return new ArrayList<>(sources);
    }
}
