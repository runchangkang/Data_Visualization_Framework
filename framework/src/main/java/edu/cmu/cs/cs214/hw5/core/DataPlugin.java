package edu.cmu.cs.cs214.hw5.core;

import java.util.List;

public interface DataPlugin {

    String getName();

    boolean hasNext();

    ClientPoint getNext();

    List<String> getPopupParameters();

}
