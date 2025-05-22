package net.ingoh.myagents.lang.il;

import net.ingoh.myagents.lang.execution.Interpreter;

import java.util.List;

public record MethodDecl(MethodIdentifier id, List<ParameterIdentifier> params, Block body) implements ILNode, MemberDecl {
    public MethodDecl {
        if (id == null) {
            throw new IllegalArgumentException("Method id cannot be null");
        }
        if (params == null) {
            throw new IllegalArgumentException("Parameters cannot be null");
        }
    }

    @Override
    public Object accept(Interpreter interpreter) {
        interpreter.methodDecl(this);
        return null;
    }

    public Object invoke(Interpreter interpreter, Object... args) {
        return interpreter.invokeMethod(this, args);
    }
}
