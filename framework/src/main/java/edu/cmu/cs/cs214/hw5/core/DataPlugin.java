package edu.cmu.cs.cs214.hw5.core;

public interface DataPlugin {

    String getName();

    boolean hasNext();

    ClientPoint getNext();

}
