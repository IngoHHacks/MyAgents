package net.ingoh.myagents.lang.il;

import java.util.List;

public record ClassDecl(TypeIdentifier name, List<ClassBodyDecl> body) implements ILNode, TopLevelDecl, MemberDecl {
    public ClassDecl {
        if (name == null) {
            throw new IllegalArgumentException("Class name cannot be null");
        }
        if (body == null) {
            throw new IllegalArgumentException("Class body cannot be null");
        }
    }
}
