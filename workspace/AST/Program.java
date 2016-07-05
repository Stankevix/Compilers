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

public class Program{

	private Decl decl;
	private ArrayList<Decl> declList;
	
	public  Program(){

	}
	
	public Program(Decl decl, ArrayList<Decl> declList){
		this.decl = decl;
		if(declList != null){
			this.declList = declList;
		}
	}
	
	public void genC(PW pw){
		
		pw.out.println("#include<stdio.h>");
		pw.out.println("#include<stdlib.h>");
		
		pw.out.println("");
		
		decl.genC(pw);
		if(declList!= null){
			for(Decl d: declList){
				d.genC(pw);
			}
		}
		//pw.sub();
	}

}
