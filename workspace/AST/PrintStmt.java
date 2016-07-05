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

public class PrintStmt{
	private Expr expr;
    private ArrayList<Expr> exprList;


    public PrintStmt(){

    }

    public PrintStmt(Expr expr){
    	this.expr = expr;
    }


    public PrintStmt(Expr expr, ArrayList<Expr> exprList){
    	this.expr = expr;
    	if(exprList!=null){
    	    this.exprList = exprList;
    	}
    }
    
    
    public void genC(PW pw){

        pw.print("printf(");

        Type type = expr.getExprType();
        
        if(type!=null){
            if(exprList == null){
                if(type.getType().equals(Symbol.INT)){
                    pw.out.print("\"%d\",");
                }else if(type.getType().equals(Symbol.DOUBLE)){
                    pw.out.print("\"%lf\",");
                }else if(type.getType().equals(Symbol.CHAR)){
                    pw.out.print("\"%c\",");
                }
                expr.genC(pw);
                pw.out.print(")");
                
            }else{
                
                if(type.getType().equals(Symbol.INT)){
                    pw.out.print("\"%d");
                }else if(type.getType().equals(Symbol.DOUBLE)){
                    pw.out.print("\"%lf");
                }else if(type.getType().equals(Symbol.CHAR)){
                    pw.out.print("\"%c");
                }
    
                
                for(Expr e : exprList){
                    type = e.getExprType();
                    
                    if(type.getType().equals(Symbol.INT)){
                        pw.out.print("%d");
                    }else if(type.getType().equals(Symbol.DOUBLE)){
                        pw.out.print("%lf");
                    }else if(type.getType().equals(Symbol.CHAR)){
                        pw.out.print("%c");
                    }
                }
                
                pw.out.print("\",");
                expr.genC(pw);
                for(Expr f : exprList){
                    pw.print(",");
                    f.genC(pw);
                }
                
                pw.out.print(")");
            }
        }else{
            
            
            if(exprList == null){
                pw.print("\"");
                expr.genC(pw);
                pw.print("\")");
            }else{
                pw.print("\"");
                expr.genC(pw);
                pw.print("\"");
                for(Expr f : exprList){
                    pw.print(",");
                    f.genC(pw);
                }
                pw.print(")");
            }
            
        }
    }
    
}