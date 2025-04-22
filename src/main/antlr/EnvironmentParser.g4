parser grammar EnvironmentParser;

@header {
package net.ingoh.myagents.lang.parsers;
}

options { tokenVocab=EnvironmentLexer; }

program: (typeDecl | nameDecl | agentsDecl)* tickBlock EOF;

typeDecl: TYPE ENVIRONMENT SEMICOLON;
nameDecl: NAME ID SEMICOLON;
agentsDecl: AGENTS ID+ SEMICOLON;
tickBlock: TICK LPAREN TIME_LITERAL RPAREN blockRetBool;

blockRetBool: LBRACE statement* RETURN BOOL_LITERAL SEMICOLON RBRACE;

statement
    : PRINT STRING_LITERAL SEMICOLON
    ;