package net.ingoh.myagents.lang.il;

import net.ingoh.myagents.lang.execution.Interpreter;

public record LocalVariableDecl(LocalVariableIdentifier id, Expr expr) implements ILNode, BlockStmt, ForInit {
    public LocalVariableDecl {
        if (id == null) {
            throw new IllegalArgumentException("Field id cannot be null");
        }
        if (expr == null) {
            throw new IllegalArgumentException("Expression cannot be null");
        }
    }

    @Override
    public Object accept(Interpreter interpreter) {
        interpreter.localVariableDecl(this);
        return null;
    }
}
