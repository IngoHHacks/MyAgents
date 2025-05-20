package net.ingoh.myagents.lang.symbols;

import net.ingoh.myagents.lang.execution.Interpreter;
import net.ingoh.myagents.lang.il.ConstructorDecl;

import java.util.List;

public class ConstructorSymbolImpl implements ConstructorSymbol {
    private final ConstructorDecl constructorDecl;
    private final int size;

    public ConstructorSymbolImpl(ConstructorDecl constructorDecl) {
        this.constructorDecl = constructorDecl;
        this.size = constructorDecl.params().size();
    }

    @Override
    public String getName() {
        return String.join(",", getParameterNames());
    }

    @Override
    public List<String> getParameterNames() {
        return constructorDecl.params().stream().map(s -> s.id()).toList();
    }

    @Override
    public int getParameterCount() {
        return size;
    }

    @Override
    public Object invoke(Interpreter interpreter, Object... args) {
        var type = interpreter.resolveFile(constructorDecl.type().getNamespace());
        return type.newInstance(interpreter, constructorDecl, args);
    }
}
