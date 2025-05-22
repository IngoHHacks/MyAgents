package net.ingoh.myagents.lang.il;

import net.ingoh.myagents.lang.execution.Interpreter;

import java.util.Arrays;

public record MethodCallExpr(Expr target, IdentifierOrSpecial methodName, ExprList args) implements ILNode, Expr {
    public MethodCallExpr {
        if (target == null) {
            throw new IllegalArgumentException("Target cannot be null");
        }
        if (methodName == null) {
            throw new IllegalArgumentException("Method cannot be null");
        }
    }

    @Override
    public Object accept(Interpreter interpreter) {
        var method = interpreter.resolveMethod(target.accept(interpreter), methodName, args);
        var argObjs = args.exprs().stream()
                .map(arg -> arg.accept(interpreter))
                .toArray(Object[]::new);
        return method.invoke(interpreter, argObjs);
    }
}