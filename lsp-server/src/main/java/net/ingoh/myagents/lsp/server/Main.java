package net.ingoh.myagents.lsp.server;

import org.eclipse.lsp4j.launch.LSPLauncher;

public class Main {
    public static void main(String[] args) {
        MyAgentsLSP server = new MyAgentsLSP();
        var launcher = LSPLauncher.createServerLauncher(server, System.in, System.out);
        server.connect(launcher.getRemoteProxy());
        launcher.startListening();
    }
}
