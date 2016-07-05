/* =====================================================================
 * Universidade Federal de São Carlos - Campus Sorocaba
 * Compiladores - 2016/1
 * Orientação: Prof.ª Dr.ª Tiemi C. Sakata
 * 
 * Analise sintatica para a linguagem da seção 3
 *
 * Trabalho - Fase final
 *
 * Março de 2016
 * 
 * Gabriel Stankevix Soares		|	511340
 * Luis Augusto França Barbosa	|	511374
===================================================================== */

package Lexer;

public enum Symbol {
      EOF("eof"),
      IDENT("Identifier"),
      NUMBER("Number"),
      PLUS("+"),
      MINUS("-"),
      MULT("*"),
      DIV("/"),
      IDIV("\\"),
      LT("<"),
      LE("<="),
      GT(">"),
      GE(">="),
      EM("!"),
      NEQ("!="),
      DQ(":="),
      EQ("=="),
      ASSIGN("="),
      LEFTPAR("("),
      RIGHTPAR(")"),
      SEMICOLON(";"),
      VAR("var"),
      BEGIN("begin"),
      END("end"),
      IF("if"),
      THEN("then"),
      ELSE("else"),
      ENDIF("endif"),
      COMMA(","),
      READ("read"),

      IBAR("\'"),
      QMARK("\""),
      COLON(":"),
      ALVEOLAR("!"),
      VOID("void"),
      MAIN("main"),
      INT("int"),
      DOUBLE("double"),
      CHAR("char"),
      READINTEGER("readInteger"),
      READDOUBLE("readDouble"),
      READCHAR("readChar"),
      WHILE("while"),
      BREAK("break"),
      PRINT("print"),
      RETURN("return"),
      LEFTBRACES("{"),
      RIGHTBRACES("}"),
      LEFTBRACKETS("["),
      RIGHTBRACKETS("]"),
      AMPERSAND("&"),
      DAMPERSAND("&&"),
      PCT("%"),
      ERROR("error"),
      HASHTAG("#"),
      VBAR("|"),
      MAGNITUDE("||"),
      DOT("."),
      UNDERSCORE("_"),
      NOT("not"),
      AND("and"),
      OR("or"),
      FALSE("false"),
      TRUE("true"),
      BOOLEAN("boolean"),
      INTEGER("integer"),
      WRITE("write");

      Symbol(String name) {
          this.name = name;
      }
      public String toString() { 
            return name; 
      }
      public String name;
}
  
