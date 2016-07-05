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

public class SimExpr{
	private Term term;
    private ArrayList<Symbol> addOpList;
    private ArrayList<Term> termList;
	private Symbol unary = Symbol.ERROR;
	private Type type;

    public SimExpr(){

    }
    
    public SimExpr(Term term,Type type){
    	this.term = term;
    	this.type = type;
    }

    public SimExpr(Symbol unary, Term term, Type type){
    	this.unary = unary;
    	this.term = term;
    	this.type = type;
    }

    public SimExpr(Term term, ArrayList<Symbol> addOpList, ArrayList<Term> termList, Type type){
    	this.term = term;
    	this.addOpList = addOpList;
    	this.termList = termList;
    	this.type = type;

    }

    public SimExpr(Symbol unary, Term term, ArrayList<Symbol> addOpList, ArrayList<Term> termList, Type type){
    	this.unary = unary;
    	this.term = term;
    	this.addOpList = addOpList;
    	this.termList = termList;
    	this.type = type;
    }
    
    public Type getSimExprType(){
        return type;
    }
    
    public Term getTerm(){
        return term;
    }
    
    public void genC(PW pw){
        
        
        if(!(unary.equals(Symbol.ERROR) ))
            pw.print(unary.toString());
        term.genC(pw);
        
        if( addOpList != null){
            int i = 0;
            
            //varre os addoplist e termlist ao mesmo tempo
            for(Term t: termList){
                String s = addOpList.get(i).toString();
                pw.print(s);
                t.genC(pw);
                //i++;
            }
        }
        
    }
	
}