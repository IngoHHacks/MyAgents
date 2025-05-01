package net.ingoh.myagents.lsp.server.tokens;

public class TokenType {
    int type;
    int modifier;

    public TokenType(int type, int modifier) {
        this.type = type;
        this.modifier = modifier;
    }

    public TokenType withType(int type) {
        return new TokenType(type, this.modifier);
    }

    public TokenType withType(SemanticToken.Types type) {
        return withType(type.getId());
    }

    public TokenType withModifier(int modifier) {
        return new TokenType(this.type, this.modifier | modifier);
    }

    public TokenType withModifier(SemanticToken.Modifiers modifier) {
        return withModifier(modifier.getId());
    }

    public int getType() {
        return type;
    }

    public int getModifier() {
        return modifier;
    }
}
