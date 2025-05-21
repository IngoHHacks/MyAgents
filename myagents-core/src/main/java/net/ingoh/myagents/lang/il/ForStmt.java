package net.ingoh.myagents.lang.il;

import net.ingoh.myagents.lang.execution.Interpreter;

import java.util.List;

public record ForStmt(
        ForInit init,
        Expr condition,
        ExprList update,
        Stmt body
) implements ILNode, Stmt {
    public ForStmt {
        if (init == null) {
            throw new IllegalArgumentException("Initialization cannot be null");
        }
        if (condition == null) {
            throw new IllegalArgumentException("Condition cannot be null");
        }
        if (update == null) {
            throw new IllegalArgumentException("Update cannot be null");
        }
        if (body == null) {
            throw new IllegalArgumentException("Body cannot be null");
        }
    }

    @Override
    public Object accept(Interpreter interpreter) {
        init.accept(interpreter);

        while ((Boolean) condition.accept(interpreter)) {
            body.accept(interpreter);

            for (Expr expr : update.exprs()) {
                expr.accept(interpreter);
            }
        }

        return null;
    }
}
