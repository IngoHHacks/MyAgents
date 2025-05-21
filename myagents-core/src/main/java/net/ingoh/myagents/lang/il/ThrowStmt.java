package net.ingoh.myagents.lang.il;

import net.ingoh.myagents.lang.execution.Interpreter;
import net.ingoh.myagents.lang.il.Expr;
import net.ingoh.myagents.lang.il.Stmt;

public record ThrowStmt(
        Expr exception
) implements ILNode, Stmt {
    public ThrowStmt {
        if (exception == null) {
            throw new IllegalArgumentException("Exception cannot be null");
        }
    }

    @Override
    public Object accept(Interpreter interpreter) {
        throw new RuntimeException((String) exception.accept(interpreter));
    }
}
