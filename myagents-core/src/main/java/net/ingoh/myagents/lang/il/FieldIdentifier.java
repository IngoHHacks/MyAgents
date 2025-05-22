package net.ingoh.myagents.lang.il;

import net.ingoh.myagents.lang.execution.Interpreter;

public record FieldIdentifier(String id) implements ILNode, VariableIdentifier, MemberIdentifier {
    public FieldIdentifier {
        if (id == null || id.isEmpty()) {
            throw new IllegalArgumentException("Variable id cannot be null or empty");
        }
    }

    @Override
    public Object accept(Interpreter interpreter) {
        return id;
    }
}