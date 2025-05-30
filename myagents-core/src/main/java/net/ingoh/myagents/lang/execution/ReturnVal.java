package net.ingoh.myagents.lang.execution;

public class ReturnVal {
    public final Object value;
    public final boolean isVoid;

    public ReturnVal(Object value) {
        this.value = value;
        this.isVoid = value == null;
    }
}
