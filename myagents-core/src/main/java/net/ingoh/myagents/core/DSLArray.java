package net.ingoh.myagents.core;

import java.util.List;

public class DSLArray {
    private List<Object> array;

    public DSLArray(List<Object> array) {
        if (array == null) {
            throw new IllegalArgumentException("Array cannot be null");
        }
        this.array = array;
    }

    public List<Object> getArray() {
        return array;
    }

    public Object get(int index) {
        if (index < 0 || index >= array.size()) {
            throw new IndexOutOfBoundsException("Index out of bounds: " + index);
        }
        return array.get(index);
    }

    public Object toArray() {
        var arr = new Object[array.size()];
        for (int i = 0; i < array.size(); i++) {
            if (array.get(i) instanceof DSLArray dslArray) {
                arr[i] = dslArray.toArray();
            } else {
                arr[i] = array.get(i);
            }
        }
        return arr;
    }
}
