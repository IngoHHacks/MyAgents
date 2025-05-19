package net.ingoh.myagents.lang.il;

public record LocalVariableDecl(VariableIdentifier name, Expr expr) implements ILNode, BlockStmt, ForInit {
    public LocalVariableDecl {
        if (name == null) {
            throw new IllegalArgumentException("Field id cannot be null");
        }
        if (expr == null) {
            throw new IllegalArgumentException("Expression cannot be null");
        }
    }
}
