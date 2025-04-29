package net.ingoh.myagents.core;

public class EnvironmentImpl extends Environment {
    public EnvironmentImpl() {
        this.tickRate = 1.0f;
        this.agents = new java.util.ArrayList<>();
    }

    @Override
    public boolean tick(float dt) {
        return true;
    }
}
