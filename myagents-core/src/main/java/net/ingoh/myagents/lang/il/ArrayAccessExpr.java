package net.ingoh.myagents.lang.il;

public record ArrayAccessExpr(Expr array, Expr index) implements ILNode, Expr {
    public ArrayAccessExpr {
        if (array == null || index == null) {
            throw new IllegalArgumentException("Array and index cannot be null");
        }
    }
}