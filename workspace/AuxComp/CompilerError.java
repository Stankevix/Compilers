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


package AuxComp;
import AST.*;
import Lexer.*;
import java.util.ArrayList;
import java.util.*;
import java.lang.System;
import java.io.*;

public class CompilerError {
    
    private Lexer lexer;
    private PrintWriter out;
    private boolean thereWasAnError;
    private String arquivo_erro;
    
    public CompilerError( Lexer lexer, PrintWriter out, String arquivo_erro ) {
          // output of an error is done in out
        this.lexer = lexer;
        this.out = out;
        this.arquivo_erro=arquivo_erro;
        thereWasAnError = false;
    }
    
    public void setLexer( Lexer lexer ) {
        this.lexer = lexer;
    }
    
    public boolean wasAnErrorSignalled() {
        return thereWasAnError;
    }

    public void show( String strMessage ) {
        show( strMessage, false );
    }
    
    public void show( String strMessage, boolean goPreviousToken ) {
        // is goPreviousToken is true, the error is signalled at the line of the
        // previous token, not the last one.
        if ( goPreviousToken ) {
            out.println("\nError at the file:"+arquivo_erro);
            out.println("Error at line " + lexer.getLineNumberBeforeLastToken() + ": ");
            out.println( lexer.getLineBeforeLastToken() );
        }
        else {
            out.println("\nError at the file:"+arquivo_erro);
            out.println("Error at line " + lexer.getLineNumber() + ": ");
            out.println(lexer.getCurrentLine());
        }
        
        out.println( strMessage );
        out.flush();
        if ( out.checkError() )
          System.out.println("Error in signaling an error");
        thereWasAnError = true;
    }
       
    public void signal( String strMessage ) {
        show( strMessage );
        out.flush();
        thereWasAnError = true;
        throw new RuntimeException();
    }
    
}
