package net.ingoh.myagents.lang.il;

import net.ingoh.myagents.lang.execution.Interpreter;

import java.util.List;

public record ConstructorDecl(TypeIdentifier type, List<ParameterIdentifier> params, Block body) implements ILNode, MemberDecl {
    public ConstructorDecl {
        if (body == null) {
            body = new Block(List.of());
        }
        if (params == null) {
            throw new IllegalArgumentException("Parameters cannot be null");
        }
    }

    @Override
    public Object accept(Interpreter interpreter) {
        interpreter.constructorDecl(this);
        return null;
    }
}
