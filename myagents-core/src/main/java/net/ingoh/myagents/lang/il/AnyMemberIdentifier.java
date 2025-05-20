package net.ingoh.myagents.lang.il;

import net.ingoh.myagents.lang.execution.Interpreter;

public record AnyMemberIdentifier(String id) implements ILNode, MemberIdentifier {
    public AnyMemberIdentifier {
        if (id == null) {
            throw new IllegalArgumentException("Member id cannot be null");
        }
    }

    @Override
    public Object accept(Interpreter interpreter) {
        return id;
    }
}