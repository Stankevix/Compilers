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

public class Formals{
	private Variable variable;
    private ArrayList<Variable> variableList;
    
    public Formals(){

    }
    
    public Formals(Variable variable, ArrayList<Variable> variableList){
        if(variable != null){
            this.variable = variable;
        }    
        if(variableList!=null){
            
            this.variableList = variableList;
        }
    }
    
    public Variable getVariable(){
        return variable;
    }
    
    public ArrayList<Variable> getVariableList(){
        return variableList;
    }
    
    public void genC(PW pw){
    	
    	if(variable!=null){
    	    variable.genC(pw);
    	}
    	
    	for(Variable v: variableList){
    	    pw.out.print(", ");
            v.genC(pw);
        }
    }

    
}