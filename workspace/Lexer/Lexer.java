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
import java.util.*;
import AST.*;
import AuxComp.*; 

public class Lexer {
    
    public Lexer( char []input, CompilerError error ) {
        this.input = input;
          // add an end-of-file label to make it easy to do the lexer
        input[input.length - 1] = '\0';
          // number of the current line
        lineNumber = 1;
        tokenPos = 0;
        beforeLastTokenPos = 0;
        this.error = error;
    }

      // contains the keywords
    static public Hashtable<String, Symbol> keywordsTable;
    
     // this code will be executed only once for each program execution
    static{
      keywordsTable = new Hashtable<String, Symbol>();
      keywordsTable.put( "if", Symbol.IF );
      keywordsTable.put( "else", Symbol.ELSE );
      keywordsTable.put( "integer", Symbol.INTEGER );
      keywordsTable.put( "boolean", Symbol.BOOLEAN );
      keywordsTable.put( "char", Symbol.CHAR );
      keywordsTable.put( "true", Symbol.TRUE );
      keywordsTable.put( "false", Symbol.FALSE );
      keywordsTable.put( "and", Symbol.AND );
      keywordsTable.put( "or", Symbol.OR );
      keywordsTable.put( "not", Symbol.NOT );
        
      //adicionadas novas palavras chaves 
      keywordsTable.put( "void", Symbol.VOID );
      keywordsTable.put( "main", Symbol.MAIN );
      keywordsTable.put( "int", Symbol.INT );
      keywordsTable.put( "double", Symbol.DOUBLE );
      keywordsTable.put( "char", Symbol.CHAR );
      keywordsTable.put( "while", Symbol.WHILE );
      keywordsTable.put( "break", Symbol.BREAK );
      keywordsTable.put( "print", Symbol.PRINT );
      keywordsTable.put( "return", Symbol.RETURN);
      keywordsTable.put( "readInteger", Symbol.READINTEGER );
      keywordsTable.put( "readDouble", Symbol.READDOUBLE );
      keywordsTable.put( "readChar", Symbol.READCHAR );
        
    }
     
     
    
