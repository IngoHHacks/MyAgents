package net.ingoh.myagents.lang.il;

import net.ingoh.myagents.lang.execution.Interpreter;

import java.util.List;

public record ArrayAccessExpr(Expr array, Expr index) implements ILNode, Expr {
    public ArrayAccessExpr {
        if (array == null || index == null) {
            throw new IllegalArgumentException("Array and index cannot be null");
        }
    }

    @Override
    public Object accept(Interpreter interpreter) {
        var arr = array.accept(interpreter);
        var idx = index.accept(interpreter);
        if (arr instanceof Object[] array && idx instanceof Integer index) {
            return array[index];
        } else if (arr instanceof int[] array && idx instanceof Integer index) {
            return array[index];
        } else if (arr instanceof double[] array && idx instanceof Integer index) {
            return array[index];
        } else if (arr instanceof boolean[] array && idx instanceof Integer index) {
            return array[index];
        } else if (arr instanceof char[] array && idx instanceof Integer index) {
            return array[index];
        } else if (arr instanceof List<?> list && idx instanceof Integer index) {
            return list.get(index);
        } else {
            throw new IllegalArgumentException("Invalid array access");
        }
    }
}