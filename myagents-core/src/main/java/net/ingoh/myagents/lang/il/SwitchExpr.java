package net.ingoh.myagents.lang.il;

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
}
