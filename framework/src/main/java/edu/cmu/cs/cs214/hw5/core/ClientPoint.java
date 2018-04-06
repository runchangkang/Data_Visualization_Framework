package edu.cmu.cs.cs214.hw5.core;

import java.util.HashMap;
import java.util.Map;

public class ClientPoint {

    private int x;
    private int y;
    private int z;
    private Map<String,Double> attributes;

    public ClientPoint(int x, int y, int z, Map<String,Double> attributes){
        this.x = x;
        this.y = y;
        this.z = z;
        this.attributes = new HashMap<>(attributes);
    }
}
