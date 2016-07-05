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

public class Term{
	private Factor factor;
    private ArrayList<Symbol> mulOpList;
    private ArrayList<Factor> factorList;
    private Type type;
    
    public Term(){

    }
    
    public Term(Factor factor, Type type){
        this.factor = factor;
        this.type = type;
    }
    
    public Term(Factor factor, ArrayList<Symbol> mulOpList, ArrayList<Factor> factorList, Type type){
        this.factor = factor;
        this.mulOpList = mulOpList;
        this.factorList = factorList;
        this.type = type;
    }
	
	public Type getTermType(){
	    return type;
	}
	
	public Factor getFactor(){
	    return factor;
	}

	
	public void genC(PW pw){
	    
	    
	    factor.genC(pw);
	    if(factorList != null) {
	        int i = 0;
	        for(Factor f : factorList){
	            pw.print(""+mulOpList.get(i).toString()+"");
	            f.genC(pw);
	            //i++;
	        }
	    }
	}
}