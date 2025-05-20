package net.ingoh.myagents.lang.il;

import net.ingoh.myagents.lang.execution.Interpreter;

public record TypeIdentifier(String id) implements ILNode, MemberIdentifier {
    public TypeIdentifier {
        if (id == null || id.isEmpty()) {
            throw new IllegalArgumentException("Type id cannot be null or empty");
        }
    }

    @Override
    public Object accept(Interpreter interpreter) {
        return id;
    }

    public NamespaceIdentifier getNamespace() {
        return new NamespaceIdentifier(id.substring(0, id.lastIndexOf('.')));
    }
}