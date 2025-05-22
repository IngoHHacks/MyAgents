package net.ingoh.myagents.lang.il;

import net.ingoh.myagents.lang.execution.Interpreter;

import java.util.List;

public record OverrideBodyDecl(List<ClassBodyDecl> bodyDecls, String type, String name) implements ILNode, TopLevelDecl {
    public OverrideBodyDecl {
        if (bodyDecls == null) {
            throw new IllegalArgumentException("Override body declarations cannot be null or empty");
        }
    }

    @Override
    public Object accept(Interpreter interpreter) {
        interpreter.declsFromOverrideBody(this);
        return null;
    }
}
