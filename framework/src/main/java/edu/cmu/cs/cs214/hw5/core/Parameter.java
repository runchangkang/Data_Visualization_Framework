package edu.cmu.cs.cs214.hw5.core;

/**
 * Defines an interface parameter as specified by a visualisation plugin.
 * Functions as an (immutable) container class for the framework to read.
 */
class Parameter {

    private final double min;
    private final double max;
    private final String name;

    /**
     * Creates a new parameter slider with a minimum and maximum value and a label
     * @param name label
     * @param min value
     * @param max value
     */
    Parameter(String name, double min, double max){
        this.name = name;
        this.min = min;
        this.max = max;
    }

    /**
     * @return max slider value
     */
    double getMax() {
        return max;
    }

    /**
     * @return min slider value
     */
    double getMin() {
        return min;
    }

    /**
     * @return slider label
     */
    String getName() {
        return name;
    }
}
