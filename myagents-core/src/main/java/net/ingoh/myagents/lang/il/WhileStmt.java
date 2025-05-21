package net.ingoh.myagents.lang.il;

import net.ingoh.myagents.lang.execution.Interpreter;
import net.ingoh.myagents.lang.il.BlockStmt;
import net.ingoh.myagents.lang.il.Expr;
import net.ingoh.myagents.lang.il.Stmt;

public record WhileStmt(
        Expr condition,
        BlockStmt body
) implements ILNode, Stmt {

    public WhileStmt {
        if (condition == null) {
            throw new IllegalArgumentException("Condition cannot be null");
        }
        if (body == null) {
            throw new IllegalArgumentException("Body cannot be null");
        }
    }

    @Override
    public Object accept(Interpreter interpreter) {
        while ((Boolean) condition.accept(interpreter)) {
            body.accept(interpreter);
        }
        return null;
    }
}
