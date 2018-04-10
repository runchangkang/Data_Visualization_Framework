package edu.cmu.cs.cs214.hw5.core;

import java.util.List;

abstract class Processor {
    abstract DataSet apply(List<DataSet> sources);
}
