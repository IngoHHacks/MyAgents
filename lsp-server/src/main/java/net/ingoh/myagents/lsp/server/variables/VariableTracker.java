package net.ingoh.myagents.lsp.server.variables;

import java.util.LinkedList;
import java.util.List;
import java.util.Stack;

public class VariableTracker {
    private static final int BRACKETS = 0;
    private static final int PARENTHESES = 1;
    private boolean invalid = false;

    private final List<TrackedVariable> trackedVariables;
    private final Stack<Integer> scopeStack;

    public VariableTracker() {
        this.trackedVariables = new LinkedList<>();
        this.scopeStack = new Stack<>();
    }

    public void push(int variant, Class<?> type, String id) {
        TrackedVariable variable = new TrackedVariable(variant, type, id, scopeStack.size());
        trackedVariables.add(variable);
    }

    public void braceIn() {
        scopeStack.push(BRACKETS);
    }

    public void braceOut() {
        if (scopeStack.isEmpty() || scopeStack.peek() != BRACKETS) {
            invalid = true;
            return;
        }
        scopeStack.pop();
        updateVariables();
    }

    public void parenIn() {
        scopeStack.push(PARENTHESES);
    }

    public void parenOut() {
        if (scopeStack.isEmpty() || scopeStack.peek() != PARENTHESES) {
            invalid = true;
            return;
        }
        scopeStack.pop();
        updateVariables();
    }

    private void updateVariables() {
        for (int i = trackedVariables.size() - 1; i >= 0; i--) {
            TrackedVariable variable = trackedVariables.get(i);
            if (variable.getScope() == scopeStack.size()) {
                trackedVariables.remove(i);
            }
        }
    }

    public TrackedVariable find(String id) {
        if (isInvalid() || id == null || id.isEmpty()) {
            return null;
        }
        for (int i = trackedVariables.size() - 1; i >= 0; i--) {
            TrackedVariable variable = trackedVariables.get(i);
            if (variable.getId().equals(id)) {
                return variable;
            }
        }
        return null;
    }

    public List<TrackedVariable> getVariables() {
        if (isInvalid()) {
            return List.of();
        }
        return new LinkedList<>(trackedVariables);
    }

    public void clear() {
        trackedVariables.clear();
        scopeStack.clear();
        invalid = false;
    }

    public boolean isInvalid() {
        return invalid;
    }
}
