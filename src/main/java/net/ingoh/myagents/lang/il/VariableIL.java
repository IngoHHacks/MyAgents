package net.ingoh.myagents.lang.il;

public class VariableIL extends ExpressionIL {
    public String name;
    public String type;

    public VariableIL() {
        this.name = "";
        this.type = "";
    }

    public VariableIL(String name, String type) {
        this.name = name;
        this.type = type;
    }
}