    //Nova implementação nextToken
    public void nextToken() {
        char ch;
        
        while ((ch = input[tokenPos]) == ' ' || ch == '\r' || ch == '\t' || ch == '\n'){
          // count the number of lines
          if ( ch == '\n')
            lineNumber++;
          tokenPos++;
        }
        
        //chega no final do arquivo
        if ( ch == '\0') 
          token = Symbol.EOF;
        else
          //ignora comentario //
          if (input[tokenPos] == '/' && input[tokenPos + 1] == '/'){
            // comment found
            //tokenPos+=2;
            while (input[tokenPos] != '\0' && input[tokenPos] != '\n'){
              tokenPos++;
            }
            nextToken();
          //ignora comentario / *
          }else if(input[tokenPos] == '/' && input[tokenPos + 1] == '*'){
            // comment found
            tokenPos+=2;
            while(input[tokenPos] != '*' && input[tokenPos] != '/'){//TEORICAMENTE ERA PRA FUNCIONAR!!!!!!!!!!
              tokenPos++;
              if(input[tokenPos] == '\0')
                error.signal("Error: Comment not terminated!");
            }
            tokenPos+=2;
            nextToken();
          }else{
            
            if (Character.isLetter( ch )){ //caso encontre um caracter

              // get an identifier(name) or keyword (palavra reservada)
              StringBuffer ident = new StringBuffer();
              //percorre o token 
              while ( Character.isLetter( input[tokenPos]) || Character.isDigit( input[tokenPos]) || input[tokenPos]== '_'){
                  ident.append(input[tokenPos]);
                  tokenPos++;
                  
              }
              

              nameVariable = ident.toString();//converte para string e verifica se é palavra reservada
              System.out.print(nameVariable+" ");
              // if identStr is in the list of keywords, it is a keyword !
        
              Symbol value = keywordsTable.get(nameVariable);
              if ( value == null ){ //caso não esteja na Lista de palavras reservadas
                String auxbusca = nameVariable.toLowerCase();
                value = keywordsTable.get(auxbusca);
                if (value != null ){
                  error.signal("Error: Keywords must be in Lower Case!");
                }
                token = Symbol.IDENT; //REVER o que token recebe
              }else{
                token = value; 
                if (Character.isDigit(input[tokenPos]))
                  error.signal("Error: Word followed by a number!");
              }
                  
            }else if ( Character.isDigit( ch )){ //caso encontre um digito
                // get a number
                StringBuffer number = new StringBuffer();
                while ( Character.isDigit( input[tokenPos] ) ) {
                    number.append(input[tokenPos]);
                    tokenPos++;
                }
                token = Symbol.NUMBER;
                nameVariable = number.toString();
                try{
                   numberValue = Integer.valueOf(number.toString()).intValue();
                }catch( NumberFormatException e ){
                   error.signal("Error: Number out of limits!");
                }
                if ( numberValue >= MaxValueInteger )
                   error.signal("Error: Number out of limits!");
                System.out.print(numberValue+" ");
            }else{
                tokenPos++;
                System.out.print(ch+" ");
                switch ( ch ) {
                    case '+' :
                      token = Symbol.PLUS;
                      //nameVariable = "+";
                      break;
                    case '-' :
                      token = Symbol.MINUS;
                      //nameVariable = "-";
                      break;
                    case '*' :
                      token = Symbol.MULT;
                      //nameVariable = "*";
                      break;
                    case '/' :
                      token = Symbol.DIV;
                      //nameVariable = "/";
                      break;
                    case '%' :
                      token = Symbol.PCT;
                      //nameVariable = "%";
                      break;
                    case '<' :
                      if ( input[tokenPos] == '=' ) {
                        tokenPos++;
                        System.out.print("= ");
                        token = Symbol.LE;
                        //nameVariable = "<=";
                      }
                      else {
                        if(input[tokenPos+1] == '=')
                          error.signal("Error: Bad Formation!");
                        token = Symbol.LT;
                        //nameVariable = "<";
                      }
                      break;
                    case '>' :
                      if ( input[tokenPos] == '=' ) {
                        tokenPos++;
                        System.out.print("= ");
                        token = Symbol.GE;
                        //nameVariable = ">=";
                      }
                      else{
                        if(input[tokenPos+1] == '=')
                          error.signal("Error: Bad Formation!");
                        token = Symbol.GT;
                        //nameVariable = ">";
                      }
                      break;
                    case '=' :
                      if ( input[tokenPos] == '=' ) {
                        tokenPos++;
                        System.out.print("= ");
                        token = Symbol.EQ;
                        //nameVariable = "==";
                      }
                      else{
                        if(input[tokenPos+1] == '=')
                          error.signal("Error: Bad Formation!");
                        token = Symbol.ASSIGN;
                        //nameVariable = "=";
                      }
                      break;
                    case '{':
                      token = Symbol.LEFTBRACES;
                      //nameVariable = "{";
                      break;
                    case '\\':
                      token = Symbol.IDIV;
                      nameVariable = "\\";
                      break;
                    case '}':
                      token = Symbol.RIGHTBRACES;
                      //nameVariable = "}";
                      break;
                    case '(' :
                      token = Symbol.LEFTPAR;
                      //nameVariable = "(";
                      break;
                    case ')' :
                      token = Symbol.RIGHTPAR;
                      //nameVariable = ")";
                      break;
                    case ',' :
                      token = Symbol.COMMA;
                      nameVariable = ",";
                      break;
                    case '.' :
                      token = Symbol.DOT;
                      //nameVariable = ".";
                      break;
                    case ';' :
                      token = Symbol.SEMICOLON;
                      //nameVariable = ";";
                      break;
                    case '[' :
                      token = Symbol.LEFTBRACKETS;
                      //nameVariable = "[";
                      break;
                    case ']' :
                      token = Symbol.RIGHTBRACKETS;
                      //nameVariable = "]";
                      break;
                    case '!' :
                      if(input[tokenPos] == '='){
                        tokenPos++;
                        token = Symbol.NEQ;
                        //nameVariable = "!=";
                      }else{
                        if(input[tokenPos+1] == '=')
                          error.signal("Error: Bad Formation!");
                        token = Symbol.EM;
                        nameVariable = "!";
                      }
                      break;
                    case '|' :
                      if(input[tokenPos] == '|'){
                        tokenPos++;
                        System.out.print("| ");
                        token = Symbol.MAGNITUDE;
                        //nameVariable = "||";
                      }else{
                        error.signal("Error: Illegal initialization of '||' !");
                      }
                      break;
                    case ':' :
                      if(input[tokenPos] == '='){
                        System.out.print("= ");
                        tokenPos++;
                        token = Symbol.DQ;
                        //nameVariable = ":=";
                      }else{
                        if(input[tokenPos+1] == '=')
                          error.signal("Error: Bad Formation!");
                        nameVariable = ":";
                      }
                      break;
                    case '\"':
                      token = Symbol.QMARK;
                      //nameVariable = "\"";
                      break;
                    case '\'' : 
                      token = Symbol.IBAR;
                      charValue = input[tokenPos];
                      System.out.print(charValue+" ");
                      tokenPos++;
                      //nameVariable = "'";
                      System.out.print("' ");
                      if ( input[tokenPos] != '\'' ) 
                        error.signal("Error: Illegal literal character" + input[tokenPos-1]+"!");
                      tokenPos++;
                      break;
                    default :
                      error.signal("Error: Invalid Character: '" + ch + "'!");
                }
            }
          }
        beforeLastTokenPos = lastTokenPos;
        lastTokenPos = tokenPos - 1;      
    }

