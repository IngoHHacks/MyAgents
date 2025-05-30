package net.ingoh.myagents.utils;

import java.util.LinkedList;
import java.util.List;

public class ThreadSafeList<T> {
    private final java.util.List<T> list;

    public ThreadSafeList() {
        this.list = new java.util.ArrayList<>();
    }

    public synchronized boolean add(T item) {
        return list.add(item);
    }

    public synchronized boolean remove(T item) {
        return list.remove(item);
    }

    public synchronized T get(int index) {
        return list.get(index);
    }

    public synchronized int size() {
        return list.size();
    }

    public synchronized boolean isEmpty() {
        return list.isEmpty();
    }

    public synchronized List<T> toList() {
        return new LinkedList<>(list);
    }

    public synchronized boolean contains(T recipient) {
        return list.contains(recipient);
    }
}
