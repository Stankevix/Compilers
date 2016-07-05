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

public class Factor{
	private LValue lValue;
    private Expr expr;
    private Numbers numbers;
    private Type type;
    private Calls calls;
    private String message;
    private char c = ' ';
    private int flag;
    private int par = 0;
    
    public Factor(){

    }
    
    public Factor(LValue lValue, String message){
        this.lValue = lValue;
        this.message = message;
    }
    
    public Factor( String message){
        this.message = message;
    }
    
    public Factor(LValue lValue, Expr expr, Type type){
        this.lValue = lValue;
        this.expr = expr;
        this.type = type;
    }
    
    public Factor(LValue lValue, Expr expr, Type type, int par){
        this.lValue = lValue;
        this.expr = expr;
        this.type = type;
        this.par = par;
    }
    
    public Factor(LValue lValue, Numbers numbers, Type type){
        this.lValue = lValue;
        this.numbers = numbers;
        this.type = type;
    }

    public Factor(Expr expr, Type type){
        this.expr = expr;
        this.type = type;
    }
    
    public Factor(LValue lValue, Type type){
        this.lValue = lValue;
        this.type = type;
    }
    
    
    public Factor(LValue lValue, Type type, int flag){
        this.lValue = lValue;
        this.type = type;
        this.flag = flag;
    }


    public Factor(LValue lValue, char c, Type type){
        this.lValue = lValue;
        this.c = c;
        this.type = type;
    }
    
    public Factor(char c, Type type){
        this.c = c;
        this.type = type;
    }
    
    public Factor( Type type){
        this.type = type;
    }

    public Factor(Numbers numbers, Type type){
        this.numbers = numbers;
        this.type = type;
    }
    
    public Factor(char c){
        this.c = c;
        
    }
    
    public Factor(Calls calls, Type type){
        this.calls = calls;
        this.type = type;
    }
    
    public Type getFactorType(){
        return type;
    }
    
    public LValue getLValue(){
        return lValue;
    }
    
    public Calls getCalls(){
        return calls;
    }
    
    
    public void genC(PW pw){
        
        
        if(calls!=null){
            calls.genC(pw);
        }else if(lValue != null){
            if(c != ' '){
                lValue.genC(pw);
                pw.print("='"+c+"'");
            }else{
                
                
                if(type == null){
                    lValue.genC(pw);
                    pw.print("=");
                    if(message != null){
                        pw.print("\"");
                        pw.out.print(message);
                        pw.print("\"");
                    }
                }else if(type.getType() == Symbol.INT && flag == 1){
                    //lValue.genC(pw);
                    pw.print("scanf(\"%d\",&");
                    lValue.genC(pw);
                    pw.print(")");
                }else if(type.getType() == Symbol.DOUBLE && flag == 1){
                    //lValue.genC(pw);
                    pw.print("scanf(\"%lf\",&");
                    lValue.genC(pw);
                    pw.print(")");
                }else if(type.getType() == Symbol.CHAR && flag == 1){
                    //lValue.genC(pw);
                    pw.print("scanf(\"%c\",&");
                    lValue.genC(pw);
                    pw.print(")");
                }else{
                    lValue.genC(pw);
                    
                    if(expr != null){
                            pw.print("=");
                            
                            if(par == 1){
                               pw.print("(");
                                expr.genC(pw);
                                pw.print(")"); 
                            }else{
                                //lValue.genC(pw);
                            
                                expr.genC(pw);
                            }
                            
                        
                    }
                }
            }
        }else{
            //caso NAO tenha lvalue := algumacoisa
            //symbol number
            if(numbers!=null){
                
                if(numbers.getType() == Symbol.INT){
                   pw.out.print(numbers.getValueInt()); 
                }else{
                   pw.out.print(numbers.getValueDouble()); 
                }
            }else{
                if(expr != null){
                    //pw.print("=");
                    if(par == 1){
                        pw.print("(");
                        expr.genC(pw);
                        pw.print(")"); 
                    }else{
                        expr.genC(pw);
                    }
                    
                }else{
                    if(c != ' '){
                        pw.out.print("\'"+c+"\'");
                    }else{
                        pw.out.print(message);
                    }
                }
            }
        }
    }
}


//lValue.genC(pw);
            //caso tenha lvalue := algumacoisa
           /* if(numbers!=null){
                lValue.genC(pw);
                if(numbers.getType() == Symbol.INT){
                   pw.print("="+numbers.getValueInt()); 
                }else{
                   pw.print("="+numbers.getValueDouble()); 
                }
                System.out.println("Print de um number - lvalue := numbers");
            }else{*/