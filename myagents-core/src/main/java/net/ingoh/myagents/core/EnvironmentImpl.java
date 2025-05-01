package net.ingoh.myagents.core;


import java.util.LinkedList;

public class EnvironmentImpl extends Environment {
    public EnvironmentImpl() {
        this.tickRate = 1.0f;
        this.agents = new LinkedList<>();
    }

    @Override
    public boolean tick(float dt) {
        return true;
    }
}
