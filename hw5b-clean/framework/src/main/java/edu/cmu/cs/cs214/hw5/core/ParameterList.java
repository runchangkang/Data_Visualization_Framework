package edu.cmu.cs.cs214.hw5.core;

import java.util.ArrayList;
import java.util.List;

/**
 * A list of parameters created by Visualisation Plugins for the framework to display as sliders.
 * A ParameterList is instantiated and then arbitrarily many parameters are added as desired.
 */
public class ParameterList {

    private List<Parameter> parameterList = new ArrayList<>();

    /**
     * Adds a new parameter to the list. Framework takes care of display, value, and init.
     * @param name label
     * @param min value of parameter
     * @param max value of parameter
     */
    public void addParameter(String name, double min, double max){
        parameterList.add(new Parameter(name,min,max));
    }

    /**
     * @return snapshot of currently instantiated parameters
     */
    List<Parameter> getParameters(){
        return new ArrayList<>(parameterList);
    }
}