    // return the line number of the last token got with getToken()
    public int getLineNumber() {
        return lineNumber;
    }
    
    public int getLineNumberBeforeLastToken() {
        return getLineNumber( beforeLastTokenPos );
    }
    
    
    private int getLineNumber( int index ) {
        // return the line number in which the character input[index] is
        int i, n, size;
        n = 1;
        i = 0;
        size = input.length;
        while ( i < size && i < index ) {
          if ( input[i] == '\n' )
            n++;
          i++;
        }
        return n;
    }
    
    
    public String getCurrentLine() {
        return getLine(lastTokenPos);
    }

    public String getLineBeforeLastToken() {
        return getLine(beforeLastTokenPos);
    }
        
    private String getLine( int index ) {
        // get the line that contains input[index]. Assume input[index] is at a token, not
        // a white space or newline
        
        int i = index;
        if ( i == 0 ) 
          i = 1; 
        else 
          if ( i >= input.length )
            i = input.length;
            
        StringBuffer line = new StringBuffer();
          // go to the beginning of the line
        while ( i >= 1 && input[i] != '\n' )
          i--;
        if ( input[i] == '\n' )
          i++;
          // go to the end of the line putting it in variable line
        while ( input[i] != '\0' && input[i] != '\n' && input[i] != '\r' ) {
            line.append( input[i] );
            i++;
        }
        return line.toString();
    }

    public String getStringValue() {
       return stringValue;
    }
    
    public int getNumberValue() {
       return numberValue;
    }
    
    public char getCharValue() {
       return charValue;
    }
    
    public String getNameVariable(){
      return nameVariable;
    }

    

    // current token
    public Symbol token;
    private String nameVariable;
    private String stringValue;
    private int numberValue;
    private char charValue;
    private int beforeLastTokenPos;
    private int  tokenPos;
    //  input[lastTokenPos] is the last character of the last token
    private int lastTokenPos;
      // program given as input - source code
    private char []input;
    
    // number of current line. Starts with 1
    private int lineNumber;
    
    private CompilerError error;
    private static final int MaxValueInteger = 32768;
}
