package edu.cmu.cs.cs214.hw5.core;

import java.util.ArrayList;
import java.util.List;

/**
 * Wrapper class for multiple parameters (see: Parameter)
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
    public List<Parameter> getParameters(){
        return new ArrayList<>(parameterList);
    }
}
