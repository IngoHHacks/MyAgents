package net.ingoh.myagents.lsp.server;

import org.eclipse.lsp4j.launch.LSPLauncher;

public class Main {
    public static void main(String[] args) {
        MyAgentsLSP server = new MyAgentsLSP();
        LSPLauncher.createServerLauncher(server, System.in, System.out).startListening();
    }
}
