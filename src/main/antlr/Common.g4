grammar Common;

// Lexer rules

// Symbols
LPAREN: '(';
RPAREN: ')';
LBRACE: '{';
RBRACE: '}';
DOT: '.';
COMMA: ',';
COLON: ':';
SEMICOLON: ';';
DASH: '-';
QUOTE: '"';
SINGLE_QUOTE: '\'';
BACKSLASH: '\\';

// Variable types
FLOAT: DASH? [0-9]+ DOT ([0-9]+)?;
INT: DASH? [0-9]+;
BOOL
    : [tT][rR][uU][eE]
    | [fF][aA][lL][sS][eE]
    ;

STRING: QUOTE (~["\\] | BACKSLASH .)* QUOTE;

// Keywords
NAME: [nN][aA][mM][eE];
PRINT: [pP][rR][iI][nN][tT];
RETURN: [rR][eE][tT][uU][rR][nN];

// Common methods
TICK: [tT][iI][cC][kK];

// Whitespace and comments
WS: [ \t\r\n]+ -> skip;
COMMENT_LINE: '//' ~[\r\n]* -> skip;
COMMENT_BLOCK: '/*' .*? '*/' -> skip;

// Identifiers (keep this at the end so it doesn't interfere with keywords)
ID: [a-zA-Z_][a-zA-Z0-9_]*;

// Parser rules
number: INT | FLOAT;

block: LBRACE statement* RBRACE;

statement
    : printStatement
    ;

printStatement: PRINT LPAREN expression RPAREN SEMICOLON;

expression
    : STRING
    | ID
    ;
