lexer grammar CommonLexer;

@header {
package net.ingoh.myagents.lang.lexers;
}

TICK: [tT][iI][cC][kK];
PRINT: [pP][rR][iI][nN][tT];
RETURN: [rR][eE][tT][uU][rR][nN];
BOOL_LITERAL
    : [tT][rR][uU][eE]
    | [fF][aA][lL][sS][eE]
    ;

TYPE: [tT][yY][pP][eE];
NAME: [nN][aA][mM][eE];

STRING_LITERAL: '"' (~["\\] | '\\' .)* '"';
ID: [a-zA-Z_][a-zA-Z0-9_]*;

LPAREN: '(';
RPAREN: ')';
LBRACE: '{';
RBRACE: '}';
SEMICOLON: ';';

WS: [ \t\r\n]+ -> skip;