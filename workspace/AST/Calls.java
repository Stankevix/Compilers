
/* =====================================================================
 * Universidade Federal de São Carlos - Campus Sorocaba
 * Compiladores - 2016/1
 * Orientação: Prof.ª Dr.ª Tiemi C. Sakata
 * 
 * Analise sintatica para a linguagem da seção 3
 *
 * Trabalho - Fase Final
 *
 * Março de 2016
 * 
 * Gabriel Stankevix Soares		|	511340
 * Luis Augusto França Barbosa	|	511374
===================================================================== */

package AST;
import Lexer.*;
import java.util.ArrayList;

public class Calls{
	private Ident ident;
    private Actuals actuals;
    
    public Calls(){

    }
    
    public Calls(Ident ident, Actuals actuals){
        this.ident = ident;
        this.actuals = actuals;
    }
    
    public Ident getIdent(){
        return ident;
    }
    
    public Actuals getActuals(){
        return actuals;
    }
    
    public void genC(PW pw){
    	ident.genC(pw);
    	pw.out.print("(");
    	if(actuals!=null){
    	    actuals.genC(pw);
    	}
    	pw.out.print(")");
    }

    
}