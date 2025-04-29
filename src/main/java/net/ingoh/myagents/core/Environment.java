package net.ingoh.myagents.core;

import java.util.List;

public abstract class Environment {

    public float tickRate;
    public List<Class<?>> agents;

    public boolean tick(float time) {
        return true;
    }
}
