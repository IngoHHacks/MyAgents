package net.ingoh.myagents.lsp.server.tokens;

import java.util.LinkedList;
import java.util.List;

public class SemanticTokenEncoder {
    public static List<Integer> encode(List<SemanticToken> tokens) {
        List<Integer> result = new LinkedList<>();
        int lastLine = 0;
        int lastChar = 0;

        for (SemanticToken token : tokens) {
            int deltaLine = token.line - lastLine;
            int deltaChar = (deltaLine == 0) ? token.startChar - lastChar : token.startChar;

            result.add(deltaLine);
            result.add(deltaChar);
            result.add(token.length);
            result.add(token.tokenType);
            result.add(token.tokenModifiers);

            lastLine = token.line;
            lastChar = token.startChar;
        }

        return result;
    }
}