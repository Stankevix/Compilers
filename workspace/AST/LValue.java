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

public class LValue{
	private Expr expr;
    private Ident ident;

    public LValue(){

    }
    
    public LValue(Ident ident){
        this.ident = ident;
    }
    

	public LValue(Ident ident, Expr expr){
        this.ident = ident;
        this.expr = expr;
    }
    
    public Ident getIdent(){
        return ident;
    }
    
    public Expr getExpr(){
        return expr;
    }
    
    public void genC(PW pw){
        pw.print(ident.getNameIdent());
        if(expr!= null){
            pw.print("[");
            expr.genC(pw);
            pw.print("]");
        }
    }
}