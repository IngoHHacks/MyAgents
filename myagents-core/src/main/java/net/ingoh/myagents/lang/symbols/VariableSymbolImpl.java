package net.ingoh.myagents.lang.symbols;

import net.ingoh.myagents.lang.execution.Interpreter;
import net.ingoh.myagents.lang.il.FieldDecl;

public class VariableSymbolImpl implements VariableSymbol {
    private final String name;
    private Object value;

    public VariableSymbolImpl(Interpreter interpreter, String name, Object value) {
        this.name = name;
        while (value instanceof VariableSymbol) {
            value = ((VariableSymbol) value).getValue(interpreter);
        }
        this.value = value;
    }

    @Override
    public String getName() {
        return name;
    }

    @Override
    public Object getValue(Interpreter interpreter) {
        return value;
    }

    @Override
    public <T> T getValue(Interpreter interpreter, Class<T> type) {
        if (value == null) {
            return null;
        }
        try {
            return type.cast(value);
        } catch (ClassCastException e) {
            throw new IllegalArgumentException("Cannot cast value to " + type.getName(), e);
        }
    }

    @Override
    public void setValue(Interpreter interpreter, Object value) {
        while (value instanceof VariableSymbol) {
            value = ((VariableSymbol) value).getValue(interpreter);
        }
        this.value = value;
    }

    @Override
    public void changeValueBy(Interpreter interpreter, Number value) {
        var thisNum = getValue(interpreter, Number.class);
        if (thisNum instanceof Integer || thisNum instanceof Long) {
            this.value = thisNum.longValue() + value.longValue();
        } else if (thisNum instanceof Float || thisNum instanceof Double) {
            this.value = thisNum.doubleValue() + value.doubleValue();
        } else {
            throw new IllegalArgumentException("Unsupported number type: " + thisNum.getClass().getName());
        }
    }
}
