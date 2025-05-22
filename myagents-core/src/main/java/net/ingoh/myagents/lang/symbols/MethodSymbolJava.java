package net.ingoh.myagents.lang.symbols;

import net.ingoh.myagents.lang.execution.Interpreter;

import java.lang.reflect.Method;
import java.lang.reflect.Parameter;
import java.util.List;
import java.util.stream.Stream;

public class MethodSymbolJava implements MethodSymbol {
    private final Method src;
    private final Object obj;

    public MethodSymbolJava(Method src) {
        this.src = src;
        this.obj = null;
    }

    public MethodSymbolJava(Method src, Object obj) {
        this.src = src;
        this.obj = obj;
    }

    @Override
    public String getName() {
        return src.getName();
    }

    @Override
    public List<String> getParameterNames() {
        return Stream.of(src.getParameters())
                .map(Parameter::getName)
                .toList();
    }

    @Override
    public Object invoke(Interpreter interpreter, Object... args) {
        try {
            return src.invoke(obj != null ? obj : interpreter.getExecutionSource().getSource(), args);
        } catch (Exception e) {
            throw new RuntimeException("Failed to invoke method: " + getName(), e);
        }
    }
}
