package net.ingoh.myagents.lang.il;

import net.ingoh.myagents.lang.execution.Interpreter;

import java.util.List;

public record ClassDecl(TypeIdentifier id, List<ClassBodyDecl> body) implements ILNode, TopLevelDecl, MemberDecl {
    public ClassDecl {
        if (id == null) {
            throw new IllegalArgumentException("Class id cannot be null");
        }
        if (body == null) {
            throw new IllegalArgumentException("Class body cannot be null");
        }
    }

    @Override
    public Object accept(Interpreter interpreter) {
        interpreter.classDecl(this);
        return null;
    }
}
