package net.ingoh.myagents.lang.il;

public class StringLiteralIL extends ExpressionIL {
    public String value;

    public StringLiteralIL() {
        this.value = "";
    }

    public StringLiteralIL(String value) {
        this.value = value;
    }
}
