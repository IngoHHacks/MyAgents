package net.ingoh.myagents.lsp.server.tokens;

import net.ingoh.myagents.lang.internal.EnvironmentLexer;
import net.ingoh.myagents.lang.internal.MyAgentsLexer;
import net.ingoh.myagents.lsp.server.variables.TrackedVariable;
import net.ingoh.myagents.lsp.server.variables.VariableTracker;
import net.ingoh.myagents.lsp.server.MyAgentsLSP;
import org.antlr.v4.runtime.CharStream;
import org.antlr.v4.runtime.CharStreams;
import org.antlr.v4.runtime.Lexer;
import org.antlr.v4.runtime.Token;
import org.eclipse.lsp4j.Position;

import java.util.LinkedList;
import java.util.List;

public class SemanticTokenizer {

    private static final TokenType NONE = new TokenType(SemanticToken.Types.NONE.getId(), 0);

    public static List<SemanticToken> parseSemanticTokens(MyAgentsLSP server, String text) {
        return parseSemanticTokens(server, text, null);
    }

    public static List<SemanticToken> parseSemanticTokens(MyAgentsLSP server, String text, Position caret) {
        boolean variablesSuggested = false;
        int numIdentifiers = 0;
        int maxIdentifiers = 0;
        TokenType identifiersAreWhat = new TokenType(SemanticToken.Types.NONE.getId(), 0);
        boolean trackIdentifiers = false;
        VariableTracker trackedVariables = server.getDocumentTrackData().getTrackedVariables();
        List<TrackedVariable> variablesHere = server.getDocumentTrackData().getVariablesHere();
        trackedVariables.clear();
        variablesHere.clear();
        List<SemanticToken> tokens = new LinkedList<>();

        CharStream input = CharStreams.fromString(text);
        Lexer lexer = new MyAgentsLexer(input);
        lexer.removeErrorListeners();
        Token token;

        while ((token = lexer.nextToken()).getType() != Token.EOF) {
            String tokenText = token.getText();
            String tn = lexer.getVocabulary().getSymbolicName(token.getType());
            int line = token.getLine() - 1;
            int charPos = token.getCharPositionInLine();
            int length = tokenText.length();

            if (caret != null) {
                if (!variablesSuggested && (line > caret.getLine() || (line == caret.getLine() && charPos + length >= caret.getCharacter()))) {
                    variablesHere = trackedVariables.getVariables();
                    variablesSuggested = true;
                }
            }

            switch (tn) {
                case "SEMICOLON" -> {
                    numIdentifiers = 0;
                    maxIdentifiers = 0;
                    identifiersAreWhat = NONE;
                    trackIdentifiers = false;
                    continue;
                }
                case "NAME" -> {
                    maxIdentifiers = 1;
                    identifiersAreWhat = new TokenType(SemanticToken.Types.CLASS.getId(), 0);
                }
                case "AGENTS" -> {
                    maxIdentifiers = -1;
                    identifiersAreWhat = new TokenType(SemanticToken.Types.CLASS.getId(), 0);
                }
                case "TICK" -> {
                    maxIdentifiers = 1;
                    identifiersAreWhat = new TokenType(SemanticToken.Types.PARAMETER.getId(), 0);
                    trackIdentifiers = true;
                }
                case "LBRACE" -> trackedVariables.braceIn();
                case "RBRACE" -> trackedVariables.braceOut();
                case "LPAREN" -> trackedVariables.parenIn();
                case "RPAREN" -> trackedVariables.parenOut();
            }

            if (tn.equals("ID") && (numIdentifiers < maxIdentifiers || maxIdentifiers == -1)) {
                tokens.add(new SemanticToken(line, charPos, length, identifiersAreWhat.getType(), identifiersAreWhat.getModifier()));
                numIdentifiers++;
                if (trackIdentifiers) {
                    trackedVariables.push(TrackedVariable.PARAMETER, Float.class, tokenText);
                }
                continue;
            } else {
                TrackedVariable variable = trackedVariables.find(tokenText);
                if (variable != null) {
                    tokens.add(new SemanticToken(line, charPos, length, variable.getTokenType().getType(), variable.getTokenType().getModifier()));
                    continue;
                }
            }

            TokenType type = SemanticTokenizer.classify(token);
            if (type.type >= 0) {
                tokens.add(new SemanticToken(line, charPos, length, type.getType(), type.getModifier()));
            }
        }
        server.getDocumentTrackData().setVariablesHere(variablesHere);

        return tokens;
    }

    public static TokenType classify(Token token) {
            return switch (token.getType()) {
            case EnvironmentLexer.FLOAT, EnvironmentLexer.INT -> SemanticToken.Types.NUMBER.type();
            case EnvironmentLexer.BOOL -> SemanticToken.Types.KEYWORD.type();
            case EnvironmentLexer.STRING -> SemanticToken.Types.STRING.type();
            case EnvironmentLexer.NAME, EnvironmentLexer.AGENTS, EnvironmentLexer.TICKRATE -> SemanticToken.Types.PROPERTY.type();
            case EnvironmentLexer.TICK, EnvironmentLexer.PRINT -> SemanticToken.Types.FUNCTION.type();
            case EnvironmentLexer.COMMENT_LINE, EnvironmentLexer.COMMENT_BLOCK -> SemanticToken.Types.COMMENT.type();
            default -> SemanticToken.Types.NONE.type();
        };
    }
}
