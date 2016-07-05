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

public class Type{
	private char flagType = '0';
	private Symbol c;
	private int numero = 0;
	private int numberVector = 0;
	
	public Type(){

	}
	

	public Type( Symbol c, char flagType ){
		this.c = c;
		this.flagType = flagType;
	}
	
	public Type( Symbol c, char flagType, int numberVector){
		this.c = c;
		this.flagType = flagType;
		this.numberVector = numberVector;
	}

	public int getNumberVector(){
		return numberVector;
	}
	
	public Symbol getType(){
	    return c;
	}
	
	public char getTypeArray(){
		return flagType;
	}
	
	public void setType(Symbol c){
		this.c = c;
	}
	
	
	public int getNumero(){
		return numero;
	}
	
	public void setNumero(int numero){
		this.numero = numero;
	}

}