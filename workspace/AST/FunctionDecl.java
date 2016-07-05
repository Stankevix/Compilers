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

public class FunctionDecl{
	private FunctionDecl function = null;
    private StmtBlock stmtBlock = null;
    private Formals formals = null;
    private Ident ident = null;
    private Type type = null;
    
    
    public FunctionDecl(){

    }

    public FunctionDecl(Type type, Ident ident, Formals formals, StmtBlock stmtBlock){
        if(type!=null){
    	    this.type = type;
        }
        if(ident!= null){
            this.ident = ident;
        }
        
        if(formals!=null){
            this.formals = formals;
        }
        if(stmtBlock!=null){
            this.stmtBlock = stmtBlock;
        }
    }
    
    public Type getType(){
        return type;
    }
    
    public Formals getFormals(){
        
        return formals;
    }
    
    
    public StmtBlock getStmtBlock(){
        
        return stmtBlock;
    }
 
    public void genC(PW pw){
        
        if(type != null){
            String s = type.getType().toString();
            pw.out.print(s+" ");
        }else{
            pw.out.print("void ");
        }
        if(ident!= null){
            ident.genC(pw);
        }
        pw.out.print("(");
        if(formals!=null){
            formals.genC(pw);
        }
       
        pw.out.println("){");
        if(stmtBlock!=null){
            stmtBlock.genC(pw);
        }
        pw.out.println("}");
    }
    
}