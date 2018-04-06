package edu.cmu.cs.cs214.hw5.core;

public class TestPlugin implements DataPlugin{
    @Override
    public String getName() {
        return "Hello from TestPlugin!";
    }

    @Override
    public boolean hasNext() {
        return false;
    }

    @Override
    public ClientPoint getNext() {
        return null;
    }
}
