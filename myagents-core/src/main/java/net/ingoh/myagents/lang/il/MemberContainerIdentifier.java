package net.ingoh.myagents.lang.il;

import net.ingoh.myagents.lang.execution.Interpreter;

public record MemberContainerIdentifier(String id) implements ILNode, Identifier {
    public MemberContainerIdentifier {
        if (id == null) {
            throw new IllegalArgumentException("Id cannot be null");
        }
    }

    @Override
    public Object accept(Interpreter interpreter) {
        return id;
    }
}
