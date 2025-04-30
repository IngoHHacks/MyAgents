grammar Agent;

import Common;

// Lexer rules
// To be added later

// Parser rules
program: (nameDecl | tickMethodDecl)* EOF;

nameDecl: NAME ID SEMICOLON;
tickMethodDecl: TICK ID block;