package net.ingoh.myagents.lang.il;

import java.util.List;

public class EnvironmentIL implements ILNode {
    public final String TYPE = "Environment";
    public String name;
    public Float tickRate;
    public List<String> agents;
    public MethodIL tickMethod;
}
