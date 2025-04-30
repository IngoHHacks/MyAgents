/* --------------------------------------------------------------------------------------------
 * Copyright (c) Microsoft Corporation. All rights reserved.
 * Licensed under the MIT License. See License.txt in the project root for license information.
 * ------------------------------------------------------------------------------------------ */

import * as cp from 'child_process';
import * as path from 'path';
import { ExtensionContext, workspace } from 'vscode';

import {
	LanguageClient,
	LanguageClientOptions,
	ServerOptions
} from 'vscode-languageclient/node';

let client: LanguageClient;

export function activate(context: ExtensionContext) {
	// The server is implemented in Java
	const serverModule = path.join(context.extensionPath, 'lsp-server.jar');

	// If the extension is launched in debug mode then the debug server options are used
	// Otherwise the run options are used
	const serverOptions: ServerOptions = () => {
		const args = [
			'-agentlib:jdwp=transport=dt_socket,server=y,suspend=n,address=*:5005,quiet=y',
			'-jar', serverModule
		];
		const process = cp.spawn('java', args, { stdio: 'pipe' });
		process.on('stderr', (data) => {
			console.error(`Server stderr: ${data}`);
		});
		return Promise.resolve({
			reader: process.stdout,
			writer: process.stdin
		});
	};

	// Options to control the language client
	const clientOptions: LanguageClientOptions = {
		// Register the server for myagents documents
		documentSelector: [{ scheme: 'file', language: 'myagents' }],
		synchronize: {
			// Notify the server about file changes to '.clientrc files contained in the workspace
			fileEvents: workspace.createFileSystemWatcher('**/.clientrc')
		},
		outputChannelName: 'MyAgents Language Server'
	};

	// Create the language client and start the client.
	client = new LanguageClient(
		'myAgentsLanguageServer',
		'MyAgents Language Server',
		serverOptions,
		clientOptions
	);

	// Start the client. This will also launch the server
	client.start();
}

export function deactivate(): Thenable<void> | undefined {
	if (!client) {
		return undefined;
	}
	return client.stop();
}
