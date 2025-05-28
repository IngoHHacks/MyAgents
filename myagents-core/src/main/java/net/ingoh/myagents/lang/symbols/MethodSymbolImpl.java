package net.ingoh.myagents.lang.symbols;

import net.ingoh.myagents.lang.execution.Interpreter;
import net.ingoh.myagents.lang.il.MethodDecl;

import java.util.List;

public class MethodSymbolImpl implements MethodSymbol {
    private final MethodDecl methodDecl;

    public MethodSymbolImpl(MethodDecl methodDecl) {
        this.methodDecl = methodDecl;
    }

    @Override
    public String getName() {
        return methodDecl.id().id();
    }

    @Override
    public List<String> getParameterNames() {
        return methodDecl.params().stream()
                .map(param -> param.id())
                .toList();
    }

    @Override
    public Object invoke(Interpreter interpreter, Object obj, Object... args) {
        return methodDecl.invoke(interpreter, obj, args);
    }
}
