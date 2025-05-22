package net.ingoh.myagents.lang.il;

import net.ingoh.myagents.lang.execution.Interpreter;
import net.ingoh.myagents.lang.il.Expr;
import net.ingoh.myagents.lang.il.Stmt;

public record ExprStmt(
        Expr expr
) implements ILNode, Stmt {
    public ExprStmt {
        if (expr == null) {
            throw new IllegalArgumentException("Expression cannot be null");
        }
    }

    @Override
    public Object accept(Interpreter interpreter) {
        return expr.accept(interpreter);
    }
}
