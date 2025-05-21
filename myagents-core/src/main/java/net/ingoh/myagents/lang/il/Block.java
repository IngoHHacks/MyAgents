package net.ingoh.myagents.lang.il;

import net.ingoh.myagents.lang.execution.Interpreter;

import java.util.List;

public record Block(List<BlockStmt> statements) implements ILNode, ClassBodyDecl, Stmt {
    public Block {
        if (statements == null) {
            throw new IllegalArgumentException("Block statements cannot be null or empty");
        }
    }

    @Override
    public Object accept(Interpreter interpreter) {
        return interpreter.runBlock(statements);
    }
}
