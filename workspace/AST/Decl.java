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

public class Decl{
	private FunctionDecl function;

	public  Decl(){

	}

	public  Decl(FunctionDecl function){
		this.function = function;
	}
	
	public FunctionDecl getFunction (){
		return function;
	}
	
	public void genC(PW pw){
		
		if(function!= null){
			function.genC(pw);
		}

	}

}