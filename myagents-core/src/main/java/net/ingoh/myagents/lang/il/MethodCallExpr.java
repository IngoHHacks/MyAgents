package net.ingoh.myagents.lang.il;

import net.ingoh.myagents.lang.execution.ExecutionSource;
import net.ingoh.myagents.lang.execution.Interpreter;
import net.ingoh.myagents.lang.execution.ReturnVal;
import net.ingoh.myagents.lang.symbols.ClassSymbolFile;
import net.ingoh.myagents.lang.symbols.Symbol;

import java.util.Arrays;

public record MethodCallExpr(Expr target, IdentifierOrSpecial methodName, ExprList args) implements ILNode, Expr {
    public MethodCallExpr {
        if (args == null) {
            args = new ExprList(Arrays.asList());
        }
        if (target == null) {
            throw new IllegalArgumentException("Target cannot be null");
        }
        if (methodName == null) {
            throw new IllegalArgumentException("Method cannot be null");
        }
    }

    @Override
    public Object accept(Interpreter interpreter) {
        var obj = target.accept(interpreter);
        var method = interpreter.resolveMethod(obj, methodName, args);
        var argObjs = args.exprs().stream()
                .map(arg -> arg.accept(interpreter)).map(x -> interpreter.transformVars(interpreter, x))
                .toArray(Object[]::new);
        if (obj instanceof ClassSymbolFile) {
            obj = null;
        }
        var r = method.invoke(interpreter, obj, argObjs);
        if (r instanceof ReturnVal rv) {
            return rv.value;
        }
        return r;
    }
}