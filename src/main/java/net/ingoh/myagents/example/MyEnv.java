package net.ingoh.myagents.example;

import net.ingoh.myagents.core.Agent;
import net.ingoh.myagents.core.Environment;

import java.util.List;

public class MyEnv extends Environment {
    public List<Agent> agents;

    public MyEnv() {

    }

    @Override
    public boolean tick(float time) {
        super.tick(time);
        System.out.println("a");
        return true;
    }
}
