package net.ingoh.myagents.lang.il;

import net.ingoh.myagents.lang.execution.Interpreter;

public record IfStmt(
        Expr condition,
        Stmt thenStmt,
        Stmt elseStmt
) implements ILNode, Stmt {
    public IfStmt {
        if (condition == null) {
            throw new IllegalArgumentException("Condition cannot be null");
        }
        if (thenStmt == null) {
            throw new IllegalArgumentException("Then statement cannot be null");
        }
    }

    @Override
    public Object accept(Interpreter interpreter) {
        var result = condition.accept(interpreter);
        if (result instanceof Boolean) {
            if ((Boolean) result) {
                return thenStmt.accept(interpreter);
            } else if (elseStmt != null) {
                return elseStmt.accept(interpreter);
            }
            return null; // No else statement
        } else {
            throw new IllegalArgumentException("Condition must evaluate to a boolean");
        }
    }
}
