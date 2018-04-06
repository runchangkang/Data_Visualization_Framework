package edu.cmu.cs.cs214.hw5.core;

import java.awt.*;

public interface VisualPlugin {

    ParameterList addInterfaceParameters();

    void drawSet(Graphics2D g2d, QueryableSet qSet, int x, int y);
}
