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
import java.util.*;

public class StmtBlock{
	private ArrayList<VariableDecl> variableDeclList;
	private ArrayList<Stmt>stmtList;

	public StmtBlock(){

	}

	public  StmtBlock(ArrayList<VariableDecl> variableDeclList, ArrayList<Stmt> stmtList){
		if(!(variableDeclList.isEmpty())){
			this.variableDeclList = variableDeclList;
		}
		if(!(stmtList.isEmpty())){
			this.stmtList = stmtList;
		}
	}
	
	public ArrayList<Stmt> getStmt(){
		ArrayList<Stmt>list =  new ArrayList<Stmt>();
		for(Stmt s : stmtList){
				if(s.getReturnStmt() != null){
					list.add(s);
				}
		}
		return list;
	}
	
	public void genC(PW pw){
		
		if(variableDeclList != null){
			for(VariableDecl v : variableDeclList){
				v.genC(pw);
			}
		}

		pw.out.println("");
		
		if(stmtList != null){
			for(Stmt s : stmtList){
				s.genC(pw);
			}
		}
	}
}
	