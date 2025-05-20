package net.ingoh.myagents.lang.il;

import net.ingoh.myagents.lang.execution.Interpreter;

import java.util.Arrays;

public record ExprMethodCallExpr(Expr target, MethodIdentifier methodId, ExprList args) implements ILNode, Expr {
    public ExprMethodCallExpr {
        if (target == null) {
            throw new IllegalArgumentException("Target cannot be null");
        }
        if (methodId == null) {
            throw new IllegalArgumentException("Method id cannot be null");
        }
        if (args == null) {
            throw new IllegalArgumentException("Arguments cannot be null");
        }
    }

    @Override
    public Object accept(Interpreter interpreter) {
        var method = interpreter.resolveMethod(target.accept(interpreter), methodId, args);
        var argObjs = args.exprs().stream()
                .map(arg -> arg.accept(interpreter))
                .toArray(Object[]::new);
        return method.invoke(interpreter, argObjs);
    }
}