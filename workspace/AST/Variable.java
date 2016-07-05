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

public class Variable{
	private Type type;
    private Ident ident;
    private String name; 

    public Variable(){

    }
    
    public Variable(String name){
		this.name = name;
    }
    
	public Variable (Type type, Ident ident){
		this.type = type;
		this.ident = ident;
	}
	
	public Type getVariableType(){
	    return type;
	}
	
	public Ident getVariableIdent(){
	    return ident;
	}
	
	public void genC(PW pw){
		
		if(type.getTypeArray() == '0'){
			
			pw.out.print(type.getType().toString()+" "+ident.getNameIdent());
		}else{
			pw.out.print(type.getType().toString()+" "+ident.getNameIdent()+"["+type.getNumberVector()+"]");
		}
	}
	
}
