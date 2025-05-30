package net.ingoh.myagents.lang.symbols;

import net.ingoh.myagents.lang.execution.Interpreter;
import net.ingoh.myagents.utils.ClassHelper;

import java.lang.reflect.Field;

public class VariableSymbolJava implements VariableSymbol {
    private final Field src;
    private Object staticValue;

    public VariableSymbolJava(Field src) {
        this.src = src;
        this.staticValue = null;
    }

    @Override
    public String getName() {
        return src.getName();
    }

    @Override
    public Object getValue(Interpreter interpreter, Object object) {
        try {
            if (object == null) {
                return staticValue;
            }
            return src.get(object);
        } catch (IllegalAccessException e) {
            throw new RuntimeException("Failed to access field value", e);
        }
    }

    @Override
    public <T> T getValue(Interpreter interpreter, Object object, Class<T> type) {
        try {
            if (object == null) {
                return type.cast(staticValue);
            }
            Object value = src.get(object);
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
    public void setValue(Interpreter interpreter, Object object, Object value) {
        try {
            while (value instanceof VariableSymbol) {
                value = ((VariableSymbol) value).getValue(interpreter, object);
            }
            if (object == null) {
                staticValue = value;
                return;
            }
            src.set(object, ClassHelper.cast(value, src.getType()));
        } catch (IllegalAccessException e) {
            throw new RuntimeException("Failed to set field value", e);
        }
    }

    @Override
    public void changeValueBy(Interpreter interpreter, Object object, Number value) {
         var thisNum = getValue(interpreter, object, Number.class);
         if (object == null) {
            thisNum = thisNum.longValue() + (staticValue instanceof Number ? ((Number) staticValue).longValue() : 0);
         }
         if (thisNum instanceof Integer || thisNum instanceof Long) {
            setValue(interpreter, object, thisNum.longValue() + value.longValue());
         } else if (thisNum instanceof Float || thisNum instanceof Double) {
            setValue(interpreter, object, thisNum.doubleValue() + value.doubleValue());
         } else {
            throw new IllegalArgumentException("Unsupported number type: " + thisNum.getClass().getName());
         }
    }
}
