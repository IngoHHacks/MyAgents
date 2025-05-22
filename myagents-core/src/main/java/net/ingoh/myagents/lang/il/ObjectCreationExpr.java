package net.ingoh.myagents.lang.il;

import net.ingoh.myagents.lang.execution.Interpreter;
import net.ingoh.myagents.lang.symbols.VariableSymbol;
import net.ingoh.myagents.lang.symbols.VariableSymbolImpl;

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

    @Override
    public Object accept(Interpreter interpreter) {
        var constructor = interpreter.resolveConstructor(type, args);
        var argObjs = args.exprs().stream()
                .map(arg -> arg.accept(interpreter))
                .toList();
        var obj = constructor.invoke(interpreter, argObjs);
        if (classBody != null && classBody.size() > 0)
        {
            throw new UnsupportedOperationException("Class body is not supported yet");
        }
        return obj;
    }
}