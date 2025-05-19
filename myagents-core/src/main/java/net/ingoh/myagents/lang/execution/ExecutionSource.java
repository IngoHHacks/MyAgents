package net.ingoh.myagents.lang.execution;

public class ExecutionSource {
    public static ExecutionSource STATIC = new ExecutionSource(null);

    private final Object source;

    public ExecutionSource(Object source) {
        this.source = source;
    }

    public Object getSource() {
        return source;
    }
}
