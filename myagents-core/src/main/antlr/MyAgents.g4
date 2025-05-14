grammar MyAgents;

// Lexer rules

// Java-based Keywords
BREAK: 'break';
CASE: 'case';
CATCH: 'catch';
CLASS: 'class';
CONTINUE: 'continue';
DEFAULT: 'default';
DO: 'do';
ELSE: 'else';
FINALLY: 'finally';
FOR: 'for';
FOREACH: 'foreach';
IF: 'if';
IN: 'in';
GOTO: 'goto';
IMPORT: 'import';
INSTANCEOF: 'instanceof';
NEW: 'new';
PACKAGE: 'package';
RETURN: 'return';
STATIC: 'static';
SUPER: 'super';
SWITCH: 'switch';
THIS: 'this';
THROW: 'throw';
TRY: 'try';
WHILE: 'while';

// Symbols
LPAREN: '(';
RPAREN: ')';
LBRACE: '{';
RBRACE: '}';
LBRACK: '[';
RBRACK: ']';
SEMICOLON: ';';
COLON: ':';
COMMA: ',';
DOT: '.';
ASSIGN: '=';
GT: '>';
LT: '<';
EQ: '==';
LE: '<=';
GE: '>=';
NE: '!=';
AND: '&&';
OR: '||';
NOT: '!';
PLUS: '+';
MINUS: '-';
MULT: '*';
DIV: '/';
MOD: '%';
BITAND: '&';
BITOR: '|';
BITXOR: '^';
BITNOT: '~';
INC: '++';
DEC: '--';
LSHIFT: '<<';
RSHIFT: '>>';
URSHIFT: '>>>';
QUESTION: '?';
ADD_ASSIGN: '+=';
SUB_ASSIGN: '-=';
MUL_ASSIGN: '*=';
DIV_ASSIGN: '/=';
MOD_ASSIGN: '%=';
AND_ASSIGN: '&=';
OR_ASSIGN: '|=';
XOR_ASSIGN: '^=';
LSHIFT_ASSIGN: '<<=';
RSHIFT_ASSIGN: '>>=';
URSHIFT_ASSIGN: '>>>=';
ARROW: '->';
DCOLON: '::';
AT: '@';
ELLIPSIS: '...';

// Variable literals
INT_LITERAL: ('0' | [1-9] (Digits? | '_'+ Digits)) [lL]?;
HEX_LITERAL: '0' [xX] [0-9a-fA-F] ([0-9a-fA-F_]* [0-9a-fA-F])? [lL]?;
OCT_LITERAL: '0' '_'* [0-7] ([0-7_]* [0-7])? [lL]?;
BINARY_LITERAL: '0' [bB] [01] ([01_]* [01])? [lL]?;

FLOAT_LITERAL:
    (Digits '.' Digits? | '.' Digits) ExponentPart? [fFdD]?
    | Digits (ExponentPart [fFdD]? | [fFdD])
