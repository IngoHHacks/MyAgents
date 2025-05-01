package net.ingoh.myagents.lsp.server.documents;

import net.ingoh.myagents.core.DataType;
import net.ingoh.myagents.lsp.server.variables.TrackedVariable;
import net.ingoh.myagents.lsp.server.variables.VariableTracker;
import org.antlr.v4.runtime.CharStream;
import org.antlr.v4.runtime.Lexer;
import org.antlr.v4.runtime.Parser;
import org.eclipse.lsp4j.Position;

import java.util.LinkedList;
import java.util.List;

public class DocumentTrackData {
    private String currentDocumentUri;
    private int currentDocumentType = -1;
    private final VariableTracker trackedVariables = new VariableTracker();
    private List<TrackedVariable> variablesHere = new LinkedList<>();


    public void setCurrentDocument(String fileUri) {
        this.currentDocumentUri = fileUri;
        if (fileUri.endsWith(".yenv")) {
            currentDocumentType = DataType.TYPE_ENVIRONMENT;
        } else if (fileUri.endsWith(".yage")) {
            currentDocumentType = DataType.TYPE_AGENT;
        } else {
            currentDocumentType = DataType.TYPE_UNKNOWN;
        }
    }

    public String getCurrentDocument() {
        return currentDocumentUri;
    }

    public int getCurrentDocumentType() {
        return currentDocumentType;
    }

    public Class<? extends Lexer> getLexerForCurrentDocument() {
        return DataType.getLexerForType(currentDocumentType);
    }

    public Lexer getLexerForCurrentDocument(CharStream input) {
        var lexerClass = getLexerForCurrentDocument();
        try {
            return lexerClass.getConstructor(CharStream.class).newInstance(input);
        } catch (Exception e) {
            throw new RuntimeException("Failed to create lexer for current document", e);
        }
    }

    public Class<? extends Parser> getParserForCurrentDocument() {
        return DataType.getParserForType(currentDocumentType);
    }

    public Parser getParserForCurrentDocument(CharStream input) {
        var parserClass = getParserForCurrentDocument();
        try {
            return parserClass.getConstructor(CharStream.class).newInstance(input);
        } catch (Exception e) {
            throw new RuntimeException("Failed to create parser for current document", e);
        }
    }

    public void clear() {
        currentDocumentUri = null;
        currentDocumentType = DataType.TYPE_UNKNOWN;
    }

    public VariableTracker getTrackedVariables() {
        return trackedVariables;
    }

    public List<TrackedVariable> getVariablesHere() {
        return new LinkedList<>(variablesHere);
    }

    public void setVariablesHere(List<TrackedVariable> variablesHere) {
        this.variablesHere = new LinkedList<>(variablesHere);
    }
}
