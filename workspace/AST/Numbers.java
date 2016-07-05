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

public class Numbers{
	private double intDouble;
	private int inteiro;
	private Symbol type;

	
	public Numbers(Symbol type, int inteiro){
		this.type = type;
		this.inteiro = inteiro;
	}
	
	public Numbers(Symbol type, double intDouble){
		this.type = type;
		this.intDouble = intDouble;
	}
	
	public Symbol getType(){
		return type;
	}
	
	public int getValueInt(){
		return inteiro;
	}
	
	public double getValueDouble(){
		return intDouble;
	}
}