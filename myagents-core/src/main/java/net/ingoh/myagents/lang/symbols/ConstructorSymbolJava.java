package net.ingoh.myagents.lang.symbols;

import net.ingoh.myagents.lang.execution.Interpreter;

import java.lang.reflect.Constructor;
import java.lang.reflect.Parameter;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public class ConstructorSymbolJava implements ConstructorSymbol {
    private final Constructor<?> constructor;

    public ConstructorSymbolJava(Constructor<?> constructor) {
        this.constructor = constructor;
    }

    @Override
    public String getName() {
        return constructor.getName();
    }

    @Override
    public List<String> getParameterNames() {
        return Stream.of(constructor.getParameters()).map(Parameter::getName).collect(Collectors.toList());
    }

    @Override
    public int getParameterCount() {
        return constructor.getParameterCount();
    }

    @Override
    public Object invoke(Interpreter interpreter, Object... args) {
        try {
            return constructor.newInstance(args);
        } catch (Exception e) {
            throw new RuntimeException("Failed to invoke constructor: " + getName(), e);
        }
    }
}
