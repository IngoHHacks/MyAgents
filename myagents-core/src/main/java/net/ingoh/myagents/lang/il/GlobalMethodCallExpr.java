package net.ingoh.myagents.lang.il;

import net.ingoh.myagents.lang.execution.Interpreter;

public record GlobalMethodCallExpr(IdentifierOrSpecial methodName, ExprList args) implements ILNode, Expr {
    public GlobalMethodCallExpr {
        if (methodName == null) {
            throw new IllegalArgumentException("Method cannot be null");
        }
        if (args == null) {
            throw new IllegalArgumentException("Arguments cannot be null");
        }
    }

    @Override
    public Object accept(Interpreter interpreter) {
        var method = interpreter.resolveGlobalMethod(methodName, args);
        if (method == null) {
            throw new RuntimeException("Method not found: " + methodName);
        }
        var argObjs = args.exprs().stream()
                .map(arg -> arg.accept(interpreter))
                .toArray(Object[]::new);
        return method.invoke(interpreter, argObjs);
    }
}