package net.ingoh.myagents.core;

import java.util.ArrayList;

public class EnvironmentImpl extends Environment {
    public EnvironmentImpl() {
        this.tickRate = 1.0f;
        this.agents = new ArrayList<>();
    }

    @Override
    public boolean tick(float dt) {
        return true;
    }
}
