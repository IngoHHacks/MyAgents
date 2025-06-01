package net.ingoh.myagents.lang.symbols;

import net.ingoh.myagents.lang.execution.Interpreter;
import java.util.function.Function;

import java.util.List;

public class LambdaMethodSymbol implements MethodSymbol {

    private final String name;
    private final Function<Object[], Object> function;

    public LambdaMethodSymbol(String name, Function<Object[], Object> function) {
        this.name = name;
        this.function = function;
    }

    @Override
    public Object invoke(Interpreter interpreter, Object obj, Object... args) {
        return function.apply(args);
    }

    @Override
    public String getName() {
        return name;
    }
}
