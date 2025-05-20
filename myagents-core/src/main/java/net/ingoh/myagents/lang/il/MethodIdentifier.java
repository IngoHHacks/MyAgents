package net.ingoh.myagents.lang.il;

import net.ingoh.myagents.lang.execution.Interpreter;

public record MethodIdentifier(String id) implements ILNode, MemberIdentifier {
    public MethodIdentifier {
        if (id == null || id.isEmpty()) {
            throw new IllegalArgumentException("Method id cannot be null or empty");
        }
    }

    @Override
    public Object accept(Interpreter interpreter) {
        return id;
    }
}