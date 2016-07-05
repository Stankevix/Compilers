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


package AST;
import java.lang.System;
import java.io.*;


public class PW {
   
   public PW(){
      
   }
   
   public void add() {
      currentIndent += step;
   }
   public void sub() {
      currentIndent -= step;
   }
   
   public void set( PrintWriter out ) {
      this.out = out;
      currentIndent = 0;
   }
   
   public void set( int indent ) {
      currentIndent = indent;
   }
   
   public void print( String s ) {
      out.print( space.substring(0, currentIndent) );
      out.print(s);
   }
   
   public void println( String s ) {
      out.print( space.substring(0, currentIndent) );
      out.println(s);
   }
   
   int currentIndent = 0;
   /* there is a Java and a Green mode. 
      indent in Java mode:
      3 6 9 12 15 ...
      indent in Green mode:
      3 6 9 12 15 ...
   */
   static public final int green = 0, java = 1;
   int mode = green; 
   public int step = 1;
   public PrintWriter out;
         
   
   static final private String space = "                                                                                                        ";

}
      
       
