package net.ingoh.myagents.lang.il;

import java.util.List;

public record Case(
        SwitchLabel label,
        List<BlockStmt> body) implements ILNode {}
