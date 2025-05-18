package net.ingoh.myagents.lang.il;

public record CharLiteralExpr(char character) implements ILNode, LiteralExpr {
    public CharLiteralExpr {
        // No validation needed for primitive literal values
    }
}
