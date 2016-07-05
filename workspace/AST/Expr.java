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

public class Expr{
	private Expr expr;
	private Symbol realOp;
    private SimExpr simExpr;
    private Type type;
    private int bloco;

    public Expr(){

    }
    
	public Expr (SimExpr simExpr, Type type){
		this.simExpr = simExpr; 
		this.type = type;
	}

	public Expr ( SimExpr simExpr, Symbol realOp, Expr expr, Type type){
		this.simExpr = simExpr; 
		this.realOp = realOp;
		this.expr = expr;
		this.type = type;
	}
	
	public SimExpr getExprSimExpr(){
		return simExpr;
	}
	
	public Type getExprType(){
		return type;
	}
	
	public void setBloco(int n){
		bloco = n;
	}
	
	public int getBloco(){
		return bloco;
	}
	
	
	
	public void genC(PW pw){
		
		simExpr.genC(pw);
		
		if(expr != null){
			if(realOp == Symbol.ASSIGN && bloco == 1)
				pw.print("==");
			else
				pw.print(""+realOp.toString()+"");
			expr.genC(pw);
		}
		
	}
}