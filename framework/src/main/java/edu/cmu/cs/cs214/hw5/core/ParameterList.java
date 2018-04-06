package edu.cmu.cs.cs214.hw5.core;

import java.util.ArrayList;
import java.util.List;

public class ParameterList {

    List<Parameter> parameterList = new ArrayList<>();

    public void addParameter(String name, int min, int max){
        parameterList.add(new Parameter(name,min,max));
    }

    List<Parameter> getParameters(){
        return parameterList;
    }
}
