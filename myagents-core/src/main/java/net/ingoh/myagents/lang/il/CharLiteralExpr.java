package net.ingoh.myagents.lang.il;

import net.ingoh.myagents.lang.execution.Interpreter;

public record CharLiteralExpr(char character) implements ILNode, LiteralExpr {
    public CharLiteralExpr {
        // No validation needed for primitive literal values
    }

    @Override
    public Object accept(Interpreter interpreter) {
        return character;
    }
}
