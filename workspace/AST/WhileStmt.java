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

public class WhileStmt{
	private Expr expr;
    private ArrayList<Stmt> stmtList;


    public WhileStmt(){

    }

    public WhileStmt(Expr expr){
    	this.expr = expr;
    }


    public WhileStmt(Expr expr, ArrayList<Stmt> stmtList){
    	this.expr = expr;
    	if(stmtList != null){
    	    this.stmtList = stmtList;
    	}
    }
    
    public void genC (PW pw){

        
        pw.print("while(");
        expr.genC(pw);
    
        pw.out.println("){");
        
        //pw.add();
        
        if(stmtList == null ){
            pw.out.println("}");
        }else{
            for(Stmt s: stmtList){
                s.genC(pw);
            }
            //pw.sub();
            pw.out.println("}");
        }
    }

    
}