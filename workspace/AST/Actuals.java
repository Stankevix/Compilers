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

public class Actuals{
	private Expr expr;
    private ArrayList<Expr> exprList;
    
    public Actuals(){

    }

    public Actuals(Expr expr, ArrayList<Expr> exprList){
        if(expr!=null){
    	    this.expr = expr;
    	    if(exprList!=null){
    	        this.exprList = exprList;
    	    }
        }
    }
    
    public Expr getExpr(){
        return expr;
    }
    
    public ArrayList<Expr> getExprList(){
        return exprList;
    }
 
    public void genC(PW pw){
        expr.genC(pw);
        if(exprList != null){
            for(Expr e: exprList){
                pw.out.print(",");
                e.genC(pw);
            }
        }
    }
    
}