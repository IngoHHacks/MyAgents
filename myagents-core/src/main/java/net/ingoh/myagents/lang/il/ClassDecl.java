package net.ingoh.myagents.lang.il;

import java.util.List;

public record ClassDecl(String name, List<ClassBodyDecl> body) implements ClassBodyDecl, TopLevelDecl {
    public ClassDecl {
        if (name == null || name.isEmpty()) {
            throw new IllegalArgumentException("Class name cannot be null or empty");
        }
        if (body == null) {
            throw new IllegalArgumentException("Class body cannot be null");
        }
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append("class ").append(name).append(" {\n");
        for (ClassBodyDecl decl : body) {
            sb.append(decl).append("\n");
        }
        sb.append("}");
        return sb.toString();
    }
}
