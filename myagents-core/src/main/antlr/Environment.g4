grammar Environment;

import Common;

// Lexer rules
TICKRATE: [tT][iI][cC][kK][rR][aA][tT][eE];
AGENTS: [aA][gG][eE][nN][tT][sS];

// Parser rules
program: (nameDecl | tickRateDecl | agentsDecl | tickMethodDecl)*  EOF;

nameDecl: NAME ID SEMICOLON;
tickRateDecl: TICKRATE number SEMICOLON;
agentsDecl: AGENTS ID+ SEMICOLON;
tickMethodDecl: TICK ID block;