package net.ingoh.myagents.lsp.server;

import net.ingoh.myagents.lang.internal.EnvironmentLexer;
import net.ingoh.myagents.lang.internal.EnvironmentParser;
import org.antlr.v4.runtime.CharStream;
import org.antlr.v4.runtime.CharStreams;
import org.antlr.v4.runtime.CommonTokenStream;
import org.eclipse.lsp4j.*;
import org.eclipse.lsp4j.jsonrpc.messages.Either;
import org.eclipse.lsp4j.services.TextDocumentService;

import java.util.LinkedList;
import java.util.List;
import java.util.concurrent.CompletableFuture;

public class MyAgentsTextDocumentService implements TextDocumentService {

    private final MyAgentsLSP server;

    public MyAgentsTextDocumentService(MyAgentsLSP server) {
        this.server = server;
    }

    @Override
    public void didOpen(DidOpenTextDocumentParams params) {
        publishDiagnostics(params.getTextDocument().getUri(), params.getTextDocument().getText());
    }

    @Override
    public void didChange(DidChangeTextDocumentParams params) {
        for (var change : params.getContentChanges()) {
            publishDiagnostics(params.getTextDocument().getUri(), change.getText());
        }
    }

    @Override
    public void didClose(DidCloseTextDocumentParams params) {
        publishDiagnostics(params.getTextDocument().getUri(), "");
    }

    @Override
    public void didSave(DidSaveTextDocumentParams params) {
        // No action needed on save (currently)
    }

    @Override
    public CompletableFuture<Either<List<CompletionItem>, CompletionList>> completion(CompletionParams position) {
        List<CompletionItem> items = new LinkedList<>();
        for (int i = 0; i < EnvironmentLexer.VOCABULARY.getMaxTokenType(); i++) {
            String name = EnvironmentLexer.VOCABULARY.getDisplayName(i).toLowerCase();
            if (name != null && !name.isEmpty()) {
                CompletionItem item = new CompletionItem();
                item.setLabel(name);
                item.setKind(CompletionItemKind.Keyword);
                items.add(item);
            }
        }
        return CompletableFuture.completedFuture(Either.forLeft(items));
    }

    @Override
    public CompletableFuture<CompletionItem> resolveCompletionItem(CompletionItem item) {
        return CompletableFuture.completedFuture(item);
    }

    private void publishDiagnostics(String uri, String text) {
        List<Diagnostic> diagnostics = new LinkedList<>();
        CharStream input = CharStreams.fromString(text);
        EnvironmentLexer lexer = new EnvironmentLexer(input);
        lexer.removeErrorListeners();
        lexer.addErrorListener(new MyAgentsErrorListener(diagnostics));
        CommonTokenStream tokens = new CommonTokenStream(lexer);
        EnvironmentParser parser = new EnvironmentParser(tokens);
        parser.removeErrorListeners();
        parser.addErrorListener(new MyAgentsErrorListener(diagnostics));
        try {
            parser.program();
        } catch (Exception e) {
            diagnostics.add(new Diagnostic(new Range(new Position(0, 0), new Position(0, 0)), e.getMessage(), DiagnosticSeverity.Error, "Parsing Error"));
        }
        PublishDiagnosticsParams params = new PublishDiagnosticsParams(uri, diagnostics);
        server.getClient().publishDiagnostics(params);
    }
}
