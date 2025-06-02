package net.ingoh.myagents.lang.il;

import net.ingoh.myagents.lang.execution.Interpreter;

import java.util.List;

public final record ArrayLiteralExpr(List<Expr> elements) implements ILNode, LiteralExpr {
    public ArrayLiteralExpr {
        if (elements == null) {
            throw new IllegalArgumentException("Elements cannot be null");
        }
    }

    @Override
    public Object accept(Interpreter interpreter) {
        return interpreter.parseArray(this);
    }
}