package net.ingoh.myagents.lsp.server;

import org.antlr.v4.runtime.ANTLRErrorListener;
import org.antlr.v4.runtime.BaseErrorListener;
import org.antlr.v4.runtime.RecognitionException;
import org.antlr.v4.runtime.Recognizer;
import org.eclipse.lsp4j.Diagnostic;
import org.eclipse.lsp4j.DiagnosticSeverity;
import org.eclipse.lsp4j.Position;
import org.eclipse.lsp4j.Range;

import java.util.List;

public class MyAgentsErrorListener extends BaseErrorListener {
    private final List<Diagnostic> diagnostics;

    public MyAgentsErrorListener(List<Diagnostic> diagnostics) {
        this.diagnostics = diagnostics;
    }

    @Override
    public void syntaxError(Recognizer<?, ?> recognizer, Object offendingSymbol, int line, int charPositionInLine, String msg, RecognitionException e) {
        Diagnostic diagnostic = new Diagnostic();
        diagnostic.setRange(new Range(new Position(line - 1, charPositionInLine), new Position(line - 1, charPositionInLine + 1)));
        diagnostic.setSeverity(DiagnosticSeverity.Error);
        diagnostic.setMessage(msg);
        diagnostics.add(diagnostic);
    }
}