;
HEX_FLOAT_LITERAL: '0' [xX] (HexDigits '.'? | HexDigits? '.' HexDigits) [pP] [+-]? Digits [fFdD]?;
BOOL_LITERAL: 'true' | 'false';
CHAR_LITERAL: '\'' (~['\\\r\n] | EscapeSequence) '\'';
STRING_LITERAL: '"' (~["\\\r\n] | EscapeSequence)* '"';
TEXT_BLOCK: '"""' [ \t]* [\r\n] (. | EscapeSequence)*? '"""';
NULL_LITERAL: 'null';

WS: [ \t\r\n\u000C]+ -> channel(HIDDEN);
COMMENT_LINE: '/*' .*? '*/' -> channel(HIDDEN);
LINE_COMMENT: '//' ~[\r\n]* -> channel(HIDDEN);

// Identifiers (keep this at the end so it doesn't interfere with keywords)
ID: Letter LetterOrDigit*;

// Fragment rules
fragment ExponentPart: [eE] [+-]? Digits;

fragment EscapeSequence:
    '\\' 'u005c'? [btnfr"'\\]
    | '\\' 'u005c'? ([0-3]? [0-7])? [0-7]
    | '\\' 'u'+ HexDigit HexDigit HexDigit HexDigit
;

fragment HexDigits: HexDigit ((HexDigit | '_')* HexDigit)?;

fragment HexDigit: [0-9a-fA-F];

fragment Digits: [0-9] ([0-9_]* [0-9])?;

fragment LetterOrDigit: Letter | [0-9];

fragment Letter:
    [a-zA-Z$_]
    | ~[\u0000-\u007F\uD800-\uDBFF]
    | [\uD800-\uDBFF] [\uDC00-\uDFFF]
;

// Parser rules

// Top-level rules
program: packageDecl? (importDecl | SEMICOLON)* (overrideBodyDecl | (classDecl | SEMICOLON)*) EOF;

// Java-based rules
id: ID;

qualifiedName: id (DOT id)*;

packageDecl: PACKAGE qualifiedName SEMICOLON;

importDecl: IMPORT STATIC? qualifiedName (DOT MULT)? SEMICOLON;

classDecl: CLASS id classBody;

classBody: LBRACE classBodyDecl* RBRACE;

classBodyDecl: block | memberDecl | SEMICOLON;

memberDecl: methodDecl | fieldDecl | constructorDecl | classDecl;

methodDecl: id LPAREN paramList? RPAREN methodBody;

methodBody: block | SEMICOLON;

constructorDecl: id LPAREN paramList? RPAREN constructorBody = block;

fieldDecl: id ASSIGN? expr? SEMICOLON;

variableDecls
    : variableDecl (COMMA variableDecl)*
    ;

variableDecl
    : variableDeclId (ASSIGN variableInit)?
    ;

variableDeclId
    : id (LBRACK RBRACK)*
    ;

variableInit
    : arrayInit
    | expr
    ;

arrayInit
    : LBRACE (variableInit (COMMA variableInit)* COMMA?)? RBRACE
    ;

paramList: id (COMMA id)*;

literal: INT_LITERAL | FLOAT_LITERAL | BOOL_LITERAL | CHAR_LITERAL | STRING_LITERAL | NULL_LITERAL | TEXT_BLOCK;

// Statements & Blocks

block: LBRACE blockStmt* RBRACE;

blockStmt: localVariableDecl SEMICOLON | localClassDecl | stmt;

localVariableDecl: id ASSIGN? expr;

localClassDecl: classDecl;

stmt
    : blockLabel = block
    | ifStmt = IF parExpr stmt (ELSE stmt)?
    | forStmt = FOR LPAREN forControl? RPAREN stmt
    | forEachStmt = FOREACH LPAREN forEachControl RPAREN stmt
    | whileStmt = WHILE LPAREN parExpr RPAREN stmt
    | doStmt = DO stmt WHILE LPAREN parExpr RPAREN SEMICOLON
    | tryStmt = TRY block (catchClause finallyBlock? | finallyBlock)
    | switchStmt = SWITCH parExpr LBRACE switchBlockStmtGroup* switchLabel* RBRACE
    | returnStmt = RETURN expr? SEMICOLON
    | throwStmt = THROW expr SEMICOLON
    | breakStmt = BREAK id? SEMICOLON
    | continueStmt = CONTINUE id? SEMICOLON
    | exprStmt = expr SEMICOLON
    | switchExprStatement = switchExpr SEMICOLON
    | idLabel = id COLON stmt
    | SEMICOLON
    ;

catchClause: CATCH block;

finallyBlock: FINALLY block;

switchBlockStmtGroup: switchLabel+ blockStmt+;

switchLabel
    : CASE (
        constantExpr = expr
        | varName = id
    ) COLON
    | DEFAULT COLON
    ;

forControl: forInit? SEMICOLON expr? SEMICOLON forUpdate = exprList?;

forInit: localVariableDecl | exprList;

forEachControl: id IN expr;

// Expressions
parExpr: LPAREN expr RPAREN;

exprList
    : expr (',' expr)*
    ;

methodCall
    : (id | THIS | SUPER) arguments
    ;

expr
// Primary expressions [16]
    : primary
    | expr LBRACK expr RBRACK
    | expr memberRef = DOT (
        id
        | methodCall
        | THIS
        | NEW id innerCreator
        | SUPER superSuffix
    )
    | methodCall
    | expr DCOLON id
    | id DCOLON (id | NEW)
    | switchExpr
// Postfix unary expressions [15]
    | expr postfix = (INC | DEC)
// Prefix unary expressions [14]
    | prefix = (PLUS | MINUS | INC | DEC | NOT | BITNOT) expr
// Object creation expressions [13]
    | NEW creator
// Multiplicative operators [12]
    | expr mop = (MULT | DIV | MOD) expr
// Additive operators [11]
    | expr aop = (PLUS | MINUS) expr
// Shift operators [10]
    | expr sop = (LSHIFT | RSHIFT | URSHIFT) expr
// Relational operators [9]
    | expr rop = (LT | GT | LE | GE) expr
// Equality operators [8]
    | expr eop = (EQ | NE) expr
// Bitwise AND operator [7]
    | expr baop = BITAND expr
// Bitwise XOR operator [6]
    | expr bxop = BITXOR expr
// Bitwise OR operator [5]
    | expr bop = BITOR expr
// Logic AND operator [4]
    | expr laop = AND expr
// Logic OR operator [3]
    | expr loap = OR expr
// Ternary expression [2]
    | <assoc = right> expr ternary = QUESTION expr COLON expr
// Assignment operators [1]
    | expr assign = (
        ASSIGN
        | ADD_ASSIGN
        | SUB_ASSIGN
        | MUL_ASSIGN
        | DIV_ASSIGN
        | MOD_ASSIGN
        | AND_ASSIGN
        | OR_ASSIGN
        | XOR_ASSIGN
        | LSHIFT_ASSIGN
        | RSHIFT_ASSIGN
        | URSHIFT_ASSIGN
    ) expr
// Lambda expression [0]
    | lambdaExpr
    ;

lambdaExpr
    : lambdaParameters ARROW lambdaBody;

lambdaParameters
    : id
    | LPAREN paramList RPAREN
    | LPAREN id (COMMA id)* RPAREN
    ;

lambdaBody
    : block
    | expr
    ;

primary
    : LPAREN expr RPAREN
    | THIS
    | SUPER
    | literal
    | id
    | id DOT CLASS
    | id DOT id
    ;

switchExpr
    : SWITCH parExpr LBRACE switchLabeledRule* RBRACE
    ;

switchLabeledRule
    : CASE (expr | NULL_LITERAL) (ARROW | COLON) switchRuleOutcome
    | DEFAULT (ARROW | COLON) switchRuleOutcome
    ;

switchRuleOutcome: block | blockStmt*;

creator
    : createdName classCreatorRest
    | createdName arrayCreatorRest
    ;

createdName: id (DOT id)*;

innerCreator: id classCreatorRest;

classCreatorRest: arguments classBody?;

arrayCreatorRest
    : (LBRACK RBRACK)+ arrayInit
    | (LBRACK expr RBRACK)+ (LBRACK RBRACK)*
    ;

superSuffix: arguments;

arguments: LPAREN exprList? RPAREN;

// DSL-specific rules
overrideBodyDecl: classBodyDecl*;