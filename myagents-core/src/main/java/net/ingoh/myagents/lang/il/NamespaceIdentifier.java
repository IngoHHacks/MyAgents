package net.ingoh.myagents.lang.il;

import net.ingoh.myagents.lang.execution.Interpreter;

public record NamespaceIdentifier(String id) implements ILNode, Identifier {

    public NamespaceIdentifier {
        if (id == null) {
            throw new IllegalArgumentException("Namespace id cannot be null");
        }
    }

    @Override
    public Object accept(Interpreter interpreter) {
        return id;
    }
}
