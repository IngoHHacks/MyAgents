package net.ingoh.myagents.lang.il;

import java.util.List;

public record SwitchLabel(List<Expr> constExprs, List<VariableIdentifier> variableIdentifiers) implements ILNode {
    public SwitchLabel {
        if (constExprs == null && variableIdentifiers == null) {
            throw new IllegalArgumentException("At least one of constExprs or variableIdentifiers must be non-null");
        }
    }
}