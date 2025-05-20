package net.ingoh.myagents.lang.il;

import net.ingoh.myagents.lang.execution.Interpreter;

public record IntLiteralExpr(int value) implements ILNode, LiteralExpr {
    public IntLiteralExpr {
        // No validation needed for primitive literal values
    }

    @Override
    public Object accept(Interpreter interpreter) {
        return value;
    }
}
