package net.ingoh.myagents.lang.il;

import net.ingoh.myagents.lang.execution.Interpreter;

import java.util.List;
import java.util.Objects;

public record SwitchExpr(
        Expr expr,
        List<Case> cases,
        List<BlockStmt> defaultCase
) implements ILNode, Stmt, Expr {

    public SwitchExpr {
        if (expr == null) {
            throw new IllegalArgumentException("Expression cannot be null");
        }
        if (cases == null || cases.isEmpty()) {
            throw new IllegalArgumentException("Cases cannot be null or empty");
        }
        for (Case c : cases) {
            if (c == null) {
                throw new IllegalArgumentException("Case cannot be null");
            }
        }
    }

    @Override
    public Object accept(Interpreter interpreter) {
        Object value = expr.accept(interpreter);
        for (Case c : cases) {
            if (Objects.equals(c.accept(interpreter), value)) {
                for (BlockStmt block : c.body()) {
                    block.accept(interpreter);
                }
            }
        }
        if (defaultCase != null && !defaultCase.isEmpty()) {
            for (BlockStmt block : defaultCase) {
                block.accept(interpreter);
            }
        }
        return null;
    }
}
