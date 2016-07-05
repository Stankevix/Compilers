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
 * Gabriel Stankevix Soares     |   511340
 * Luis Augusto França Barbosa  |   511374
===================================================================== */

package AST;
import Lexer.*;
import java.util.ArrayList;

public class IfStmt{
	private Expr expr;
    private ArrayList<Stmt> stmtList;
    private ArrayList<Stmt> stmt2List;

    public IfStmt(){

    }

    public IfStmt(Expr expr){
    	this.expr = expr;
    }


    public IfStmt(Expr expr, ArrayList<Stmt> stmtList){
    	this.expr = expr;
    	this.stmtList = stmtList;
    }

    public IfStmt(Expr expr, ArrayList<Stmt> stmtList, ArrayList<Stmt> stmt2List){
    	this.expr = expr;
    	
    	if(stmtList != null){
    	    this.stmtList = stmtList;
    	}
    	if(stmt2List != null){
    	    this.stmt2List = stmt2List;
    	}
    }
 
    public void genC(PW pw){
        pw.print("if(");
        
        expr.genC(pw);
        
        pw.out.println("){");
        
        
        for(Stmt s: stmtList){
            s.genC(pw);
        }

        if(stmt2List == null){
            //pw.sub();
            pw.out.println("}");
        }else{
            //pw.sub();
            pw.out.println("}else{");
            //pw.add();
            for(Stmt s2: stmt2List){
                s2.genC(pw);
            }
            //pw.sub();
            pw.out.println("}");
        }
        
    }
    
}