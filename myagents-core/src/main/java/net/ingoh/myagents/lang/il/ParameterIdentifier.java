package net.ingoh.myagents.lang.il;

import net.ingoh.myagents.lang.execution.Interpreter;

public record ParameterIdentifier(String id) implements ILNode, VariableIdentifier {
    public ParameterIdentifier {
        if (id == null || id.isEmpty()) {
            throw new IllegalArgumentException("Parameter id cannot be null or empty");
        }
    }

    @Override
    public Object accept(Interpreter interpreter) {
        return id;
    }
}