package edu.cmu.cs.cs214.hw5.core;

import edu.cmu.cs.cs214.hw5.core.processors.Processor;

/**
 * Establishes a relationship between one (or more) DataSets, a data processing operation, and its result.
 */
public class Relation {
    private DataSet source;
    private DataSet target;
    private Processor processor;

    /**
     * Instantiates the relationship within the DataGraph model.
     * @param source DataSets to apply processing to
     * @param processor to apply processing with
     */
    public Relation(DataSet source, Processor processor, DataSet target){
        this.source = source;
        this.processor = processor;
        this.target = target;
        //System.out.println(target.getName()+" LEN:" + target.getDataPoints().size());
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
    public DataSet getSource() {
        return source;
    }
}
