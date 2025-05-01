package net.ingoh.myagents.lsp.server;

import net.ingoh.myagents.lang.internal.EnvironmentLexer;
import net.ingoh.myagents.lang.internal.EnvironmentParser;
import net.ingoh.myagents.lsp.server.tokens.SemanticToken;
import net.ingoh.myagents.lsp.server.tokens.SemanticTokenEncoder;
import net.ingoh.myagents.lsp.server.tokens.SemanticTokenizer;
import net.ingoh.myagents.lsp.server.variables.TrackedVariable;
import org.antlr.v4.runtime.CharStream;
import org.antlr.v4.runtime.CharStreams;
import org.antlr.v4.runtime.CommonTokenStream;
import org.eclipse.lsp4j.*;
import org.eclipse.lsp4j.jsonrpc.messages.Either;
import org.eclipse.lsp4j.services.TextDocumentService;

import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ConcurrentHashMap;

public class MyAgentsTextDocumentService implements TextDocumentService {

    private final Map<String, TextDocumentItem> documentMap = new ConcurrentHashMap<>();
    private final MyAgentsLSP server;

    public MyAgentsTextDocumentService(MyAgentsLSP server) {
        this.server = server;
    }

    @Override
    public void didOpen(DidOpenTextDocumentParams params) {
        documentMap.put(params.getTextDocument().getUri(), params.getTextDocument());
        server.getDocumentTrackData().setCurrentDocument(params.getTextDocument().getUri());
        publishDiagnostics(params.getTextDocument().getUri(), params.getTextDocument().getText());
    }

    @Override
    public void didChange(DidChangeTextDocumentParams params) {
        var changes = params.getContentChanges();
        if (changes.isEmpty()) {
            return;
        }
        if (changes.size() > 1) {
            throw new IllegalArgumentException("Multiple changes are not supported");
        }
        if (!documentMap.containsKey(params.getTextDocument().getUri())) {
            throw new IllegalArgumentException("Document not opened");
        }
        var change = changes.get(0);
        publishDiagnostics(params.getTextDocument().getUri(), change.getText());
        documentMap.get(params.getTextDocument().getUri()).setText(change.getText());
    }

    @Override
    public void didClose(DidCloseTextDocumentParams params) {
        documentMap.remove(params.getTextDocument().getUri());
        publishDiagnostics(params.getTextDocument().getUri(), "");
    }

    @Override
    public void didSave(DidSaveTextDocumentParams params) {
        // No action needed on save (currently)
    }

    @Override
    public CompletableFuture<Either<List<CompletionItem>, CompletionList>> completion(CompletionParams position) {
        if (!documentMap.containsKey(position.getTextDocument().getUri())) {
            throw new IllegalArgumentException("Document not opened");
        }
        List<CompletionItem> items = new LinkedList<>();
        // TODO: Make this use the vocabulary of the current file data type and context-aware
        for (int i = 1; i < EnvironmentLexer.VOCABULARY.getMaxTokenType(); i++) {
            String name = EnvironmentLexer.VOCABULARY.getSymbolicName(i).toLowerCase();
            if (!name.isEmpty()) {
                CompletionItem item = new CompletionItem();
                item.setLabel(name);
                item.setKind(CompletionItemKind.Keyword);
                items.add(item);
            }
        }
        SemanticTokenizer.parseSemanticTokens(server, documentMap.get(position.getTextDocument().getUri()).getText(), position.getPosition());
        for (TrackedVariable variable : server.getDocumentTrackData().getVariablesHere()) {
            CompletionItem item = new CompletionItem();
            item.setLabel(variable.getId());
            switch (variable.getVariant()) {
                case TrackedVariable.FIELD -> item.setKind(CompletionItemKind.Field);
                case TrackedVariable.LOCAL, TrackedVariable.PARAMETER -> item.setKind(CompletionItemKind.Variable);
                default -> item.setKind(CompletionItemKind.Text);
            }
            items.add(item);
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

    @Override
    public CompletableFuture<SemanticTokens> semanticTokensFull(SemanticTokensParams params) {
        String uri = params.getTextDocument().getUri();
        if (!documentMap.containsKey(uri)) {
            throw new IllegalArgumentException("Document not opened");
        }
        String text = documentMap.get(uri).getText();

        List<SemanticToken> tokens = SemanticTokenizer.parseSemanticTokens(server, text);
        List<Integer> encoded = SemanticTokenEncoder.encode(tokens);

        return CompletableFuture.completedFuture(new SemanticTokens(encoded));
    }
}
