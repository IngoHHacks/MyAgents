package net.ingoh.myagents.lsp.server;

import org.eclipse.lsp4j.*;
import org.eclipse.lsp4j.services.LanguageClient;
import org.eclipse.lsp4j.services.LanguageClientAware;
import org.eclipse.lsp4j.services.LanguageServer;

import java.net.URI;
import java.util.List;
import java.util.concurrent.CompletableFuture;

public class MyAgentsLSP implements LanguageServer, LanguageClientAware {

    private MyAgentsTextDocumentService textDocumentService;
    private MyAgentsWorkspaceService workspaceService;
    private LanguageClient client;

    public MyAgentsLSP() {
        this.textDocumentService = new MyAgentsTextDocumentService(this);
        this.workspaceService = new MyAgentsWorkspaceService();
    }

    @Override
    public CompletableFuture<InitializeResult> initialize(InitializeParams params) {
        ServerCapabilities capabilities = new ServerCapabilities();
        capabilities.setTextDocumentSync(TextDocumentSyncKind.Full);
        capabilities.setCompletionProvider(new CompletionOptions(true, List.of(".")));
        InitializeResult result = new InitializeResult(capabilities);
        return CompletableFuture.completedFuture(result);
    }

    @Override
    public void connect(LanguageClient client) {
        this.client = client;
    }

    public LanguageClient getClient() {
        return client;
    }

    @Override
    public CompletableFuture<Object> shutdown() {
        return CompletableFuture.completedFuture(null);
    }

    @Override
    public void exit() {
        System.exit(0);
    }

    @Override
    public MyAgentsTextDocumentService getTextDocumentService() {
        return textDocumentService;
    }

    @Override
    public MyAgentsWorkspaceService getWorkspaceService() {
        return workspaceService;
    }
}
