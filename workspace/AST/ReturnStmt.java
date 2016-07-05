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

public class ReturnStmt{
	private Expr expr = null;

    public ReturnStmt(){

    }

    public ReturnStmt(Expr expr){
        if(expr != null){
    	    this.expr = expr;
        }
    }

    public Expr getExpr(){
        return expr;
    }
 
    public void genC(PW pw){
        pw.out.print("return");
        if(expr!=null){
            pw.out.print("(");
            expr.genC(pw);
            pw.out.print(")");
        }
        pw.out.println(";");
    }
    
}