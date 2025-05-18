package net.ingoh.myagents.lang.il;

import net.ingoh.myagents.lang.il.Case;
import net.ingoh.myagents.lang.il.Expr;
import net.ingoh.myagents.lang.il.Stmt;

import java.util.List;
import java.util.Objects;

public record SwitchStmt (
        Expr expr,
        List<Case> cases,
        List<BlockStmt> defaultBody
) implements ILNode, Stmt {
    public SwitchStmt {
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
