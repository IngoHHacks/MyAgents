package net.ingoh.myagents.lang.il;

import net.ingoh.myagents.lang.execution.Interpreter;

import java.util.List;

public record LocalClassDecl(ClassDecl classDecl) implements ILNode, BlockStmt {
    public LocalClassDecl {
        if (classDecl == null) {
            throw new IllegalArgumentException("Class declaration cannot be null");
        }
    }

    @Override
    public Object accept(Interpreter interpreter) {
        interpreter.localClassDecl(this);
        return null;
    }
}
