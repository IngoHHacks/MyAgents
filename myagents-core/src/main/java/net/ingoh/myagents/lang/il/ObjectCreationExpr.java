package net.ingoh.myagents.lang.il;

import java.util.List;

public record ObjectCreationExpr(
        TypeIdentifier type,
        ExprList args,
        List<ClassBodyDecl> classBody
) implements ILNode, Expr {
    public ObjectCreationExpr {
        if (type == null) {
            throw new IllegalArgumentException("Type cannot be null");
        }
        if (args == null) {
            throw new IllegalArgumentException("Arguments cannot be null");
        }
    }
}