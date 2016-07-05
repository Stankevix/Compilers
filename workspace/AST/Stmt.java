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

public class Stmt{
	private Expr expr;
    private IfStmt ifStmt;
    private WhileStmt whileStmt;
    private PrintStmt printStmt;
    private BreakStmt breakStmt;
	private ReturnStmt returnStmt;

    public Stmt(){

    }
    
	public Stmt (Expr expr){
		this.expr = expr;
	}

	public Stmt ( IfStmt ifStmt){
		this.ifStmt = ifStmt;
	}

	public Stmt (PrintStmt printStmt){
		this.printStmt = printStmt;
	}

	public Stmt (BreakStmt breakStmt){
		this.breakStmt = breakStmt;
	}

	public Stmt (WhileStmt whileStmt){
		this.whileStmt = whileStmt;
	}
	
	public Stmt (ReturnStmt returnStmt){
		this.returnStmt = returnStmt;
	}
	
	
	public ReturnStmt getReturnStmt(){
		return returnStmt;
	}
	
	public void genC(PW pw){
		
		if(expr != null){
			expr.genC(pw);
			pw.out.println(";");
		}else if(ifStmt != null){
			ifStmt.genC(pw);
		}else if(printStmt != null){
			printStmt.genC(pw);
			pw.out.println(";");
		}else if(breakStmt != null){
			breakStmt.genC(pw);
		}else if(whileStmt != null){
			whileStmt.genC(pw);
		}else if(returnStmt != null){
			returnStmt.genC(pw);
		}
		
	}

}
