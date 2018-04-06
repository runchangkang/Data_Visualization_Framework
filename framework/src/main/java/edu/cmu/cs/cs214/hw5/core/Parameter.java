package edu.cmu.cs.cs214.hw5.core;

public class Parameter {

    int min;
    int max;
    String name;

    Parameter(String name, int min, int max){
        this.name = name;
        this.min = min;
        this.max = max;
    }

    public int getMax() {
        return max;
    }

    public int getMin() {
        return min;
    }

    public String getName() {
        return name;
    }
}
