package edu.cmu.cs.cs214.hw5.core.processors;

import edu.cmu.cs.cs214.hw5.core.DataSet;

import java.util.List;

public interface Processor {
    DataSet apply(List<DataSet> sources);
}
