package net.ingoh.myagents.lang.symbols;

import net.ingoh.myagents.lang.execution.Interpreter;

import java.lang.reflect.Field;

public class VariableSymbolJava implements VariableSymbol {
    private final Field src;

    public VariableSymbolJava(Field src) {
        this.src = src;
    }

    @Override
    public String getName() {
        return src.getName();
    }

    @Override
    public Object getValue(Interpreter interpreter) {
        try {
            return src.get(interpreter.getExecutionSource().getSource());
        } catch (IllegalAccessException e) {
            throw new RuntimeException("Failed to access field value", e);
        }
    }

    @Override
    public <T> T getValue(Interpreter interpreter, Class<T> type) {
        try {
            Object value = src.get(interpreter.getExecutionSource().getSource());
            try {
                return type.cast(value);
            } catch (ClassCastException e) {
                throw new RuntimeException("Failed to cast field value", e);
            }
        } catch (IllegalAccessException e) {
            throw new RuntimeException("Failed to access field value", e);
        }
    }

    @Override
    public void setValue(Interpreter interpreter, Object value) {
        try {
            src.set(interpreter.getExecutionSource().getSource(), value);
        } catch (IllegalAccessException e) {
            throw new RuntimeException("Failed to set field value", e);
        }
    }

    @Override
    public void changeValueBy(Interpreter interpreter, Number value) {
         var thisNum = getValue(interpreter, Number.class);
         if (thisNum instanceof Integer || thisNum instanceof Long) {
            setValue(interpreter, thisNum.longValue() + value.longValue());
         } else if (thisNum instanceof Float || thisNum instanceof Double) {
            setValue(interpreter,thisNum.doubleValue() + value.doubleValue());
         } else {
            throw new IllegalArgumentException("Unsupported number type: " + thisNum.getClass().getName());
         }
    }
}
