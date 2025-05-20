package net.ingoh.myagents.lang.il;

import net.ingoh.myagents.lang.execution.Interpreter;

public record LabelIdentifier(String id) implements ILNode, Identifier {
    public LabelIdentifier {
        if (id == null || id.isEmpty()) {
            throw new IllegalArgumentException("Label identifier cannot be null or empty");
        }
    }

    @Override
    public Object accept(Interpreter interpreter) {
        return id;
    }
}
