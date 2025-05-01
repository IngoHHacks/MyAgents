package net.ingoh.myagents.lsp.server.variables;

import net.ingoh.myagents.lsp.server.tokens.SemanticToken;
import net.ingoh.myagents.lsp.server.tokens.TokenType;

public class TrackedVariable {

    public static final int FIELD = 0;
    public static final int LOCAL = 1;
    public static final int PARAMETER = 2;

    private final int variant;
    private final Class<?> type;
    private final String id;
    private final int scope;

    public TrackedVariable(int variant, Class<?> type, String id, int scope) {
        this.variant = variant;
        this.type = type;
        this.id = id;
        this.scope = scope;
    }

    public int getVariant() {
        return variant;
    }

    public Class<?> getType() {
        return type;
    }

    public String getId() {
        return id;
    }

    public int getScope() {
        return scope;
    }

    public TokenType getTokenType() {
        return switch (variant) {
            case FIELD, LOCAL -> new TokenType(SemanticToken.Types.VARIABLE.getId(), 0);
            case PARAMETER -> new TokenType(SemanticToken.Types.PARAMETER.getId(), 0);
            default -> new TokenType(SemanticToken.Types.NONE.getId(), 0);
        };
    }
}
