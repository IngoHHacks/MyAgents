package net.ingoh.myagents.lang.il;

import net.ingoh.myagents.lang.execution.Interpreter;

public record AnyVariableIdentifier(String id) implements ILNode, VariableIdentifier {
    public AnyVariableIdentifier {
        if (id == null) {
            throw new IllegalArgumentException("Variable id cannot be null");
        }
    }

    @Override
    public Object accept(Interpreter interpreter) {
        return id;
    }
}