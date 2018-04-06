package edu.cmu.cs.cs214.hw5.core;

import javax.swing.*;

public interface VisualPlugin {

    String getName();

    ParameterList addInterfaceParameters();

    JPanel drawSet(QueryableSet qSet, int x, int y);
}
