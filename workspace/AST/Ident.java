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
import Lexer.*;
import java.util.ArrayList;

public class Ident{
	private String nameVariable;
    
    public Ident(){

    }

    
    public Ident(String nameVariable){
        this.nameVariable = nameVariable;
    }
    
    public String getNameIdent(){
        return nameVariable;
    }
    
    public void genC(PW pw){
        pw.out.print(nameVariable+" ");
    }
    
}