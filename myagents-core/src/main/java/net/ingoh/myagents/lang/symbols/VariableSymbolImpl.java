package net.ingoh.myagents.lang.symbols;

import net.ingoh.myagents.core.DSLArray;
import net.ingoh.myagents.lang.execution.Interpreter;

import java.util.HashMap;

public class VariableSymbolImpl implements VariableSymbol {
    private final String name;
    private Object staticValue;
    private Object defaultValue;
    private HashMap<Object, Object> values = new HashMap<>();

    public VariableSymbolImpl(Interpreter interpreter, String name, Object object, Object value) {
        this.name = name;
        while (value instanceof VariableSymbol) {
            value = ((VariableSymbol) value).getValue(interpreter, object);
        }
        this.defaultValue = value;
        setValue(interpreter, object, value);
    }

    @Override
    public String getName() {
        return name;
    }

    @Override
    public Object getValue(Interpreter interpreter, Object object) {
        if (object == null) {
            return staticValue;
        }
        if (values.containsKey(object)) {
            var val = values.get(object);
            while (val instanceof VariableSymbol vs) {
                val = vs.getValue(interpreter, object);
            }
            if (val instanceof DSLArray arr) {
                val = arr.toArray();
            }
            return val;
        }
        setValue(interpreter, object, defaultValue);
        return values.get(object);
    }

    @Override
    public <T> T getValue(Interpreter interpreter, Object object, Class<T> type) {
        var value = getValue(interpreter, object);
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
    public void setValue(Interpreter interpreter, Object object, Object value) {
        while (value instanceof VariableSymbol) {
            value = ((VariableSymbol) value).getValue(interpreter, object);
        }
        if (object == null) {
            staticValue = value;
        } else {
            values.put(object, value);
        }
    }

    @Override
    public void changeValueBy(Interpreter interpreter, Object object, Number value) {
        var thisNum = getValue(interpreter, object, Number.class);
        if (thisNum instanceof Float || thisNum instanceof Double || value instanceof Float || value instanceof Double) {
            setValue(interpreter, object, thisNum.doubleValue() + value.doubleValue());
        } else if (thisNum != null && value != null) {
            setValue(interpreter, object, thisNum.longValue() + value.longValue());
        } else {
            throw new IllegalArgumentException("Unsupported number type: " + thisNum.getClass().getName());
        }
    }

    @Override
    public String toString() {
        return staticValue.toString();
    }
}
