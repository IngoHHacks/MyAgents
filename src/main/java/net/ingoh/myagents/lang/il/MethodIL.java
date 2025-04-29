package net.ingoh.myagents.lang.il;

import java.util.List;

public class MethodIL implements ILNode {
    public String name;
    public String returnType;
    public List<ParameterIL> parameters;
    public List<StatementIL> statements;
}
