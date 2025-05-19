package net.ingoh.myagents.lang.il;

import net.ingoh.myagents.lang.execution.Interpreter;

public record FieldDecl(FieldIdentifier id, Expr expr) implements ILNode, MemberDecl {
    public FieldDecl {
        if (id == null) {
            throw new IllegalArgumentException("Field id cannot be null");
        }
        if (expr == null) {
            throw new IllegalArgumentException("Expression cannot be null");
        }
    }

    @Override
    public Object accept(Interpreter interpreter) {
        interpreter.fieldDecl(this);
        return null;
    }
}
