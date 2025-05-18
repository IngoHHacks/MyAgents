package net.ingoh.myagents.lang.il;

import java.util.List;

public record ExprList(List<Expr> exprs) implements ILNode, ForInit {
    public ExprList {
        if (exprs == null) {
            throw new IllegalArgumentException("Expressions cannot be null");
        }
    }
}