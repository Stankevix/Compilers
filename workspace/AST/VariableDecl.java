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

public class VariableDecl{
	private Variable variable;

    public VariableDecl(){

    }
    
	public VariableDecl(Variable variable){
		this.variable = variable;

	}
	
	public void genC(PW pw){
		Type type = variable.getVariableType();
		Ident ident = variable.getVariableIdent();

		if(type.getTypeArray() == '0'){
			
			pw.out.println(type.getType().toString()+" "+ident.getNameIdent()+";");
		}else{
			pw.out.println(type.getType().toString()+" "+ident.getNameIdent()+"["+type.getNumberVector()+"];");
		}
	}
	
	
}
